import { http } from "./http.js";

export const kardexApi = {
  getByEquipo: (equipoId) => http.get(`/kardex/equipo/${equipoId}`)
};
