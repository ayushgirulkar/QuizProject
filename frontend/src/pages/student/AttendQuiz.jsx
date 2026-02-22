import React, { useEffect, useState, useRef } from 'react';
import { api } from '../../api';
import Loader from '../../components/Loader';
import './AttendQuiz.css';

export default function AttendQuiz({ user }) {

  const [code, setCode] = useState('');
  const [quiz, setQuiz] = useState(null);
  const [answers, setAnswers] = useState({});
  const [error, setError] = useState('');
  const [secondsLeft, setSecondsLeft] = useState(0);
  const [submitted, setSubmitted] = useState(null);
  const [loading, setLoading] = useState(false);

  const startRef = useRef(null);
  const timerRef = useRef(null);

  const search = async () => {
    setError('');
    setSubmitted(null);
    setLoading(true);

    try {
      const q = await api(`/api/quizzes/by-code/${code.trim().toUpperCase()}`);
      setQuiz(q);
      setAnswers({});
      startRef.current = new Date();
      setSecondsLeft(q.timeLimitMinutes * 60);
    } catch (e) {
      setError(e.message || 'Quiz not found');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!quiz) return;

    timerRef.current = setInterval(() => {
      setSecondsLeft(s => {
        if (s <= 1) {
          clearInterval(timerRef.current);
          doSubmit(true);
          return 0;
        }
        return s - 1;
      });
    }, 1000);

    return () => clearInterval(timerRef.current);
  }, [quiz]);

  const choose = (qid, oid) =>
    setAnswers(a => ({ ...a, [qid]: oid }));

  const doSubmit = async (auto = false) => {
    if (!quiz) return;

    try {
      const payload = {
        quizId: quiz.id,
        userId: user.id,
        startedAt: startRef.current.toISOString(),
        answers: Object.entries(answers).map(([questionId, optionId]) => ({
          questionId: Number(questionId),
          optionId
        }))
      };

      const res = await api('/api/submissions/submit', 'POST', payload);
      setSubmitted(res);
      setQuiz(null);
      setError('');
    } catch (e) {
      setError(e.message || 'Submission failed');
      setQuiz(null);
    }
  };

  const progress = quiz
    ? (Object.keys(answers).length / quiz.questions.length) * 100
    : 0;

  return (
    <div className="attend-page">

      {loading && <Loader />}

      <h1>Attend Quiz</h1>

      {error && <div className="error-box">{error}</div>}

      {!quiz && !submitted && (
        <div className="search-card">
          <input
            placeholder="Enter Quiz Code"
            value={code}
            onChange={e => setCode(e.target.value)}
          />
          <button onClick={search}>Start</button>
        </div>
      )}

      {quiz && (
        <div className="quiz-card">

          <div className="quiz-header">
            <div>
              <h2>{quiz.title}</h2>
              <p>{quiz.adminName}</p>
            </div>

            <div className={`timer ${secondsLeft < 60 ? 'danger' : ''}`}>
              ⏰ {Math.floor(secondsLeft / 60)}:
              {String(secondsLeft % 60).padStart(2, '0')}
            </div>
          </div>

          {/* Progress Bar */}
          <div className="progress-bar">
            <div style={{ width: `${progress}%` }}></div>
          </div>

          <div className="questions">
            {quiz.questions.map((q, index) => (
              <div key={q.id} className="question-card">
                <h4>Q{index + 1}. {q.text}</h4>

                {q.options.map(o => (
                  <label
                    key={o.id}
                    className={`option ${answers[q.id] === o.id ? 'selected' : ''}`}
                  >
                    <input
                      type="radio"
                      name={`q${q.id}`}
                      onChange={() => choose(q.id, o.id)}
                      checked={answers[q.id] === o.id}
                    />
                    {o.text}
                  </label>
                ))}
              </div>
            ))}
          </div>

          <button className="submit-btn" onClick={() => doSubmit(false)}>
            Submit Quiz
          </button>

        </div>
      )}

      {submitted && (
        <div className="result-card">
          <h2>🎉 Quiz Completed</h2>
          <p>Score: {submitted.score} / {submitted.total}</p>
          <p>Time Taken: {submitted.timeTakenSeconds}s</p>
        </div>
      )}

    </div>
  );
}