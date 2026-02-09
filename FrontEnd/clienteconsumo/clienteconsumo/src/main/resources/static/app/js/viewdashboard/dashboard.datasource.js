import { equiposApi } from "../api/equipos.api.js";
import { mantenimientosApi } from "../api/mantenimientos.api.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { actasApi } from "../api/actas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { incidentesApi } from "../api/incidentes.api.js";
import { adjuntosApi } from "../api/adjuntos.api.js";

let cacheData = null;

export async function getDashboardData() {
  if (cacheData) return cacheData;
  const [equiposRes, mantRes, movRes, clientesRes, actasRes, empresasRes, incidentesRes] = await Promise.allSettled([
    equiposApi.list(),
    mantenimientosApi.list(),
    movimientosApi.list(),
    clientesApi.list(),
    actasApi.list(),
    empresasApi.list(),
    incidentesApi.list()
  ]);
  cacheData = {
    equipos: equiposRes.status === "fulfilled" ? equiposRes.value : [],
    mantenimientos: mantRes.status === "fulfilled" ? mantRes.value : [],
    movimientos: movRes.status === "fulfilled" ? movRes.value : [],
    clientes: clientesRes.status === "fulfilled" ? clientesRes.value : [],
    actas: actasRes.status === "fulfilled" ? actasRes.value : [],
    empresas: empresasRes.status === "fulfilled" ? empresasRes.value : [],
    incidentes: incidentesRes.status === "fulfilled" ? incidentesRes.value : []
  };
  return cacheData;
}

export function clearDashboardCache() {
  cacheData = null;
}

export async function fetchAdjuntosRecientes(actas) {
  const seleccion = actas.slice(0, 5);
  const adjuntos = await Promise.all(
    seleccion.map(async (a) => {
      try {
        const lista = await adjuntosApi.listByActa(a.id);
        return (lista || []).map((it) => ({ ...it, actaId: a.id, actaCodigo: a.codigo || a.id }));
      } catch (err) {
        return [];
      }
    })
  );
  return adjuntos.flat().sort((a, b) => (b.id || 0) - (a.id || 0)).slice(0, 5);
}


