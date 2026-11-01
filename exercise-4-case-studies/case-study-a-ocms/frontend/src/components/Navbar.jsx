import { useEffect, useState } from 'react';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../AuthContext.jsx';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [unread, setUnread] = useState(0);

  // refresh the badge on every page change and every 20 seconds
  useEffect(() => {
    let stopped = false;
    function load() {
      api('/notifications/unread-count')
        .then((data) => !stopped && setUnread(data.count))
        .catch(() => {});
    }
    load();
    const timer = setInterval(load, 20000);
    return () => {
      stopped = true;
      clearInterval(timer);
    };
  }, [location.pathname]);

  function handleLogout() {
    logout();
    navigate('/login');
  }

  const isStudent = user.role === 'STUDENT';

  return (
    <header className="navbar">
      <div className="navbar-inner">
        <span className="brand">OCMS</span>

        <nav className="nav-links">
          <NavLink to="/courses">Courses</NavLink>
          {isStudent && <NavLink to="/my-learning">My learning</NavLink>}
          {!isStudent && <NavLink to="/instructor/courses">My courses</NavLink>}
          <NavLink to="/notifications">
            Notifications
            {unread > 0 && <span className="badge">{unread}</span>}
          </NavLink>
          <NavLink to="/profile">Profile</NavLink>
        </nav>

        <div className="nav-user">
          <span className="muted">
            {user.name} ({user.role.toLowerCase()})
          </span>
          <button className="btn ghost small" onClick={handleLogout}>
            Log out
          </button>
        </div>
      </div>
    </header>
  );
}
