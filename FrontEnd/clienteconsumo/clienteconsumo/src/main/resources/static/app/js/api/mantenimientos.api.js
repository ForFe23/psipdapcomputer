import { http } from "./http.js";

export const mantenimientosApi = {
  list: () => http.get("/mantenimientos"),
  listBySerie: (serie) => http.get(`/mantenimientos/equipo/${encodeURIComponent(serie)}`),
  getById: (id) => http.get(`/mantenimientos/${id}`),
  create: (payload) => http.post("/mantenimientos", payload),
  update: (id, payload) => http.put(`/mantenimientos/${id}`, payload),
  remove: (id) => http.del(`/mantenimientos/${id}`),
  iniciar: (id) => http.put(`/mantenimientos/${id}/iniciar`),
  completar: (id) => http.put(`/mantenimientos/${id}/completar`)
};


