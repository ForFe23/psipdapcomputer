import { http } from "./http.js";

export const usuariosApi = {
  list: () => http.get("/usuarios"),
  listByCliente: (idCliente) => http.get(`/usuarios/cliente/${encodeURIComponent(idCliente)}`),
  getById: (id) => http.get(`/usuarios/${id}`)
};
