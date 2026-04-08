import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { MessageCircle, ArrowRight } from "lucide-react";
import { registerRequest } from "../api/authApi";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
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
      await registerRequest(formData);
      navigate("/login");
    } catch (err) {
      setError(err?.response?.data?.message || "Registration failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-shell">
        <div className="auth-hero">
          <div className="auth-badge">Modern chat, built for speed</div>
          <h1>Pulse</h1>
          <p>
            Create your account and start chatting instantly with a sleek
            real-time messaging experience.
          </p>
        </div>

        <div className="auth-card modern-auth-card">
          <div className="auth-brand">
            <div className="auth-logo">
              <MessageCircle size={24} />
            </div>
            <h2>Create account</h2>
            <p>Join Pulse and start real-time conversations.</p>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="auth-field">
              <label>Full name</label>
              <input
                type="text"
                name="name"
                placeholder="Enter your full name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>

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
                placeholder="Create a password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            {error && <p className="auth-error">{error}</p>}

            <button type="submit" className="auth-submit-btn" disabled={loading}>
              <span>{loading ? "Creating..." : "Create account"}</span>
              {!loading && <ArrowRight size={18} />}
            </button>
          </form>

          <p className="auth-footer-text">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </div>
      </div>
    </div>
  );
}