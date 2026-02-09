import { loadLayout } from "../ui/render.js";
import { kardexApi } from "../api/kardex.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError } from "../ui/alerts.js";

let equipos = [];

function renderSeries(clienteId) {
  const seriesList = document.querySelector("#seriesList");
  if (!seriesList) return;
  const filtered = clienteId ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(clienteId)) : equipos;
  seriesList.innerHTML = `<option value="⬇SUGERENCIAS⬇"></option>` + filtered.map((e) => `<option value="${e.serie || e.serieEquipo}">${e.serie || e.serieEquipo}</option>`).join("");
}

function syncClienteFromSerie(serieVal) {
  const clienteSelect = document.querySelector("#clienteSelect");
  const equipoHidden = document.querySelector("#equipoId");
  if (!serieVal) {
    if (equipoHidden) equipoHidden.value = "";
    return;
  }
  const eq = equipos.find((e) => (e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
  if (eq) {
    if (clienteSelect) clienteSelect.value = eq.empresaId ?? eq.idCliente ?? "";
    if (equipoHidden) equipoHidden.value = eq.id ?? "";
    renderSeries(eq.empresaId ?? eq.idCliente);
  } else if (equipoHidden) {
    equipoHidden.value = "";
  }
}

function renderMovimientos(data = []) {
  const tbody = document.querySelector("#movimientos-tbody");
  const rows = data.map((m) => `
    <tr>
      <td>${m.tipo ?? ""}</td>
      <td>${m.ubicacionOrigen ?? ""}</td>
      <td>${m.ubicacionDestino ?? ""}</td>
      <td>${m.fecha ? String(m.fecha).replace("T", " ") : ""}</td>
    </tr>
  `);
  tbody.innerHTML = rows.length ? rows.join("") : `<tr><td colspan="4" class="text-center text-muted">Sin movimientos</td></tr>`;
  const badge = document.querySelector("#mov-count");
  if (badge) badge.textContent = rows.length;
}

function renderIncidentes(data = []) {
  const tbody = document.querySelector("#incidentes-tbody");
  const rows = data.map((i) => `
    <tr>
      <td>${i.detalle ?? ""}</td>
      <td>${i.fechaIncidente ? String(i.fechaIncidente).replace("T", " ") : ""}</td>
      <td>${i.tecnico ?? ""}</td>
    </tr>
  `);
  tbody.innerHTML = rows.length ? rows.join("") : `<tr><td colspan="3" class="text-center text-muted">Sin incidentes</td></tr>`;
  const badge = document.querySelector("#inc-count");
  if (badge) badge.textContent = rows.length;
}

async function loadCatalogos() {
  try {
    const clientes = await empresasApi.list();
    const sel = document.querySelector("#clienteSelect");
    if (sel) sel.innerHTML = `<option value="">Seleccione empresa</option>` + clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    
  }
  try {
    equipos = await equiposApi.list();
    renderSeries("");
  } catch (err) {
    equipos = [];
  }
}

async function consultar() {
  const equipoId = document.querySelector("#equipoId").value;
  const desde = document.querySelector("#desde").value;
  const hasta = document.querySelector("#hasta").value;
  if (!equipoId) {
    showError("Selecciona un equipo");
    return;
  }
  try {
    const data = await kardexApi.getByEquipo(equipoId);
    let movimientos = data.movimientos || [];
    let incidentes = data.incidentes || [];
    if (desde) {
      movimientos = movimientos.filter((m) => m.fecha && m.fecha >= desde);
      incidentes = incidentes.filter((i) => i.fechaIncidente && i.fechaIncidente.substring(0, 10) >= desde);
    }
    if (hasta) {
      movimientos = movimientos.filter((m) => m.fecha && m.fecha.substring(0, 10) <= hasta);
      incidentes = incidentes.filter((i) => i.fechaIncidente && i.fechaIncidente.substring(0, 10) <= hasta);
    }
    renderMovimientos(movimientos);
    renderIncidentes(incidentes);
  } catch (err) {
    showError(err.message);
  }
}

async function main() {
  await loadLayout("kardex");
  await loadCatalogos();
  const serieInput = document.querySelector("#serieEquipo");
  const clienteSelect = document.querySelector("#clienteSelect");
  if (serieInput) serieInput.addEventListener("input", () => syncClienteFromSerie(serieInput.value));
  if (clienteSelect) clienteSelect.addEventListener("change", () => renderSeries(clienteSelect.value));
  document.querySelector("#kardex-form").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    await consultar();
  });
}

document.addEventListener("DOMContentLoaded", main);


