import api from "./axios";

export const getMessages = async (conversationId) => {
  const response = await api.get(`/messages/conversation/${conversationId}`);
  return response.data;
};


export const sendMessage = async (conversationId, content) => {
  const response = await api.post("/messages", {
    conversationId,
    content,
  });

  return response.data;
};