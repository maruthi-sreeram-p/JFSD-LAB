import { useEffect, useMemo, useState } from 'react';
import { api } from '../api.js';
import { useAuth } from '../AuthContext.jsx';

// "2 videos, 1 pdf" style summary of a course's content
function contentSummary(contents) {
  if (contents.length === 0) {
    return 'No content yet';
  }
  const counts = {};
  contents.forEach((c) => {
    counts[c.type] = (counts[c.type] || 0) + 1;
  });
  return Object.entries(counts)
    .map(([type, n]) => n + ' ' + type.toLowerCase() + (n > 1 ? 's' : ''))
    .join(', ');
}

export default function Courses() {
  const { user } = useAuth();
  const isStudent = user.role === 'STUDENT';

  const [courses, setCourses] = useState([]);
  const [enrolledIds, setEnrolledIds] = useState(new Set());
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        setCourses(await api('/courses'));
        if (isStudent) {
          const enrollments = await api('/enrollments/mine');
          setEnrolledIds(new Set(enrollments.map((e) => e.courseId)));
        }
      } catch (err) {
        setMessage({ type: 'error', text: err.message });
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [isStudent]);

  const categories = useMemo(() => [...new Set(courses.map((c) => c.category))].sort(), [courses]);

  const visible = courses.filter((c) => {
    const text = (c.title + ' ' + c.description + ' ' + c.instructorName).toLowerCase();
    return text.includes(search.toLowerCase()) && (category === '' || c.category === category);
  });

  async function enroll(course) {
    setMessage({ type: '', text: '' });
    try {
      await api('/enrollments', { method: 'POST', body: { courseId: course.id } });
      setEnrolledIds(new Set([...enrolledIds, course.id]));
      setMessage({ type: 'ok', text: 'You are enrolled in "' + course.title + '". Find it under My learning.' });
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    }
  }

  return (
    <>
      <h1>All courses</h1>

      <div className="toolbar">
        <input
          placeholder="Search by title, description or instructor"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          <option value="">All categories</option>
          {categories.map((c) => (
            <option key={c}>{c}</option>
          ))}
        </select>
      </div>

      {message.text && <p className={message.type === 'error' ? 'error' : 'success'}>{message.text}</p>}

      {loading && <p className="muted">Loading...</p>}
      {!loading && visible.length === 0 && <p className="muted">No courses found.</p>}

      <div className="grid">
        {visible.map((course) => (
          <article key={course.id} className="panel course-card">
            <span className="tag">{course.category}</span>
            <h3>{course.title}</h3>
            <p className="muted">by {course.instructorName}</p>
            <p>{course.description}</p>
            <p className="muted small-text">{contentSummary(course.contents)}</p>

            {isStudent &&
              (enrolledIds.has(course.id) ? (
                <button className="btn ghost" disabled>
                  Enrolled
                </button>
              ) : (
                <button className="btn" onClick={() => enroll(course)}>
                  Enroll
                </button>
              ))}
            {!isStudent && course.instructorId === user.id && <span className="tag own">Your course</span>}
          </article>
        ))}
      </div>
    </>
  );
}
