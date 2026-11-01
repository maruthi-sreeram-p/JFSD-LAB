import { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';
import { api } from '../api.js';

export default function Navbar() {
  const [openAlerts, setOpenAlerts] = useState(0);

  // the badge shows how many alerts nobody has acknowledged yet
  useEffect(() => {
    let stopped = false;
    function load() {
      api('/alerts?open=true')
        .then((list) => !stopped && setOpenAlerts(list.length))
        .catch(() => {});
    }
    load();
    const timer = setInterval(load, 5000);
    return () => {
      stopped = true;
      clearInterval(timer);
    };
  }, []);

  return (
    <header className="navbar">
      <div className="navbar-inner">
        <span className="brand">SHMS</span>
        <nav className="nav-links">
          <NavLink to="/" end>
            Doctor dashboard
          </NavLink>
          <NavLink to="/patients">Patients</NavLink>
          <NavLink to="/doctors">Doctors</NavLink>
          <NavLink to="/alerts">
            Alerts
            {openAlerts > 0 && <span className="badge">{openAlerts}</span>}
          </NavLink>
        </nav>
      </div>
    </header>
  );
}
