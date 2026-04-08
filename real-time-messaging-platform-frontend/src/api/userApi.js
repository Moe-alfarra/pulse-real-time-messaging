import api from "./axios";

export const getCurrentUser = async () => {
  const response = await api.get("/users/me");
  return response.data;
};

export const updateUserName = async (name) => {
  const response = await api.put(`/users/me?name=${encodeURIComponent(name)}`);
  return response.data;
};

export const updateUserPassword = async (currentPassword, newPassword) => {
  const response = await api.put(
    `/users/me/password?currentPassword=${encodeURIComponent(currentPassword)}&newPassword=${encodeURIComponent(newPassword)}`
  );
  return response.data;
};