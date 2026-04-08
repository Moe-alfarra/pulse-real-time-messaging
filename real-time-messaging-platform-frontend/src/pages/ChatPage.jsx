import { useEffect, useState } from "react";
import { MessageSquarePlus } from "lucide-react";

import { useAuth } from "../context/AuthContext";
import { getCurrentUser } from "../api/userApi";

import ChatWindow from "../components/ChatWindow";
import ConversationList from "../components/ConversationList";
import NewChatModal from "../components/NewChatModal";
import ProfileMenu from "../components/ProfileMenu";
import ProfileModal from "../components/ProfileModal";

import api from "../api/axios";

import {
  connectWebSocket,
  subscribeToSidebarRefresh,
  disconnectWebSocket,
} from "../api/websocketService";

export default function ChatPage() {
  const { user, logout, updateUser } = useAuth();

  const [selectedConversation, setSelectedConversation] = useState(null);
  const [conversations, setConversations] = useState([]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const [isSocketReady, setIsSocketReady] = useState(false);

  useEffect(() => {
    fetchConversations();
    loadCurrentUser();

    connectWebSocket(() => {
      setIsSocketReady(true);

      subscribeToSidebarRefresh((message) => {
        if (typeof message === "string") {
          fetchConversations();
          return;
        }

        handleConversationMessageUpdate(message.conversationId, message);
      });
    });

    return () => {
      disconnectWebSocket();
    };
  }, []);

  const fetchConversations = async () => {
    try {
      const response = await api.get("/conversations");

      setConversations(response.data);

      setSelectedConversation((currentSelected) => {
        if (!currentSelected) return currentSelected;

        const updatedSelected = response.data.find(
          (conversation) =>
            conversation.conversationId === currentSelected.conversationId
        );

        return updatedSelected || currentSelected;
      });
    } catch (error) {
      console.error("Failed to fetch conversations", error);
    }
  };

  const loadCurrentUser = async () => {
    try {
      const data = await getCurrentUser();

      updateUser({
        userId: data.id,
        name: data.name,
        email: data.email,
      });
    } catch (error) {
      console.error("Failed to load current user", error);
    }
  };

  const handleConversationCreated = (conversation) => {
    setSelectedConversation(conversation);
    setIsModalOpen(false);
  };

  const handleConversationMessageUpdate = (conversationId, message) => {
    setConversations((prev) => {
      const existingConversation = prev.find(
        (conversation) => conversation.conversationId === conversationId
      );

      if (!existingConversation) {
        fetchConversations();
        return prev;
      }

      const isActive = selectedConversation?.conversationId === conversationId;
      const isOwnMessage = message.senderId === user?.userId;

      const updatedConversation = {
        ...existingConversation,
        lastMessage: message.content,
        lastMessageTime: message.sentAt,
        unreadCount:
          isActive || isOwnMessage
            ? 0
            : (existingConversation.unreadCount || 0) + 1,
      };

      const remaining = prev.filter(
        (conversation) => conversation.conversationId !== conversationId
      );

      return [updatedConversation, ...remaining];
    });
  };

  const handleSelectConversation = (conversation) => {
    setSelectedConversation(conversation);

    setConversations((prev) =>
      prev.map((c) =>
        c.conversationId === conversation.conversationId
          ? { ...c, unreadCount: 0 }
          : c
      )
    );
  };

  return (
    <div className="app-shell">
      {/* SIDEBAR */}
      <aside className="sidebar">
        <div className="sidebar-top">
          <div>
            <h2>Pulse</h2>
            <p className="sidebar-subtitle">Real-time messaging</p>
          </div>

          <button
            className="icon-button primary"
            onClick={() => setIsModalOpen(true)}
            title="Start new chat"
          >
            <MessageSquarePlus size={20} />
          </button>
        </div>

        <ConversationList
          conversations={conversations}
          selectedConversation={selectedConversation}
          onSelectConversation={handleSelectConversation}
        />
      </aside>

      {/* MAIN CHAT AREA */}
      <main className="chat-panel with-topbar">
        <div className="chat-topbar">
          <ProfileMenu
            user={user}
            onOpenProfile={() => setIsProfileOpen(true)}
          />
        </div>

        {isSocketReady && (
          <ChatWindow
            conversation={selectedConversation}
            onMessageUpdate={handleConversationMessageUpdate}
          />
        )}
      </main>

      {/* NEW CHAT MODAL */}
      {isModalOpen && (
        <NewChatModal
          onClose={() => setIsModalOpen(false)}
          onConversationCreated={handleConversationCreated}
        />
      )}

      {/* PROFILE MODAL */}
      {isProfileOpen && (
        <ProfileModal
          user={user}
          onClose={() => setIsProfileOpen(false)}
          onLogout={logout}
        />
      )}
    </div>
  );
}