import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../api.js';

export default function Doctors() {
  const navigate = useNavigate();
  const [doctors, setDoctors] = useState([]);
  const [form, setForm] = useState({ name: '', specialization: '', contact: '' });
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/doctors')
      .then(setDoctors)
      .catch((err) => setMessage({ type: 'error', text: err.message }))
      .finally(() => setLoading(false));
  }, []);

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function addDoctor(e) {
    e.preventDefault();
    setMessage({ type: '', text: '' });
    try {
      const created = await api('/doctors', { method: 'POST', body: form });
      setDoctors([...doctors, created].sort((a, b) => a.name.localeCompare(b.name)));
      setForm({ name: '', specialization: '', contact: '' });
      setMessage({ type: 'ok', text: created.name + ' was added.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  function openDashboard(doctor) {
    localStorage.setItem('shms_doctor', String(doctor.id));
    navigate('/');
  }

  return (
    <>
      <h1>Doctors</h1>

      {message.text && <p className={message.type === 'error' ? 'error' : 'success'}>{message.text}</p>}

      <div className="two-columns">
        <section>
          {loading && <p className="muted">Loading...</p>}
          {!loading && doctors.length === 0 && <p className="muted">No doctor has been added yet.</p>}
          <div className="stack">
            {doctors.map((d) => (
              <article key={d.id} className="panel row-between">
                <div>
                  <h3>{d.name}</h3>
                  <p className="muted">{d.specialization || 'General'}</p>
                  <p className="muted small-text">
                    {d.contact || 'no contact'} - {d.patientCount} patient{d.patientCount === 1 ? '' : 's'}
                  </p>
                </div>
                <button className="btn ghost small" onClick={() => openDashboard(d)}>
                  Open dashboard
                </button>
              </article>
            ))}
          </div>
        </section>

        <form className="panel" onSubmit={addDoctor}>
          <h3>Add a doctor</h3>
          <label>
            Name
            <input name="name" value={form.name} onChange={change} required />
          </label>
          <label>
            Specialization
            <input name="specialization" value={form.specialization} onChange={change} />
          </label>
          <label>
            Contact
            <input name="contact" value={form.contact} onChange={change} />
          </label>
          <button className="btn">Add doctor</button>
          <p className="muted small-text">
            To give a doctor patients, open a patient and choose the doctor there.
          </p>
        </form>
      </div>
    </>
  );
}
