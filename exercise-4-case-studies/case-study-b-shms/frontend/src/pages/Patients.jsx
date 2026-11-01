import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api.js';
import PatientForm from '../components/PatientForm.jsx';

export default function Patients() {
  const [patients, setPatients] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/patients')
      .then(setPatients)
      .catch((err) => setMessage({ type: 'error', text: err.message }))
      .finally(() => setLoading(false));
  }, []);

  async function register(form) {
    setMessage({ type: '', text: '' });
    try {
      const created = await api('/patients', { method: 'POST', body: form });
      setPatients([...patients, created].sort((a, b) => a.name.localeCompare(b.name)));
      setShowForm(false);
      setMessage({ type: 'ok', text: created.name + ' was registered.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  return (
    <>
      <div className="row-between">
        <h1>Patients</h1>
        {!showForm && (
          <button className="btn" onClick={() => setShowForm(true)}>
            Register patient
          </button>
        )}
      </div>

      {message.text && <p className={message.type === 'error' ? 'error' : 'success'}>{message.text}</p>}

      {showForm && (
        <div className="panel">
          <h3>Register a patient</h3>
          <PatientForm submitLabel="Register" onSubmit={register} onCancel={() => setShowForm(false)} />
        </div>
      )}

      {loading && <p className="muted">Loading...</p>}
      {!loading && patients.length === 0 && !showForm && <p className="muted">No patient is registered yet.</p>}

      {patients.length > 0 && (
        <div className="panel table-wrap">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Age / gender</th>
                <th>Blood group</th>
                <th>Contact</th>
                <th>Doctor</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((p) => (
                <tr key={p.id}>
                  <td>
                    <Link to={'/patients/' + p.id}>{p.name}</Link>
                  </td>
                  <td>
                    {p.age} / {p.gender}
                  </td>
                  <td>{p.bloodGroup || '-'}</td>
                  <td>{p.contact || '-'}</td>
                  <td>{p.doctorName || <span className="muted">not assigned</span>}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
