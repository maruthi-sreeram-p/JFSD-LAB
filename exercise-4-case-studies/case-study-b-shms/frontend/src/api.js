// small wrapper around fetch: turns an error response into Error(message)

export async function api(path, { method = 'GET', body } = {}) {
  const response = await fetch('/api' + path, {
    method,
    headers: body !== undefined ? { 'Content-Type': 'application/json' } : {},
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

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

export function formatTime(iso) {
  return new Date(iso).toLocaleString([], { dateStyle: 'medium', timeStyle: 'medium' });
}

// the normal ranges, shown next to the input fields (they match SafeRanges.java in the backend)
export const SAFE_RANGES = {
  heartRate: '60-100 bpm',
  bloodPressure: '90-140 / 60-90 mmHg',
  oxygenLevel: '95-100 %',
  temperature: '36.1-37.5 °C',
};
