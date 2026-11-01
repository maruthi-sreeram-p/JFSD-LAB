import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { api } from '../api.js';

export default function CourseStudents() {
  const { id } = useParams();
  const [course, setCourse] = useState(null);
  const [students, setStudents] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([api('/courses/' + id), api('/courses/' + id + '/students')])
      .then(([c, s]) => {
        setCourse(c);
        setStudents(s);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id]);

  const completed = students.filter((s) => s.status === 'COMPLETED').length;
  const average = students.length === 0 ? 0 : Math.round(students.reduce((sum, s) => sum + s.progress, 0) / students.length);

  return (
    <>
      <p>
        <Link to="/instructor/courses">&larr; My courses</Link>
      </p>
      <h1>{course ? course.title : 'Students'}</h1>

      {error && <p className="error">{error}</p>}
      {loading && <p className="muted">Loading...</p>}

      {!loading && !error && (
        <>
          <div className="stats">
            <div className="panel stat">
              <b>{students.length}</b>
              <span className="muted">enrolled</span>
            </div>
            <div className="panel stat">
              <b>{completed}</b>
              <span className="muted">completed</span>
            </div>
            <div className="panel stat">
              <b>{average}%</b>
              <span className="muted">average progress</span>
            </div>
          </div>

          {students.length === 0 ? (
            <p className="muted">No student has enrolled yet.</p>
          ) : (
            <div className="panel table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Student</th>
                    <th>Contact</th>
                    <th>Enrolled on</th>
                    <th>Status</th>
                    <th>Progress</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((s) => (
                    <tr key={s.enrollmentId}>
                      <td>
                        {s.name}
                        <br />
                        <span className="muted small-text">{s.email}</span>
                      </td>
                      <td>{s.contact || '-'}</td>
                      <td>{s.enrolledOn}</td>
                      <td>
                        <span className={'status ' + s.status.toLowerCase()}>
                          {s.status === 'COMPLETED' ? 'Completed' : 'Ongoing'}
                        </span>
                      </td>
                      <td className="progress-cell">
                        <div className="progress">
                          <div className="progress-bar" style={{ width: s.progress + '%' }} />
                        </div>
                        <span className="muted small-text">
                          {s.completed}/{s.total} ({s.progress}%)
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </>
      )}
    </>
  );
}
