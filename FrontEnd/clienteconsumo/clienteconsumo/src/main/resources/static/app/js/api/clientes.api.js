import { http } from "./http.js";

export const clientesApi = {
  list: () => http.get("/clientes"),
  getById: (id) => http.get(`/clientes/${id}`)
};
