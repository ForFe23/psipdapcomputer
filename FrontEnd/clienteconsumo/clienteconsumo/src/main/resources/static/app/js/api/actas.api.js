import { http } from "./http.js";

export const actasApi = {
  list: () => http.get("/actas"),
  create: (payload) => http.post("/actas", payload),
  getById: (id) => http.get(`/actas/${id}`),
  update: (id, payload) => http.put(`/actas/${id}`, payload),
  remove: (id) => http.del(`/actas/${id}`),
  listItems: (id) => http.get(`/actas/${id}/items`)
};
