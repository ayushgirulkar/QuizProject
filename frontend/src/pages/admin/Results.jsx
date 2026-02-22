import React, { useEffect, useState } from 'react';
import { api } from '../../api';
import Loader from '../../components/Loader';
import './Results.css';

export default function Results({ user }) {

  const [quizzes, setQuizzes] = useState([]);
  const [selected, setSelected] = useState('');
  const [rows, setRows] = useState([]);
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const data = await api(`/api/quizzes/mine/${user.id}`);
        setQuizzes(data);
      } catch (e) {
        setErr(e.message);
      }
    })();
  }, [user.id]);

  const load = async (id) => {
    setSelected(id);
    if (!id) return;

    setLoading(true);
    setErr('');
    try {
      const data = await api(`/api/submissions/results/${id}`);
      setRows(data);
    } catch (e) {
      setErr(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="results-page">

      <div className="results-header">
        <h1>Quiz Results</h1>
        <p>Monitor student performance</p>
      </div>

      {err && <div className="error-box">{err}</div>}

      <div className="select-box">
        <select value={selected} onChange={e => load(e.target.value)}>
          <option value="">Select Quiz</option>
          {quizzes.map(q => (
            <option key={q.id} value={q.id}>
              {q.title} ({q.code})
            </option>
          ))}
        </select>
      </div>

      {loading && <Loader />}

      {!loading && selected && rows.length === 0 && (
        <div className="empty-state">
          No submissions yet for this quiz.
        </div>
      )}

      <div className="results-grid">
        {rows.map((r, i) => (
          <div key={i} className="result-card">
            <h3>{r.studentName}</h3>
            <p><strong>Score:</strong> {r.score}</p>
            <p><strong>Time Taken:</strong> {r.timeTakenSeconds}s</p>
            <p className="submitted">
              {new Date(r.submittedAt).toLocaleString()}
            </p>
          </div>
        ))}
      </div>

    </div>
  );
}