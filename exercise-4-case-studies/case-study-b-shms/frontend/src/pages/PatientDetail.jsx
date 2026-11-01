import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { SAFE_RANGES, api, formatTime } from '../api.js';
import PatientForm from '../components/PatientForm.jsx';

const EMPTY_VITALS = { heartRate: '', systolic: '', diastolic: '', oxygenLevel: '', temperature: '' };

// a table cell that turns amber or red when the value is out of range
function Cell({ value, flag }) {
  return <td className={flag ? 'cell-' + flag.toLowerCase() : ''}>{value}</td>;
}

export default function PatientDetail() {
  const { id } = useParams();
  const [patient, setPatient] = useState(null);
  const [doctors, setDoctors] = useState([]);
  const [history, setHistory] = useState([]);
  const [vitals, setVitals] = useState(EMPTY_VITALS);
  const [result, setResult] = useState(null);
  const [message, setMessage] = useState({ type: '', text: '' });

  const loadHistory = useCallback(() => {
    api('/patients/' + id + '/vitals?limit=20')
      .then(setHistory)
      .catch(() => {});
  }, [id]);

  useEffect(() => {
    Promise.all([api('/patients/' + id), api('/doctors')])
      .then(([p, d]) => {
        setPatient(p);
        setDoctors(d);
      })
      .catch((err) => setMessage({ type: 'error', text: err.message }));
    loadHistory();
    // keep the history up to date while this page is open
    const timer = setInterval(loadHistory, 5000);
    return () => clearInterval(timer);
  }, [id, loadHistory]);

  async function assignDoctor(e) {
    const doctorId = e.target.value === '' ? null : Number(e.target.value);
    try {
      setPatient(await api('/patients/' + id + '/doctor', { method: 'PUT', body: { doctorId } }));
      setMessage({ type: 'ok', text: doctorId ? 'Doctor assigned.' : 'Doctor removed.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  async function savePatient(form) {
    setMessage({ type: '', text: '' });
    try {
      setPatient(await api('/patients/' + id, { method: 'PUT', body: form }));
      setMessage({ type: 'ok', text: 'Details updated.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  function changeVital(e) {
    setVitals({ ...vitals, [e.target.name]: e.target.value });
  }

  async function recordVitals(e) {
    e.preventDefault();
    setResult(null);
    setMessage({ type: '', text: '' });
    try {
      const body = {
        heartRate: Number(vitals.heartRate),
        systolic: Number(vitals.systolic),
        diastolic: Number(vitals.diastolic),
        oxygenLevel: Number(vitals.oxygenLevel),
        temperature: Number(vitals.temperature),
      };
      setResult(await api('/patients/' + id + '/vitals', { method: 'POST', body }));
      setVitals(EMPTY_VITALS);
      loadHistory();
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  if (!patient) {
    return message.text ? <p className="error">{message.text}</p> : <p className="muted">Loading...</p>;
  }

  return (
    <>
      <p>
        <Link to="/patients">&larr; All patients</Link>
      </p>
      <h1>{patient.name}</h1>

      {message.text && <p className={message.type === 'error' ? 'error' : 'success'}>{message.text}</p>}

      <div className="two-columns">
        <section className="panel">
          <h3>Details</h3>
          <label>
            Doctor monitoring this patient
            <select value={patient.doctorId || ''} onChange={assignDoctor}>
              <option value="">Not assigned</option>
              {doctors.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name}
                  {d.specialization ? ' - ' + d.specialization : ''}
                </option>
              ))}
            </select>
          </label>
          <PatientForm key={patient.id} initial={patient} submitLabel="Save details" onSubmit={savePatient} />
        </section>

        <section className="panel">
          <h3>Record vital signs</h3>
          <form onSubmit={recordVitals}>
            <div className="form-grid">
              <label>
                Heart rate (bpm)
                <input name="heartRate" type="number" value={vitals.heartRate} onChange={changeVital} required />
                <span className="hint">safe: {SAFE_RANGES.heartRate}</span>
              </label>
              <label>
                Oxygen level (%)
                <input name="oxygenLevel" type="number" value={vitals.oxygenLevel} onChange={changeVital} required />
                <span className="hint">safe: {SAFE_RANGES.oxygenLevel}</span>
              </label>
              <label>
                Systolic (mmHg)
                <input name="systolic" type="number" value={vitals.systolic} onChange={changeVital} required />
              </label>
              <label>
                Diastolic (mmHg)
                <input name="diastolic" type="number" value={vitals.diastolic} onChange={changeVital} required />
              </label>
            </div>
            <span className="hint">blood pressure safe: {SAFE_RANGES.bloodPressure}</span>
            <label>
              Temperature (&deg;C)
              <input
                name="temperature"
                type="number"
                step="0.1"
                value={vitals.temperature}
                onChange={changeVital}
                required
              />
              <span className="hint">safe: {SAFE_RANGES.temperature}</span>
            </label>
            <button className="btn">Save reading</button>
          </form>

          {result && result.alerts.length === 0 && <p className="success">Reading saved. All values are in the safe range.</p>}
          {result && result.alerts.length > 0 && (
            <div className="error">
              <b>
                Reading saved - {result.alerts.length} alert{result.alerts.length > 1 ? 's' : ''} raised
              </b>
              <ul>
                {result.alerts.map((a) => (
                  <li key={a.id}>{a.message}</li>
                ))}
              </ul>
            </div>
          )}
        </section>
      </div>

      <h2>Reading history</h2>
      {history.length === 0 ? (
        <p className="muted">No readings yet.</p>
      ) : (
        <div className="panel table-wrap">
          <table>
            <thead>
              <tr>
                <th>Time</th>
                <th>Heart rate</th>
                <th>Blood pressure</th>
                <th>Oxygen</th>
                <th>Temperature</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {history.map((r) => (
                <tr key={r.id}>
                  <td>{formatTime(r.recordedAt)}</td>
                  <Cell value={r.heartRate + ' bpm'} flag={r.flags.heartRate} />
                  <Cell
                    value={r.systolic + ' / ' + r.diastolic + ' mmHg'}
                    flag={r.flags.systolic === 'CRITICAL' || r.flags.diastolic === 'CRITICAL' ? 'CRITICAL' : r.flags.systolic || r.flags.diastolic}
                  />
                  <Cell value={r.oxygenLevel + ' %'} flag={r.flags.oxygenLevel} />
                  <Cell value={r.temperature + ' °C'} flag={r.flags.temperature} />
                  <td>
                    <span className={'status ' + r.status.toLowerCase()}>{r.status.toLowerCase()}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
