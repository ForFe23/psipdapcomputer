import { http } from "./http.js";

const buildQuery = (params = {}) => {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== "") search.set(k, v);
  });
  const q = search.toString();
  return q ? `?${q}` : "";
};

export const equiposApi = {
  list: (params = {}) => http.get(`/equipos${buildQuery(params)}`),
  create: (payload) => http.post("/equipos", payload),
  getBySerie: (serie) => http.get(`/equipos/serie/${encodeURIComponent(serie)}`),
  getById: (id) => http.get(`/equipos/${id}`),
  update: (id, payload) => http.put(`/equipos/${id}`, payload),
  remove: (id) => http.del(`/equipos/${id}`)
};


