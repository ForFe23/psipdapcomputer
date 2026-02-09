import { http } from "./http.js";

export const authApi = {
  login: (correo, password) => http.post("/auth/login", { correo, password })
};


