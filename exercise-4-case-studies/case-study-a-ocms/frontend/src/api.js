// small wrapper around fetch: adds the token and turns errors into Error(message)

const TOKEN_KEY = 'ocms_token';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  } else {
    localStorage.removeItem(TOKEN_KEY);
  }
}

export async function api(path, { method = 'GET', body } = {}) {
  const headers = {};
  if (body !== undefined) {
    headers['Content-Type'] = 'application/json';
  }
  const token = getToken();
  if (token) {
    headers.Authorization = 'Bearer ' + token;
  }

  const response = await fetch('/api' + path, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  // an expired or invalid token: send the user back to the login page
  if (response.status === 401 && !path.startsWith('/auth/')) {
    setToken(null);
    window.dispatchEvent(new Event('ocms-logout'));
  }

  if (!response.ok) {
    let message = 'Something went wrong (' + response.status + ')';
    try {
      const data = await response.json();
      if (data.message) {
        message = data.message;
      }
    } catch (e) {
      // the response had no JSON body
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }
  return response.json();
}
