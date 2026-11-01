import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api.js';

export default function MyLearning() {
  const [enrollments, setEnrollments] = useState([]);
  const [filter, setFilter] = useState('ALL');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/enrollments/mine')
      .then(setEnrollments)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  // tick or untick one content item; the server answers with the updated enrollment
  async function toggle(enrollment, item) {
    setError('');
    try {
      const path = '/enrollments/' + enrollment.id + '/contents/' + item.id + '/complete';
      const updated = await api(path, { method: item.done ? 'DELETE' : 'POST' });
      setEnrollments(enrollments.map((e) => (e.id === updated.id ? updated : e)));
    } catch (err) {
      setError(err.message);
    }
  }

  const completedCount = enrollments.filter((e) => e.status === 'COMPLETED').length;
  const visible = enrollments.filter((e) => filter === 'ALL' || e.status === filter);

  return (
    <>
      <h1>My learning</h1>

      <div className="tabs">
        <button className={filter === 'ALL' ? 'active' : ''} onClick={() => setFilter('ALL')}>
          All ({enrollments.length})
        </button>
        <button className={filter === 'ONGOING' ? 'active' : ''} onClick={() => setFilter('ONGOING')}>
          Ongoing ({enrollments.length - completedCount})
        </button>
        <button className={filter === 'COMPLETED' ? 'active' : ''} onClick={() => setFilter('COMPLETED')}>
          Completed ({completedCount})
        </button>
      </div>

      {error && <p className="error">{error}</p>}
      {loading && <p className="muted">Loading...</p>}
      {!loading && enrollments.length === 0 && (
        <p className="muted">
          You have not enrolled in any course yet. <Link to="/courses">Browse the courses</Link>.
        </p>
      )}
      {!loading && enrollments.length > 0 && visible.length === 0 && <p className="muted">Nothing here yet.</p>}

      <div className="stack">
        {visible.map((e) => (
          <article key={e.id} className="panel">
            <div className="row-between">
              <div>
                <h3>{e.courseTitle}</h3>
                <p className="muted">
                  {e.category} - {e.instructorName} - enrolled on {e.enrolledOn}
                </p>
              </div>
              <span className={'status ' + e.status.toLowerCase()}>{e.status === 'COMPLETED' ? 'Completed' : 'Ongoing'}</span>
            </div>

            <div className="progress-row">
              <div className="progress">
                <div className="progress-bar" style={{ width: e.progress + '%' }} />
              </div>
              <span className="muted small-text">
                {e.completed}/{e.total} done - {e.progress}%
              </span>
            </div>

            {e.contents.length === 0 ? (
              <p className="muted small-text">The instructor has not added any content yet.</p>
            ) : (
              <ul className="checklist">
                {e.contents.map((item) => (
                  <li key={item.id}>
                    <label>
                      <input type="checkbox" checked={item.done} onChange={() => toggle(e, item)} />
                      <span className="tag">{item.type}</span>
                      <span className={item.done ? 'done' : ''}>{item.title}</span>
                    </label>
                    {item.url && (
                      <a href={item.url} target="_blank" rel="noreferrer">
                        Open
                      </a>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </article>
        ))}
      </div>
    </>
  );
}
