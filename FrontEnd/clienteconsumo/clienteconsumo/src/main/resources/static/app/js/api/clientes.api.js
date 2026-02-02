import { http } from "./http.js";

export const clientesApi = {
  list: () => http.get("/clientes"),
  getById: (id) => http.get(`/clientes/${id}`),
  create: (payload) => http.post("/clientes", payload),
  update: (id, payload) => http.put(`/clientes/${id}`, payload),
  remove: (id) => http.del(`/clientes/${id}`)
};
