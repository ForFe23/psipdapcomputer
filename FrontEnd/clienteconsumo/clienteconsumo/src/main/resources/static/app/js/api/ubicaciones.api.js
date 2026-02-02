import { http } from "./http.js";

function buildQuery(filters = {}) {
  const params = new URLSearchParams();
  if (filters.empresaId) params.append("empresaId", filters.empresaId);
  const qs = params.toString();
  return qs ? `?${qs}` : "";
}

export const ubicacionesApi = {
  list: (filters) => http.get(`/ubicaciones${buildQuery(filters)}`),
  listByEmpresa: (empresaId) => http.get(`/ubicaciones${buildQuery({ empresaId })}`),
  getById: (id) => http.get(`/ubicaciones/${id}`),
  create: (payload) => http.post("/ubicaciones", payload),
  update: (id, payload) => http.put(`/ubicaciones/${id}`, payload),
  remove: (id) => http.del(`/ubicaciones/${id}`)
};
