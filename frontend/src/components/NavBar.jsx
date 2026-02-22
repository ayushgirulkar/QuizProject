import React from 'react';
import { Link } from 'react-router-dom';
import './NavBar.css';

export default function NavBar({ user, onLogout }) {

  // If no user, don't render navbar
  if (!user) return null;

  return (
    <nav className="navbar">

      <div className="navbar-inner">

        {/* LEFT */}
        <div className="nav-left">
          <Link
            to={user.role === "ADMIN" ? "/admin" : "/attend"}
            className="logo"
          >
            QuizAI 📝
          </Link>
        </div>

        {/* CENTER */}
        <div className="nav-center">
          {user.role === 'ADMIN' && (
            <>
              <Link to="/admin">Dashboard</Link>
              <Link to="/admin/create">Create Quiz</Link>
              <Link to="/admin/results">Results</Link>
            </>
          )}

          {user.role === 'STUDENT' && (
            <>
              <Link to="/attend">Attend Quiz</Link>
              <Link to="/history">History</Link>
            </>
          )}
        </div>

        {/* RIGHT */}
        <div className="nav-right">
          <span className="welcome">Hi, {user.name}</span>
          <button onClick={onLogout} className="logout-btn">
            Logout
          </button>
        </div>

      </div>

    </nav>
  );
}