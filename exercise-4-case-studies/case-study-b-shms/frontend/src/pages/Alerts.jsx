import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api, formatTime } from '../api.js';

export default function Alerts() {
  const [alerts, setAlerts] = useState([]);
  const [openOnly, setOpenOnly] = useState(true);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  // load now, then every 5 seconds so new alerts show up without a reload
  useEffect(() => {
    let stopped = false;
    function load() {
      api('/alerts?open=' + openOnly)
        .then((list) => {
          if (!stopped) {
            setAlerts(list);
            setError('');
          }
        })
        .catch((err) => !stopped && setError(err.message))
        .finally(() => !stopped && setLoading(false));
    }
    load();
    const timer = setInterval(load, 5000);
    return () => {
      stopped = true;
      clearInterval(timer);
    };
  }, [openOnly]);

  async function acknowledge(alert) {
    try {
      const updated = await api('/alerts/' + alert.id + '/acknowledge', { method: 'POST' });
      setAlerts(openOnly ? alerts.filter((a) => a.id !== alert.id) : alerts.map((a) => (a.id === alert.id ? updated : a)));
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <>
      <div className="row-between">
        <h1>Alerts</h1>
        <div className="tabs">
          <button className={openOnly ? 'active' : ''} onClick={() => setOpenOnly(true)}>
            Open
          </button>
          <button className={!openOnly ? 'active' : ''} onClick={() => setOpenOnly(false)}>
            All
          </button>
        </div>
      </div>

      {error && <p className="error">{error}</p>}
      {loading && <p className="muted">Loading...</p>}
      {!loading && alerts.length === 0 && (
        <p className="muted">{openOnly ? 'No open alerts. Every patient is in the safe range.' : 'No alerts have been raised yet.'}</p>
      )}

      {alerts.length > 0 && (
        <div className="panel table-wrap">
          <table>
            <thead>
              <tr>
                <th>Time</th>
                <th>Patient</th>
                <th>Alert</th>
                <th>Severity</th>
                <th>Doctor</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {alerts.map((a) => (
                <tr key={a.id} className={a.acknowledged ? 'dim' : ''}>
                  <td>{formatTime(a.createdAt)}</td>
                  <td>
                    <Link to={'/patients/' + a.patientId}>{a.patientName}</Link>
                  </td>
                  <td>{a.message}</td>
                  <td>
                    <span className={'status ' + a.severity.toLowerCase()}>{a.severity}</span>
                  </td>
                  <td>{a.doctorName || <span className="muted">-</span>}</td>
                  <td>
                    {a.acknowledged ? (
                      <span className="muted small-text">acknowledged</span>
                    ) : (
                      <button className="btn ghost small" onClick={() => acknowledge(a)}>
                        Acknowledge
                      </button>
                    )}
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
