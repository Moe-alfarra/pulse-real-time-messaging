import { useMemo, useState } from "react";
import { Search } from "lucide-react";

function formatSidebarTime(dateString) {
  if (!dateString) return "";

  const date = new Date(dateString);
  const now = new Date();

  const sameDay = date.toDateString() === now.toDateString();

  if (sameDay) {
    return date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  return date.toLocaleDateString([], {
    month: "short",
    day: "numeric",
  });
}

function truncateMessage(message, maxLength = 34) {
  if (!message) return "Start chatting";
  if (message.length <= maxLength) return message;
  return `${message.slice(0, maxLength)}...`;
}

export default function ConversationList({
  conversations,
  selectedConversation,
  onSelectConversation,
}) {
  const [search, setSearch] = useState("");

  const sortedConversations = useMemo(() => {
    return [...conversations].sort((a, b) => {
      const aTime = a.lastMessageTime ? new Date(a.lastMessageTime).getTime() : 0;
      const bTime = b.lastMessageTime ? new Date(b.lastMessageTime).getTime() : 0;
      return bTime - aTime;
    });
  }, [conversations]);

  const filteredConversations = useMemo(() => {
    return sortedConversations.filter((conversation) =>
      `${conversation.otherUserName || ""} ${conversation.lastMessage || ""}`
        .toLowerCase()
        .includes(search.toLowerCase())
    );
  }, [sortedConversations, search]);

  return (
    <div className="conversation-list">
      <div className="sidebar-search">
        <Search size={16} />
        <input
          type="text"
          placeholder="Search conversations..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="section-label">Conversations</div>

      {filteredConversations.length === 0 ? (
        <div className="empty-sidebar-state">
          {search.trim()
            ? "No conversations found."
            : "No conversations yet. Press the + button to start one."}
        </div>
      ) : (
        filteredConversations.map((conversation) => (
          <button
            key={conversation.conversationId}
            className={`conversation-item ${
              selectedConversation?.conversationId === conversation.conversationId
                ? "active"
                : ""
            }`}
            onClick={() => onSelectConversation(conversation)}
          >
            <div className="conversation-avatar">
              {conversation.otherUserName?.charAt(0)?.toUpperCase()}
            </div>

            <div className="conversation-text">
              <div className="conversation-row">
                <h4>{conversation.otherUserName}</h4>

                <div className="conversation-meta">
                  <span className="conversation-time">
                    {formatSidebarTime(conversation.lastMessageTime)}
                  </span>

                  {conversation.unreadCount > 0 && (
                    <span className="unread-badge">{conversation.unreadCount}</span>
                  )}
                </div>
              </div>

              <p className="conversation-preview">
                {truncateMessage(conversation.lastMessage)}
              </p>
            </div>
          </button>
        ))
      )}
    </div>
  );
}