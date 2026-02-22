import React, { useEffect, useState } from 'react';
import { api } from '../../api';
import { Link } from 'react-router-dom';
import Loader from '../../components/Loader';
import './AdminDashboard.css';

export default function AdminDashboard({ user }) {

  const [quizzes, setQuizzes] = useState([]);
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const data = await api(`/api/quizzes/mine/${user.id}`);
        setQuizzes(data);
      } catch (e) {
        setErr(e.message);
      } finally {
        setLoading(false);
      }
    })();
  }, [user.id]);

  if (loading) return <Loader />;

  return (
    <div className="admin-dashboard">

      <div className="dashboard-header">
        <div>
          <h1>Admin Dashboard</h1>
          <p>Manage and monitor your quizzes</p>
        </div>
        <Link to="/admin/create" className="create-btn">
          + Create Quiz
        </Link>
      </div>

      {err && <div className="error-box">{err}</div>}

      {quizzes.length === 0 ? (
        <div className="empty-state">
          <h3>No quizzes created yet</h3>
          <p>Start by creating your first quiz 🚀</p>
        </div>
      ) : (
        <div className="quiz-grid">
          {quizzes.map(q => (
            <div key={q.id} className="quiz-card">
              <h3>{q.title}</h3>
              <p><strong>Code:</strong> {q.code}</p>
              <p><strong>Time:</strong> {q.timeLimitMinutes} min</p>
              <p className="validity">
                {q.validFrom?.replace('T', ' ')}  
                <br /> to <br />
                {q.validUntil?.replace('T', ' ')}
              </p>
            </div>
          ))}
        </div>
      )}

    </div>
  );
}