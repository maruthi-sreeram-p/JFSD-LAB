import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api.js';

// used both for a new course and for editing one
function CourseForm({ initial, submitLabel, onSubmit, onCancel }) {
  const [form, setForm] = useState(initial);
  const [busy, setBusy] = useState(false);

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setBusy(true);
    try {
      await onSubmit(form);
    } finally {
      setBusy(false);
    }
  }

  return (
    <form className="inline-form" onSubmit={handleSubmit}>
      <label>
        Title
        <input name="title" value={form.title} onChange={change} required />
      </label>
      <label>
        Category
        <input name="category" value={form.category} onChange={change} required />
      </label>
      <label>
        Description
        <textarea name="description" rows="3" value={form.description} onChange={change} />
      </label>
      <div className="actions">
        <button className="btn" disabled={busy}>
          {submitLabel}
        </button>
        <button type="button" className="btn ghost" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

// one course the instructor teaches: edit it, manage its content, open the student list
function CourseManager({ course, onChanged, onDeleted, onError }) {
  const [editing, setEditing] = useState(false);
  const [content, setContent] = useState({ type: 'VIDEO', title: '', url: '' });

  async function saveCourse(form) {
    try {
      onChanged(await api('/courses/' + course.id, { method: 'PUT', body: form }));
      setEditing(false);
    } catch (err) {
      onError(err.message);
    }
  }

  async function deleteCourse() {
    if (!window.confirm('Delete "' + course.title + '" and all enrollments in it?')) {
      return;
    }
    try {
      await api('/courses/' + course.id, { method: 'DELETE' });
      onDeleted(course.id);
    } catch (err) {
      onError(err.message);
    }
  }

  async function addContent(e) {
    e.preventDefault();
    try {
      onChanged(await api('/courses/' + course.id + '/contents', { method: 'POST', body: content }));
      setContent({ ...content, title: '', url: '' });
    } catch (err) {
      onError(err.message);
    }
  }

  async function removeContent(item) {
    try {
      onChanged(await api('/courses/' + course.id + '/contents/' + item.id, { method: 'DELETE' }));
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <article className="panel">
      {editing ? (
        <CourseForm
          initial={{ title: course.title, category: course.category, description: course.description || '' }}
          submitLabel="Save course"
          onSubmit={saveCourse}
          onCancel={() => setEditing(false)}
        />
      ) : (
        <div className="row-between">
          <div>
            <span className="tag">{course.category}</span>
            <h3>{course.title}</h3>
            <p className="muted">{course.description}</p>
          </div>
          <div className="actions">
            <Link className="btn ghost small" to={'/instructor/courses/' + course.id + '/students'}>
              Students
            </Link>
            <button className="btn ghost small" onClick={() => setEditing(true)}>
              Edit
            </button>
            <button className="btn danger small" onClick={deleteCourse}>
              Delete
            </button>
          </div>
        </div>
      )}

      <h4>Content</h4>
      {course.contents.length === 0 && <p className="muted small-text">No content yet.</p>}
      <ul className="content-list">
        {course.contents.map((item) => (
          <li key={item.id}>
            <span className="tag">{item.type}</span>
            <span>{item.title}</span>
            {item.url && (
              <a href={item.url} target="_blank" rel="noreferrer">
                link
              </a>
            )}
            <button className="link-btn" onClick={() => removeContent(item)}>
              Remove
            </button>
          </li>
        ))}
      </ul>

      <form className="add-content" onSubmit={addContent}>
        <select value={content.type} onChange={(e) => setContent({ ...content, type: e.target.value })}>
          <option>VIDEO</option>
          <option>PDF</option>
          <option>QUIZ</option>
        </select>
        <input
          placeholder="Title"
          value={content.title}
          onChange={(e) => setContent({ ...content, title: e.target.value })}
          required
        />
        <input
          placeholder="Link (optional)"
          value={content.url}
          onChange={(e) => setContent({ ...content, url: e.target.value })}
        />
        <button className="btn small">Add content</button>
      </form>
    </article>
  );
}

export default function InstructorCourses() {
  const [courses, setCourses] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/courses/mine')
      .then(setCourses)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  async function createCourse(form) {
    setError('');
    try {
      const created = await api('/courses', { method: 'POST', body: form });
      setCourses([created, ...courses]);
      setShowForm(false);
    } catch (err) {
      setError(err.message);
    }
  }

  function replaceCourse(updated) {
    setError('');
    setCourses(courses.map((c) => (c.id === updated.id ? updated : c)));
  }

  function removeCourse(id) {
    setCourses(courses.filter((c) => c.id !== id));
  }

  return (
    <>
      <div className="row-between">
        <h1>My courses</h1>
        {!showForm && (
          <button className="btn" onClick={() => setShowForm(true)}>
            New course
          </button>
        )}
      </div>

      {error && <p className="error">{error}</p>}

      {showForm && (
        <div className="panel">
          <h3>New course</h3>
          <CourseForm
            initial={{ title: '', category: '', description: '' }}
            submitLabel="Create course"
            onSubmit={createCourse}
            onCancel={() => setShowForm(false)}
          />
        </div>
      )}

      {loading && <p className="muted">Loading...</p>}
      {!loading && courses.length === 0 && !showForm && (
        <p className="muted">You are not teaching any course yet. Create the first one.</p>
      )}

      <div className="stack">
        {courses.map((course) => (
          <CourseManager
            key={course.id}
            course={course}
            onChanged={replaceCourse}
            onDeleted={removeCourse}
            onError={setError}
          />
        ))}
      </div>
    </>
  );
}
