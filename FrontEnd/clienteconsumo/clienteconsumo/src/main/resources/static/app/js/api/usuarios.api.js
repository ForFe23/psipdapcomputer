import { http } from "./http.js";

export const usuariosApi = {
  list: () => http.get("/usuarios"),
  listByCliente: (idCliente) => http.get(`/usuarios/cliente/${encodeURIComponent(idCliente)}`),
  listByEmpresa: (empresaId) => http.get(`/usuarios?empresaId=${encodeURIComponent(empresaId)}`),
  getById: (id) => http.get(`/usuarios/${id}`),
  create: (payload) => http.post("/usuarios", payload),
  update: (id, payload) => http.put(`/usuarios/${id}` , payload),
  remove: (id) => http.del(`/usuarios/${id}`)
};


