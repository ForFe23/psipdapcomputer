import { http } from "./http.js";

export const incidentesApi = {
  list: () => http.get("/incidentes"),
  create: (payload) => http.post("/incidentes", payload),
  getById: (id) => http.get(`/incidentes/${id}`),
  update: (id, payload) => http.put(`/incidentes/${id}`, payload),
  remove: (id) => http.del(`/incidentes/${id}`),
  listBySerie: (serie) => http.get(`/incidentes/equipo/${encodeURIComponent(serie)}`)
};


