import { http } from "./http.js";

export const actaItemsApi = {
  list: (actaId) => http.get(`/actas/${actaId}/items`),
  create: (actaId, payload) => http.post(`/actas/${actaId}/items`, payload),
  update: (actaId, itemId, payload) => http.put(`/actas/${actaId}/items/${itemId}`, payload),
  remove: (actaId, itemId) => http.del(`/actas/${actaId}/items/${itemId}`)
};


