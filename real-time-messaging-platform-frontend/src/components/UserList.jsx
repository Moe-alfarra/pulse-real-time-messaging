import { useEffect, useState } from "react";
import api from "../api/axios";

export default function UserList({ onSelectConversation }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get("/users");
      //console.log("Users:", response.data);
      setUsers(response.data);
    } catch (error) {
      //console.error("Failed to fetch users", error);
    }
  };

  const startConversation = async (userId) => {
    try {
      //console.log("Clicked userId:", userId);

      const response = await api.post("/conversations/direct", null, {
        params: {
          otherUserId: userId,
        },
      });

      //console.log("Conversation response:", response.data);
      onSelectConversation(response.data);
    } catch (error) {
      //console.error("Failed to create conversation", error);
      //console.error("Status:", error?.response?.status);
      //console.error("Response data:", error?.response?.data);
    }
  };

  return (
    <div className="user-list">
      <h3>Users</h3>

      {users.map((user) => (
        <div
          key={user.userId ?? user.id}
          className="user-item"
          onClick={() => startConversation(user.userId ?? user.id)}
        >
          {user.name}
        </div>
      ))}
    </div>
  );
}