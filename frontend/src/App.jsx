import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate, useLocation } from 'react-router-dom';

import NavBar from './components/NavBar.jsx';
import Home from './pages/Home.jsx';
import Signup from './pages/Signup.jsx';
import Login from './pages/Login.jsx';

import AdminDashboard from './pages/admin/AdminDashboard.jsx';
import CreateQuiz from './pages/admin/CreateQuiz.jsx';
import Results from './pages/admin/Results.jsx';

import AttendQuiz from './pages/student/AttendQuiz.jsx';
import History from './pages/student/History.jsx';

export default function App() {

  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const navigate = useNavigate();
  const location = useLocation();

  // Save user to localStorage
  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    }
  }, [user]);

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    navigate('/');
  };

  // Hide navbar on home + auth pages
  const hideNavbar =
    location.pathname === '/' ||
    location.pathname === '/login' ||
    location.pathname === '/signup';

  return (
    <>
      {/* Show navbar only when logged in and not on auth pages */}
      {user && !hideNavbar && (
        <NavBar user={user} onLogout={logout} />
      )}

      <Routes>

        {/* Home */}
        <Route path="/" element={<Home />} />

        {/* Auth */}
        <Route
          path="/signup"
          element={
            user
              ? <Navigate to={user.role === 'ADMIN' ? '/admin' : '/attend'} />
              : <Signup onDone={() => navigate('/login')} />
          }
        />

        <Route
          path="/login"
          element={
            user
              ? <Navigate to={user.role === 'ADMIN' ? '/admin' : '/attend'} />
              : <Login onLogin={setUser} />
          }
        />

        {/* Admin */}
        <Route
          path="/admin"
          element={
            user?.role === 'ADMIN'
              ? <AdminDashboard user={user} />
              : <Navigate to="/" />
          }
        />

        <Route
          path="/admin/create"
          element={
            user?.role === 'ADMIN'
              ? <CreateQuiz user={user} />
              : <Navigate to="/" />
          }
        />

        <Route
          path="/admin/results"
          element={
            user?.role === 'ADMIN'
              ? <Results user={user} />
              : <Navigate to="/" />
          }
        />

        {/* Student */}
        <Route
          path="/attend"
          element={
            user?.role === 'STUDENT'
              ? <AttendQuiz user={user} />
              : <Navigate to="/" />
          }
        />

        <Route
          path="/history"
          element={
            user?.role === 'STUDENT'
              ? <History user={user} />
              : <Navigate to="/" />
          }
        />

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" />} />

      </Routes>
    </>
  );
}