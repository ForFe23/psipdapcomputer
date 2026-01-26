import { loadLayout } from "../ui/render.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { showError } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

function row(m) {
  return `
    <tr>
      <td>${m.id ?? ""}</td>
      <td>${m.equipoId ?? ""}</td>
      <td>${m.usuarioId ?? ""}</td>
      <td>${m.tipo ?? ""}</td>
      <td>${m.origen ?? ""}</td>
      <td>${m.destino ?? ""}</td>
      <td>${m.fecha ?? ""}</td>
    </tr>
  `;
}

function readFilters(form) {
  const filters = {};
  if (form.equipoId.value.trim()) filters.equipoId = form.equipoId.value.trim();
  if (form.usuarioId.value.trim()) filters.usuarioId = form.usuarioId.value.trim();
  if (form.desde.value) filters.desde = form.desde.value;
  if (form.hasta.value) filters.hasta = form.hasta.value;
  return filters;
}

async function loadTable(filters) {
  const tbody = document.querySelector("#movimientos-tbody");
  const dataRaw = await movimientosApi.list(filters);
  const data = (dataRaw || []).filter((m) => (m.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
  tbody.innerHTML = data.length ? data.map(row).join("") : `<tr><td colspan="7" class="text-center text-muted">Sin movimientos</td></tr>`;
}

async function main() {
  await loadLayout("movimientos");
  const form = document.querySelector("#filtros-movimientos");
  try {
    await loadTable({});
  } catch (err) {
    showError(err.message);
  }
  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    try {
      await loadTable(readFilters(form));
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
