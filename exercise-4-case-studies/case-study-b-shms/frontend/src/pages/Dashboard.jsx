import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api, formatTime } from '../api.js';
import Vital from '../components/Vital.jsx';

const REFRESH_SECONDS = 5;

const STATUS_TEXT = {
  CRITICAL: 'Critical',
  WARNING: 'Warning',
  STABLE: 'Stable',
  NO_DATA: 'No readings',
};

function PatientCard({ summary }) {
  const { patient, latest, status, openAlerts } = summary;
  const flags = latest ? latest.flags : {};

  return (
    <article className={'panel patient-card ' + status.toLowerCase()}>
      <div className="row-between">
        <div>
          <h3>
            <Link to={'/patients/' + patient.id}>{patient.name}</Link>
          </h3>
          <p className="muted small-text">
            {patient.age} yrs, {patient.gender}
            {patient.bloodGroup ? ', ' + patient.bloodGroup : ''}
          </p>
        </div>
        <span className={'status ' + status.toLowerCase()}>{STATUS_TEXT[status]}</span>
      </div>

      {latest ? (
        <>
          <div className="vitals">
            <Vital label="Heart rate" value={latest.heartRate} unit="bpm" flag={flags.heartRate} />
            <Vital
              label="Blood pressure"
              value={latest.systolic + '/' + latest.diastolic}
              unit="mmHg"
              flag={worst(flags.systolic, flags.diastolic)}
            />
            <Vital label="Oxygen" value={latest.oxygenLevel} unit="%" flag={flags.oxygenLevel} />
            <Vital label="Temperature" value={latest.temperature} unit={'°C'} flag={flags.temperature} />
          </div>
          <p className="muted small-text">Last reading {formatTime(latest.recordedAt)}</p>
        </>
      ) : (
        <p className="muted">No vital readings recorded yet.</p>
      )}

      {openAlerts > 0 && (
        <p className="small-text alert-count">
          {openAlerts} open alert{openAlerts > 1 ? 's' : ''}
        </p>
      )}
    </article>
  );
}

// the blood pressure box takes the worse of its two values
function worst(a, b) {
  if (a === 'CRITICAL' || b === 'CRITICAL') {
    return 'CRITICAL';
  }
  return a || b;
}

export default function Dashboard() {
  const [doctors, setDoctors] = useState(null);
  const [doctorId, setDoctorId] = useState(localStorage.getItem('shms_doctor') || '');
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [updatedAt, setUpdatedAt] = useState(null);

  useEffect(() => {
    api('/doctors')
      .then((list) => {
        setDoctors(list);
        // open the first doctor when nothing valid was chosen before
        if (list.length > 0 && !list.some((d) => String(d.id) === doctorId)) {
          setDoctorId(String(list[0].id));
        }
      })
      .catch((err) => setError(err.message));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // load the dashboard now and then again every few seconds
  useEffect(() => {
    if (!doctorId) {
      return;
    }
    localStorage.setItem('shms_doctor', doctorId);

    let stopped = false;
    function load() {
      api('/doctors/' + doctorId + '/dashboard')
        .then((d) => {
          if (!stopped) {
            setData(d);
            setUpdatedAt(new Date());
            setError('');
          }
        })
        .catch((err) => !stopped && setError(err.message));
    }
    load();
    const timer = setInterval(load, REFRESH_SECONDS * 1000);
    return () => {
      stopped = true;
      clearInterval(timer);
    };
  }, [doctorId]);

  async function acknowledge(alert) {
    try {
      await api('/alerts/' + alert.id + '/acknowledge', { method: 'POST' });
      setData({
        ...data,
        openAlerts: data.openAlerts.filter((a) => a.id !== alert.id),
        patients: data.patients.map((s) =>
          s.patient.id === alert.patientId ? { ...s, openAlerts: Math.max(0, s.openAlerts - 1) } : s
        ),
      });
    } catch (err) {
      setError(err.message);
    }
  }

  if (doctors && doctors.length === 0) {
    return (
      <>
        <h1>Doctor dashboard</h1>
        <p className="muted">
          There is no doctor yet. <Link to="/doctors">Add a doctor</Link> and assign patients to see the dashboard.
        </p>
      </>
    );
  }

  const critical = data ? data.patients.filter((p) => p.status === 'CRITICAL').length : 0;

  return (
    <>
      <div className="row-between">
        <h1>Doctor dashboard</h1>
        <div className="live">
          <span className="live-dot" /> Live, refreshes every {REFRESH_SECONDS} s
          {updatedAt && <span className="muted small-text"> - updated {updatedAt.toLocaleTimeString()}</span>}
        </div>
      </div>

      <div className="toolbar">
        <select value={doctorId} onChange={(e) => setDoctorId(e.target.value)}>
          {(doctors || []).map((d) => (
            <option key={d.id} value={d.id}>
              {d.name}
              {d.specialization ? ' - ' + d.specialization : ''}
            </option>
          ))}
        </select>
      </div>

      {error && <p className="error">{error}</p>}

      {data && (
        <>
          <div className="stats">
            <div className="panel stat">
              <b>{data.patients.length}</b>
              <span className="muted">patients</span>
            </div>
            <div className="panel stat">
              <b className={critical > 0 ? 'text-critical' : ''}>{critical}</b>
              <span className="muted">critical now</span>
            </div>
            <div className="panel stat">
              <b className={data.openAlerts.length > 0 ? 'text-warning' : ''}>{data.openAlerts.length}</b>
              <span className="muted">open alerts</span>
            </div>
          </div>

          {data.patients.length === 0 ? (
            <p className="muted">
              No patient is assigned to this doctor. Open a patient in <Link to="/patients">Patients</Link> and choose the doctor.
            </p>
          ) : (
            <div className="grid">
              {data.patients.map((s) => (
                <PatientCard key={s.patient.id} summary={s} />
              ))}
            </div>
          )}

          <h2>Open alerts</h2>
          {data.openAlerts.length === 0 ? (
            <p className="muted">No open alerts. Every value is in its safe range.</p>
          ) : (
            <div className="stack">
              {data.openAlerts.map((a) => (
                <div key={a.id} className={'panel alert-row ' + a.severity.toLowerCase()}>
                  <div>
                    <b>
                      <Link to={'/patients/' + a.patientId}>{a.patientName}</Link>
                    </b>{' '}
                    <span className={'status ' + a.severity.toLowerCase()}>{a.severity}</span>
                    <p>{a.message}</p>
                    <span className="muted small-text">{formatTime(a.createdAt)}</span>
                  </div>
                  <button className="btn ghost small" onClick={() => acknowledge(a)}>
                    Acknowledge
                  </button>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </>
  );
}
