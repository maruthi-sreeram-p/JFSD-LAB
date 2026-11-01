import { createContext, useContext, useEffect, useState } from 'react';
import { api, getToken, setToken } from './api.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(Boolean(getToken()));

  // if a token is saved from an earlier visit, check that it still works
  useEffect(() => {
    if (!getToken()) {
      return;
    }
    api('/users/me')
      .then(setUser)
      .catch(() => setToken(null))
      .finally(() => setLoading(false));
  }, []);

  // the api helper fires this when the server answers 401
  useEffect(() => {
    const onLogout = () => setUser(null);
    window.addEventListener('ocms-logout', onLogout);
    return () => window.removeEventListener('ocms-logout', onLogout);
  }, []);

  async function login(email, password) {
    const data = await api('/auth/login', { method: 'POST', body: { email, password } });
    setToken(data.token);
    setUser(data.user);
    return data.user;
  }

  async function register(form) {
    const data = await api('/auth/register', { method: 'POST', body: form });
    setToken(data.token);
    setUser(data.user);
    return data.user;
  }

  function logout() {
    setToken(null);
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, setUser, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
