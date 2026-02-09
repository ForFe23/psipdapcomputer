import { http } from "./http.js";

export const actaPerifericosApi = {
  list: () => http.get("/perifericos/acta-items"),
};


