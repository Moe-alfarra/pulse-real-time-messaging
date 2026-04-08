import api from "./axios";

export const markConversationAsRead = async (conversationId) => {
  await api.put(`/messages/conversations/${conversationId}/read`);
};

