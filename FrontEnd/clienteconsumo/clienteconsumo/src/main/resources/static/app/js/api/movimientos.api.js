import { http } from "./http.js";

function buildQuery(filters = {}) {
  const params = new URLSearchParams();
  if (filters.equipoId) params.append("equipoId", filters.equipoId);
  if (filters.usuarioId) params.append("usuarioId", filters.usuarioId);
  if (filters.desde) params.append("desde", filters.desde);
  if (filters.hasta) params.append("hasta", filters.hasta);
  const qs = params.toString();
  return qs ? `?${qs}` : "";
}

export const movimientosApi = {
  list: (filters) => http.get(`/movimientos${buildQuery(filters)}`),
  create: (payload) => http.post("/movimientos", payload),
  update: (id, payload) => http.put(`/movimientos/${id}`, payload),
  getById: (id) => http.get(`/movimientos/${id}`),
  remove: (id) => http.del(`/movimientos/${id}`)
};
