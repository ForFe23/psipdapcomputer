import { http } from "./http.js";

export const adjuntosApi = {
  listByActa: (actaId) => http.get(`/actas/${actaId}/adjuntos`),
  upload: (actaId, file) => {
    const formData = new FormData();
    formData.append("file", file);
    return http.post(`/actas/${actaId}/adjuntos`, formData);
  }
};
