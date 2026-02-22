import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";

/* ✅ Import local images */
import img1 from "../assets/img1.jpg";
import img2 from "../assets/img2.jpg";
import img3 from "../assets/img3.jpg";

export default function Home() {
  const navigate = useNavigate();

  /* ✅ Use imported images */
  const images = [img1, img2, img3];

  const [index, setIndex] = useState(0);

  useEffect(() => {
    const slider = setInterval(() => {
      setIndex((prev) => (prev + 1) % images.length);
    }, 4000);

    return () => clearInterval(slider);
  }, [images.length]);

  return (
    <div className="home">

      {/* HERO SECTION */}
      <section
        className="hero"
        style={{ backgroundImage: `url(${images[index]})` }}
      >
        <div className="overlay">
          <h1>Welcome to QuizAI</h1>
          <p>Learn Smarter. Test Faster. Improve Continuously.</p>

          <div className="hero-buttons">
            <button onClick={() => navigate("/login")}>
              Login
            </button>

            <button
              className="outline"
              onClick={() => navigate("/signup")}
            >
              Get Started
            </button>
          </div>
        </div>
      </section>

      {/* FEATURES SECTION */}
      <section className="features">
        <h2>Why Choose QuizAI?</h2>

        <div className="feature-grid">
          <div className="feature-card">
            <div className="icon">🧠</div>
            <h3>AI Powered Generation</h3>
            <p>Create quizzes instantly from notes or study materials.</p>
          </div>

          <div className="feature-card">
            <div className="icon">📊</div>
            <h3>Performance Tracking</h3>
            <p>Analyze your improvement with detailed insights.</p>
          </div>

          <div className="feature-card">
            <div className="icon">⚡</div>
            <h3>Instant Results</h3>
            <p>Real-time scoring and quick feedback system.</p>
          </div>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="footer">
        © {new Date().getFullYear()} QuizAI — Build By Ayush Girulkar 👦🏻
      </footer>

    </div>
  );
}