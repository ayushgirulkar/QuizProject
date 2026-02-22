import React, { useState } from 'react';
import { api } from '../api';
import { useNavigate } from 'react-router-dom';
import Loader from '../components/Loader';
import './Login.css';

export default function Login({ onLogin }) {

  const [form, setForm] = useState({ email:'', password:'' });
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');

    try {
      const user = await api('/api/auth/login','POST',form);
      onLogin(user);
      nav(user.role==='ADMIN'?'/admin':'/attend');
    } catch (e) {
      setErr(e.message || 'Invalid credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">

      {loading && <Loader />}

      <div className="login-card">
        <h2>Welcome Back 👋</h2>
        <p>Login to continue to QuizAI</p>

        {err && <div className="error-box">{err}</div>}

        <form onSubmit={submit}>

          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              value={form.email}
              onChange={e=>setForm({...form,email:e.target.value})}
              required
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              value={form.password}
              onChange={e=>setForm({...form,password:e.target.value})}
              required
            />
          </div>

          <button type="submit" className="login-btn">
            {loading ? 'Logging in...' : 'Login'}
          </button>

        </form>
      </div>

    </div>
  );
}