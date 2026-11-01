import { useEffect, useState } from 'react';
import { api } from '../api.js';

function formatTime(iso) {
  return new Date(iso).toLocaleString([], { dateStyle: 'medium', timeStyle: 'short' });
}

export default function Notifications() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/notifications')
      .then(setItems)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  async function markRead(item) {
    if (item.read) {
      return;
    }
    try {
      await api('/notifications/' + item.id + '/read', { method: 'POST' });
      setItems(items.map((n) => (n.id === item.id ? { ...n, read: true } : n)));
    } catch (err) {
      setError(err.message);
    }
  }

  async function markAllRead() {
    try {
      await api('/notifications/read-all', { method: 'POST' });
      setItems(items.map((n) => ({ ...n, read: true })));
    } catch (err) {
      setError(err.message);
    }
  }

  const unread = items.filter((n) => !n.read).length;

  return (
    <>
      <div className="row-between">
        <h1>Notifications</h1>
        {unread > 0 && (
          <button className="btn ghost small" onClick={markAllRead}>
            Mark all as read
          </button>
        )}
      </div>

      {error && <p className="error">{error}</p>}
      {loading && <p className="muted">Loading...</p>}
      {!loading && items.length === 0 && <p className="muted">No notifications yet.</p>}

      <div className="stack">
        {items.map((n) => (
          <div
            key={n.id}
            className={'panel notification' + (n.read ? '' : ' unread')}
            onClick={() => markRead(n)}
            title={n.read ? '' : 'Click to mark as read'}
          >
            <p>{n.message}</p>
            <span className="muted small-text">{formatTime(n.createdAt)}</span>
          </div>
        ))}
      </div>
    </>
  );
}
