import React, { useState } from 'react';
import { api } from '../../api';
import Loader from '../../components/Loader';
import './CreateQuiz.css';

export default function CreateQuiz({ user }) {

  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [num, setNum] = useState(5);
  const [time, setTime] = useState(10);
  const [validFrom, setValidFrom] = useState('');
  const [validUntil, setValidUntil] = useState('');
  const [result, setResult] = useState(null);
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');
    setResult(null);

    try {
      const payload = {
        adminId: user.id,
        title,
        pasteText: text,
        numQuestions: Number(num),
        timeLimitMinutes: Number(time),
        validFrom,
        validUntil
      };

      const r = await api('/api/quizzes/generate', 'POST', payload);
      setResult(r);

    } catch (e) {
      setErr(e?.response?.data?.error || e.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-page">

      {loading && <Loader />}

      <div className="create-header">
        <h1>Create New Quiz</h1>
        <p>Generate AI-powered quizzes instantly</p>
      </div>

      {err && <div className="error-box">{err}</div>}

      <form onSubmit={submit} className="create-form">

        <div className="form-grid">

          <div className="input-group">
            <label>Quiz Title</label>
            <input
              value={title}
              onChange={e => setTitle(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label>No. of Questions</label>
            <input
              type="number"
              min="1"
              max="50"
              value={num}
              onChange={e => setNum(e.target.value)}
            />
          </div>

          <div className="input-group">
            <label>Time Limit (minutes)</label>
            <input
              type="number"
              min="1"
              max="180"
              value={time}
              onChange={e => setTime(e.target.value)}
            />
          </div>

          <div className="input-group">
            <label>Valid From</label>
            <input
              type="datetime-local"
              value={validFrom}
              onChange={e => setValidFrom(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label>Valid Until</label>
            <input
              type="datetime-local"
              value={validUntil}
              onChange={e => setValidUntil(e.target.value)}
              required
            />
          </div>

        </div>

        <div className="input-group full">
          <label>Source Text</label>
          <textarea
            rows="10"
            value={text}
            onChange={e => setText(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="generate-btn" disabled={loading}>
          {loading ? 'Generating Quiz...' : 'Generate & Publish'}
        </button>

      </form>

      {/* ================= RESULT SECTION ================= */}

      {result && (
        <div className="result-card">

          <h2>
            🎉 Published: {result.title || title}
            {result.code && <span className="pill">Code: {result.code}</span>}
          </h2>

          <p>
            Time: {result.timeLimitMinutes ?? time} min
          </p>

          <div className="preview-section">
            {Array.isArray(result.questions) && result.questions.map((q, idx) => (
              <div key={idx} className="question-card">
                <h4>Q{idx + 1}. {q.text}</h4>
                <ul>
                  {q.options.map(opt => (
                    <li
                      key={opt.id}
                      className={opt.id === q.correctOptionId ? 'correct' : ''}
                    >
                      {opt.text}
                      {opt.id === q.correctOptionId && ' ✔'}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

        </div>
      )}

    </div>
  );
}