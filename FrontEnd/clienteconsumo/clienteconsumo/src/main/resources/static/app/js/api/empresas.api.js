import { http } from "./http.js";

export const empresasApi = {
  list: () => http.get("/empresas"),
  getById: (id) => http.get(`/empresas/${id}`),
  create: (payload) => http.post("/empresas", payload),
  update: (id, payload) => http.put(`/empresas/${id}`, payload),
  remove: (id) => http.del(`/empresas/${id}`)
};
