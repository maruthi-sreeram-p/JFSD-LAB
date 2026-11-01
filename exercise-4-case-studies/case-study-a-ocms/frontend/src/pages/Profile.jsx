import { useState } from 'react';
import { api } from '../api.js';
import { useAuth } from '../AuthContext.jsx';

export default function Profile() {
  const { user, setUser } = useAuth();
  const [form, setForm] = useState({ name: user.name, email: user.email, contact: user.contact || '' });
  const [message, setMessage] = useState({ type: '', text: '' });
  const [busy, setBusy] = useState(false);

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setMessage({ type: '', text: '' });
    setBusy(true);
    try {
      const updated = await api('/users/me', { method: 'PUT', body: form });
      setUser(updated);
      setMessage({ type: 'ok', text: 'Your details were updated.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <h1>My profile</h1>
      <form className="panel narrow" onSubmit={handleSubmit}>
        <p className="muted">
          Signed in as a <b>{user.role.toLowerCase()}</b>
        </p>

        {message.text && <p className={message.type === 'error' ? 'error' : 'success'}>{message.text}</p>}

        <label>
          Name
          <input name="name" value={form.name} onChange={change} required />
        </label>
        <label>
          Email
          <input name="email" type="email" value={form.email} onChange={change} required />
        </label>
        <label>
          Contact number
          <input name="contact" value={form.contact} onChange={change} />
        </label>

        <button className="btn" disabled={busy}>
          {busy ? 'Saving...' : 'Save changes'}
        </button>
      </form>
    </>
  );
}
