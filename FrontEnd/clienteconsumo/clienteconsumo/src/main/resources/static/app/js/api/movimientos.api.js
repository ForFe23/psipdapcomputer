import { http } from "./http.js";

export const movimientosApi = {
  list: (filters = {}) => {
    if (filters.equipoId) return http.get(`/movimientos/equipo/${filters.equipoId}`);
    if (filters.usuarioId) return http.get(`/movimientos/usuario/${filters.usuarioId}`);
    return http.get("/movimientos");
  },
  create: (payload) => http.post("/movimientos", payload),
  update: (id, payload) => http.put(`/movimientos/${id}`, payload),
  getById: (id) => http.get(`/movimientos/${id}`),
  remove: (id) => http.del(`/movimientos/${id}`)
};


