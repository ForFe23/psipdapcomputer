import { http } from "./http.js";

function buildQuery(filters = {}) {
  const params = new URLSearchParams();
  if (filters.empresaId) params.append("empresaId", filters.empresaId);
  if (filters.clienteId) params.append("clienteId", filters.clienteId);
  const qs = params.toString();
  return qs ? `?${qs}` : "";
}

export const personasApi = {
  list: (filters) => http.get(`/personas${buildQuery(filters)}`),
  listByEmpresa: (empresaId) => http.get(`/personas${buildQuery({ empresaId })}`),
  listByCliente: (clienteId) => http.get(`/personas${buildQuery({ clienteId })}`),
  getById: (id) => http.get(`/personas/${id}`),
  create: (payload) => http.post("/personas", payload),
  update: (id, payload) => http.put(`/personas/${id}`, payload),
  remove: (id) => http.del(`/personas/${id}`)
};


