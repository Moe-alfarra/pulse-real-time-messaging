import { useEffect, useState } from "react";
import { Lock, LogOut, User, X } from "lucide-react";
import { updateUserName, updateUserPassword } from "../api/userApi";
import { useAuth } from "../context/AuthContext";

export default function ProfileModal({ user, onClose, onLogout }) {
  const { updateUser } = useAuth();

  const [profileName, setProfileName] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const [profileMessage, setProfileMessage] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");
  const [profileError, setProfileError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const [savingProfile, setSavingProfile] = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);

  useEffect(() => {
    if (user) {
      setProfileName(user.name || "");
    }
  }, [user]);

  const handleProfileSave = async (e) => {
    e.preventDefault();
    setProfileError("");
    setProfileMessage("");
    setSavingProfile(true);

    try {
      await updateUserName(profileName);

      const updatedUser = {
        ...user,
        name: profileName.trim(),
      };

      updateUser(updatedUser);
      setProfileMessage("Name updated successfully.");
    } catch (error) {
      setProfileError(
        error?.response?.data?.message ||
          error?.response?.data ||
          "Failed to update name."
      );
    } finally {
      setSavingProfile(false);
    }
  };

  const handlePasswordSave = async (e) => {
    e.preventDefault();
    setPasswordError("");
    setPasswordMessage("");
    setSavingPassword(true);

    try {
      await updateUserPassword(currentPassword, newPassword);
      setPasswordMessage("Password updated successfully.");
      setCurrentPassword("");
      setNewPassword("");
    } catch (error) {
      setPasswordError(
        error?.response?.data?.message ||
          error?.response?.data ||
          "Failed to update password."
      );
    } finally {
      setSavingPassword(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="modal-card profile-modal-card modern-modal-card"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <div>
            <h3>Profile Settings</h3>
            <p>Manage your account information and security.</p>
          </div>

          <button className="modal-icon-btn" onClick={onClose} type="button">
            <X size={18} />
          </button>
        </div>

        <div className="profile-modal-top">
          <div className="avatar-circle profile-modal-avatar">
            {user?.name?.charAt(0)?.toUpperCase() || "U"}
          </div>

          <div>
            <h4>{user?.name || "User"}</h4>
            <p>{user?.email || "No email available"}</p>
          </div>
        </div>

        <form className="profile-form-section" onSubmit={handleProfileSave}>
          <div className="profile-section-title">
            <User size={16} />
            <span>Profile Information</span>
          </div>

          <div className="auth-field">
            <label>Name</label>
            <input
              type="text"
              value={profileName}
              onChange={(e) => setProfileName(e.target.value)}
              placeholder="Enter your name"
            />
          </div>

          <div className="auth-field">
            <label>Email</label>
            <input type="email" value={user?.email || ""} disabled />
          </div>

          {profileError && <p className="auth-error">{profileError}</p>}
          {profileMessage && <p className="auth-success">{profileMessage}</p>}

          <button
            type="submit"
            className="auth-submit-btn"
            disabled={savingProfile}
          >
            {savingProfile ? "Saving..." : "Save Name"}
          </button>
        </form>

        <form className="profile-form-section" onSubmit={handlePasswordSave}>
          <div className="profile-section-title">
            <Lock size={16} />
            <span>Change Password</span>
          </div>

          <div className="auth-field">
            <label>Current Password</label>
            <input
              type="password"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              placeholder="Enter current password"
            />
          </div>

          <div className="auth-field">
            <label>New Password</label>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Enter new password"
            />
          </div>

          {passwordError && <p className="auth-error">{passwordError}</p>}
          {passwordMessage && <p className="auth-success">{passwordMessage}</p>}

          <button
            type="submit"
            className="auth-submit-btn"
            disabled={savingPassword}
          >
            {savingPassword ? "Updating..." : "Update Password"}
          </button>
        </form>

        <div className="profile-actions">
          <button className="logout-profile-btn" onClick={onLogout} type="button">
            <LogOut size={16} />
            <span>Logout</span>
          </button>
        </div>
      </div>
    </div>
  );
}