import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;

export const connectWebSocket = (onConnected) => {
  if (stompClient && (stompClient.active || stompClient.connected)) return;

  const token = localStorage.getItem("token");
  const socket = new SockJS(import.meta.env.VITE_API_URL.replace("/api","") + "/ws");

  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },
    debug: () => {},
  });

  stompClient.onConnect = () => {
    if (onConnected) onConnected();
  };

  stompClient.onStompError = (frame) => {
  };

  stompClient.activate();
};

export const subscribeToConversation = (conversationId, onMessageReceived) => {
  if (!stompClient || !stompClient.connected) {
    return null;
  }


  return stompClient.subscribe(`/topic/conversation/${conversationId}`, (message) => {
    const body = JSON.parse(message.body);
    onMessageReceived(body);
  });
};

export const subscribeToSidebarRefresh = (onSidebarMessage) => {
  if (!stompClient || !stompClient.connected) return null;

  return stompClient.subscribe(`/user/queue/sidebar`, (message) => {
    let body;

    try {
      body = JSON.parse(message.body);
    } catch {
      body = message.body;
    }

    onSidebarMessage(body);
  });
};

export const sendWebSocketMessage = (conversationId, content) => {
  if (!stompClient || !stompClient.connected) {
    return;
  }

  stompClient.publish({
    destination: "/app/chat.send",
    body: JSON.stringify({
      conversationId,
      content,
    }),
  });
};

export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
};

export const subscribeToConversationRead = (conversationId, onReadReceived) => {
  if (!stompClient || !stompClient.connected) return null;

  return stompClient.subscribe(
    `/topic/conversation/${conversationId}/read`,
    (message) => {
      onReadReceived(message.body);
    }
  );
};
