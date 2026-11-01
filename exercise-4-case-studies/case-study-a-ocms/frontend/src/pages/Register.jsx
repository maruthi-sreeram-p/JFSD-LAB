import { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext.jsx';

export default function Register() {
  const { user, register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: '', email: '', contact: '', password: '', role: 'STUDENT' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  if (user) {
    return <Navigate to="/" replace />;
  }

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setBusy(true);
    try {
      await register(form);
      navigate('/');
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="auth-page">
      <form className="panel auth-card" onSubmit={handleSubmit}>
        <h1>Create account</h1>
        <p className="muted">Join as a student or as an instructor</p>

        {error && <p className="error">{error}</p>}

        <div className="role-switch">
          <label className={form.role === 'STUDENT' ? 'selected' : ''}>
            <input type="radio" name="role" value="STUDENT" checked={form.role === 'STUDENT'} onChange={change} />
            Student
          </label>
          <label className={form.role === 'INSTRUCTOR' ? 'selected' : ''}>
            <input type="radio" name="role" value="INSTRUCTOR" checked={form.role === 'INSTRUCTOR'} onChange={change} />
            Instructor
          </label>
        </div>

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
        <label>
          Password
          <input name="password" type="password" value={form.password} onChange={change} minLength={6} required />
        </label>

        <button className="btn" disabled={busy}>
          {busy ? 'Creating...' : 'Register'}
        </button>
        <p className="muted center">
          Already registered? <Link to="/login">Sign in</Link>
        </p>
      </form>
    </div>
  );
}
