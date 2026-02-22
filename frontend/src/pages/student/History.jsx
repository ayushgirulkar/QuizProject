import React, { useEffect, useState } from 'react';
import { api } from '../../api';
import Loader from '../../components/Loader';
import './History.css';

export default function History({ user }) {

  const [rows, setRows] = useState([]);
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const data = await api(`/api/submissions/history/${user.id}`);
        setRows(data);
      } catch (e) {
        setErr(e.message);
      } finally {
        setLoading(false);
      }
    })();
  }, [user.id]);

  const averageScore = rows.length
    ? (rows.reduce((acc, r) => acc + r.score, 0) / rows.length).toFixed(1)
    : 0;

  return (
    <div className="history-page">

      <div className="history-header">
        <h1>My Quiz History</h1>
        <p>Track your performance and progress</p>
      </div>

      {loading && <Loader />}

      {err && <div className="error-box">{err}</div>}

      {!loading && rows.length > 0 && (
        <>
          {/* Stats Section */}
          <div className="stats-grid">
            <div className="stat-card">
              <h3>{rows.length}</h3>
              <p>Total Attempts</p>
            </div>
            <div className="stat-card">
              <h3>{averageScore}</h3>
              <p>Average Score</p>
            </div>
          </div>

          {/* History Grid */}
          <div className="history-grid">
            {rows.map((r, i) => (
              <div key={i} className="history-card">
                <h3>{r.quizTitle}</h3>
                <p><strong>Score:</strong> {r.score}</p>
                <p><strong>Time:</strong> {r.timeTakenSeconds}s</p>
                <p className="submitted">
                  {new Date(r.submittedAt).toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        </>
      )}

      {!loading && rows.length === 0 && (
        <div className="empty-state">
          No quizzes attempted yet.
        </div>
      )}

    </div>
  );
}