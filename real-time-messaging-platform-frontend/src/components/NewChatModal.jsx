import { useEffect, useState } from "react";
import { X, Search, MessageSquarePlus } from "lucide-react";
import api from "../api/axios";

export default function NewChatModal({ onClose, onConversationCreated }) {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get("/users");
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch users", error);
    }
  };

  const startConversation = async (userId) => {
    try {
      const response = await api.post(
        `/conversations/direct?otherUserId=${userId}`
      );
      onConversationCreated(response.data);
    } catch (error) {
      console.error("Failed to create conversation", error);
    }
  };

  const filteredUsers = users.filter((user) =>
    `${user.name} ${user.email}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-card modern-modal-card" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div>
            <h3>Start New Chat</h3>
            <p>Find someone and begin a real-time conversation.</p>
          </div>

          <button className="modal-icon-btn" onClick={onClose} type="button">
            <X size={18} />
          </button>
        </div>

        <div className="search-box modern-search-box">
          <Search size={18} />
          <input
            type="text"
            placeholder="Search by name or email..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="modal-user-list">
          {filteredUsers.length === 0 ? (
            <div className="modal-empty-state">
              <MessageSquarePlus size={28} />
              <h4>No users found</h4>
              <p>Try searching with a different name or email.</p>
            </div>
          ) : (
            filteredUsers.map((user) => (
              <button
                key={user.userId ?? user.id}
                className="modal-user-item"
                onClick={() => startConversation(user.userId ?? user.id)}
              >
                <div className="conversation-avatar">
                  {user.name?.charAt(0)?.toUpperCase()}
                </div>

                <div className="conversation-text">
                  <h4>{user.name}</h4>
                  <p>{user.email}</p>
                </div>
              </button>
            ))
          )}
        </div>
      </div>
    </div>
  );
}