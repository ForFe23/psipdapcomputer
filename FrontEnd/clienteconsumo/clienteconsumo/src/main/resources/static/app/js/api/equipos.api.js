import { http } from "./http.js";

export const equiposApi = {
  list: () => http.get("/equipos"),
  create: (payload) => http.post("/equipos", payload),
  getBySerie: (serie) => http.get(`/equipos/serie/${encodeURIComponent(serie)}`),
  getById: (id) => http.get(`/equipos/${id}`),
  update: (id, payload) => http.put(`/equipos/${id}`, payload),
  remove: (id) => http.del(`/equipos/${id}`)
};
