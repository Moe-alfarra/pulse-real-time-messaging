import { Settings } from "lucide-react";

export default function ProfileMenu({ user, onOpenProfile }) {
  return (
    <button className="profile-trigger modern-profile-trigger" onClick={onOpenProfile}>
      <div className="profile-trigger-info">
        <span className="profile-trigger-name">{user?.name || "User"}</span>
        <span className="profile-trigger-email">{user?.email || "No email"}</span>
      </div>

      <div className="avatar-circle profile-avatar">
        {user?.name?.charAt(0)?.toUpperCase() || "U"}
      </div>

      <div className="profile-settings-icon">
        <Settings size={16} />
      </div>
    </button>
  );
}