import { useEffect, useRef, useState } from "react";
import { Clock3, SendHorizontal } from "lucide-react";
import { getMessages } from "../api/messageApi";
import { markConversationAsRead } from "../api/conversationApi";
import {
  subscribeToConversation,
  subscribeToConversationRead,
  sendWebSocketMessage,
} from "../api/websocketService";
import { useAuth } from "../context/AuthContext";

function formatMessageTime(dateString) {
  if (!dateString) return "";
  return new Date(dateString).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
}

export default function ChatWindow({ conversation, onMessageUpdate }) {
  const { user } = useAuth();
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const messagesEndRef = useRef(null);

  useEffect(() => {
    if (!conversation) return;

    let messageSubscription = null;
    let readSubscription = null;

    setMessages([]);
    loadMessages(conversation.conversationId);
    handleMarkAsRead(conversation.conversationId);

    messageSubscription = subscribeToConversation(
      conversation.conversationId,
      (message) => {
        setMessages((prev) => [...prev, message]);

        if (onMessageUpdate) {
          onMessageUpdate(conversation.conversationId, message);
        }

        if (message.senderId !== user?.userId) {
          handleMarkAsRead(conversation.conversationId);
        }
      }
    );

    readSubscription = subscribeToConversationRead(
      conversation.conversationId,
      (readerId) => {
        if (Number(readerId) !== user?.userId) {
          setMessages((prev) =>
            prev.map((msg) =>
              msg.senderId === user?.userId ? { ...msg, read: true } : msg
            )
          );
        }
      }
    );

    return () => {
      messageSubscription?.unsubscribe();
      readSubscription?.unsubscribe();
    };
  }, [conversation, user?.userId]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const loadMessages = async (conversationId) => {
    try {
      const data = await getMessages(conversationId);
      setMessages(data);

      if (data.length > 0 && onMessageUpdate) {
        const lastMessage = data[data.length - 1];
        onMessageUpdate(conversationId, lastMessage);
      }
    } catch (error) {
      console.error("Failed to load messages", error);
    }
  };

  const handleMarkAsRead = async (conversationId) => {
    try {
      await markConversationAsRead(conversationId);
    } catch (error) {
      console.error("Failed to mark conversation as read", error);
    }
  };

  const handleSend = () => {
    if (!input.trim() || !conversation) return;

    sendWebSocketMessage(conversation.conversationId, input.trim());
    setInput("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleSend();
    }
  };

  if (!conversation) {
    return (
      <div className="chat-empty modern-empty">
        <div className="empty-illustration">💬</div>
        <h3>Welcome to Pulse</h3>
        <p>Select a conversation from the left and start chatting instantly.</p>
      </div>
    );
  }

  return (
    <div className="chat-window light">
      <div className="chat-header modern">
        <div className="chat-user-meta">
          <div className="avatar-circle large">
            {conversation.otherUserName?.charAt(0)?.toUpperCase()}
          </div>

          <div>
            <h3>{conversation.otherUserName}</h3>
            <p>{conversation.otherUserEmail}</p>
          </div>
        </div>
      </div>

      <div className="messages modern">
        {messages.map((msg) => {
          const isOwn = msg.senderId === user?.userId;

          return (
            <div
              key={msg.messageId}
              className={`message-row ${isOwn ? "own" : "other"}`}
            >
              <div className={`message-bubble ${isOwn ? "own" : "other"}`}>
                {!isOwn && (
                  <span className="message-sender">{msg.senderName}</span>
                )}

                <p>{msg.content}</p>

                <div className="message-time">
                  <Clock3 size={12} />
                  <span>{formatMessageTime(msg.sentAt)}</span>

                  {isOwn && (
                    <span className="message-status">
                      {msg.read ? "✓✓" : "✓"}
                    </span>
                  )}
                </div>
              </div>
            </div>
          );
        })}

        <div ref={messagesEndRef} />
      </div>

      <div className="message-input modern">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder={`Message ${conversation.otherUserName}...`}
        />
        <button onClick={handleSend}>
          <SendHorizontal size={18} />
          Send
        </button>
      </div>
    </div>
  );
}