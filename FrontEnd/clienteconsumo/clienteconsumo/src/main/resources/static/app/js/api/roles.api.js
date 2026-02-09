import { http } from "./http.js";

export const rolesApi = {
  list: () => http.get("/roles"),
  getById: (id) => http.get(`/roles/${id}`),
  create: (payload) => http.post("/roles", payload),
  update: (id, payload) => http.put(`/roles/${id}`, payload),
  remove: (id) => http.del(`/roles/${id}`)
};


