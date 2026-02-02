import { http } from "./http.js";

const buildQuery = (params = {}) => {
  const search = new URLSearchParams();
  if (params.serie) search.set("serie", params.serie);
  if (params.clienteId) search.set("clienteId", params.clienteId);
  const query = search.toString();
  return query ? `?${query}` : "";
};

export const perifericosApi = {
  list: (params = {}) => http.get(`/perifericos${buildQuery(params)}`),
  listBySerie: (serie) => http.get(`/perifericos${buildQuery({ serie })}`),
  listByCliente: (clienteId) => http.get(`/perifericos${buildQuery({ clienteId })}`),
  get: (id) => http.get(`/perifericos/${id}`),
  create: (payload) => http.post("/perifericos", payload),
  update: (id, payload) => http.put(`/perifericos/${id}`, payload),
  remove: (id) => http.del(`/perifericos/${id}`)
};
