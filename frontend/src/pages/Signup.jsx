import React, { useState } from 'react';
import { api } from '../api';
import { useNavigate } from 'react-router-dom';
import Loader from '../components/Loader';
import './Signup.css';

export default function Signup({ onDone }) {

  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    role: 'STUDENT'
  });

  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');

    try {
      await api('/api/auth/signup','POST',form);
      onDone ? onDone() : navigate('/login');
    } catch (e) {
      setErr(e.message || 'Signup failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="signup-page">

      {loading && <Loader />}

      <div className="signup-card">
        <h2>Create Account 🚀</h2>
        <p>Join QuizAI and start learning smarter</p>

        {err && <div className="error-box">{err}</div>}

        <form onSubmit={submit}>

          <div className="input-group">
            <label>Full Name</label>
            <input
              value={form.name}
              onChange={e=>setForm({...form,name:e.target.value})}
              required
            />
          </div>

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

          <div className="input-group">
            <label>Role</label>
            <select
              value={form.role}
              onChange={e=>setForm({...form, role:e.target.value})}
            >
              <option value="STUDENT">Student</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          <button type="submit" className="signup-btn">
            {loading ? 'Creating Account...' : 'Create Account'}
          </button>

        </form>
      </div>

    </div>
  );
}