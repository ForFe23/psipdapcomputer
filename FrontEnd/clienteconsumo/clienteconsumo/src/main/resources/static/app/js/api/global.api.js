import { http } from "./http.js";

export const globalApi = {
  resumen: () => http.get("/global/resumen")
};


