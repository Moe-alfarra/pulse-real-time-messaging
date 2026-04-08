import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { MessageCircle, ArrowRight } from "lucide-react";
import { loginRequest } from "../api/authApi";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const data = await loginRequest(formData);

      login({
        token: data.token,
        user: {
          userId: data.userId,
          name: data.name,
          email: data.email,
        },
      });

      navigate("/chat");
    } catch (err) {
      setError(err?.response?.data?.message || "Invalid email or password.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-shell">
        <div className="auth-hero">
          <div className="auth-badge">Real-time messaging platform</div>
          <h1>Pulse</h1>
          <p>
            A modern messaging experience with live conversations, unread
            counters, and read receipts.
          </p>
        </div>

        <div className="auth-card modern-auth-card">
          <div className="auth-brand">
            <div className="auth-logo">
              <MessageCircle size={24} />
            </div>
            <h2>Welcome back</h2>
            <p>Sign in to continue your conversations.</p>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="auth-field">
              <label>Email</label>
              <input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="auth-field">
              <label>Password</label>
              <input
                type="password"
                name="password"
                placeholder="Enter your password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            {error && <p className="auth-error">{error}</p>}

            <button type="submit" className="auth-submit-btn" disabled={loading}>
              <span>{loading ? "Logging in..." : "Login"}</span>
              {!loading && <ArrowRight size={18} />}
            </button>
          </form>

          <p className="auth-footer-text">
            Don&apos;t have an account? <Link to="/register">Create one</Link>
          </p>
        </div>
      </div>
    </div>
  );
}