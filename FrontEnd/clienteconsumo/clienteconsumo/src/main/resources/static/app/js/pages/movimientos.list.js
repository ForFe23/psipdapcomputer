import { loadLayout } from "../ui/render.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];

const empresaNombre = (id) => {
  const e = empresas.find((em) => String(em.id) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
};

function row(m) {
  const empresaTxt = empresaNombre(m.empresaId ?? m.idCliente);
  return `
    <tr>
      <td>${m.id ?? ""}</td>
      <td>${m.idEquipo ?? m.equipoId ?? ""}</td>
      <td><span class="badge badge-light">${m.tipo ?? ""}</span></td>
      <td>${m.ubicacionOrigen ?? m.origen ?? ""}</td>
      <td>${m.ubicacionDestino ?? m.destino ?? ""}</td>
      <td>${m.fecha ?? ""}</td>
      <td><span class="badge ${estadoBadge(m.estadoInterno)}">${m.estadoInterno ?? ""}</span></td>
      <td>${m.observacion ?? ""}</td>
      <td>${empresaTxt}</td>
      <td>
        <a class="btn btn-sm btn-outline-primary" href="./form.html?id=${m.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${m.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

function readFilters(form) {
  const filters = {};
  if (form.equipoId.value.trim()) filters.equipoId = form.equipoId.value.trim();
  if (form.usuarioId.value.trim()) filters.usuarioId = form.usuarioId.value.trim();
  if (form.empresaId?.value) filters.empresaId = form.empresaId.value;
  if (form.tipo.value) filters.tipo = form.tipo.value;
  if (form.estado.value) filters.estadoInterno = form.estado.value;
  if (form.desde.value) filters.desde = form.desde.value;
  if (form.hasta.value) filters.hasta = form.hasta.value;
  return filters;
}

function filtrar(data, filtros) {
  return data.filter((m) => {
    if (filtros.empresaId && String(m.empresaId ?? m.idCliente) !== filtros.empresaId) return false;
    if (filtros.tipo && (m.tipo || "").toUpperCase() !== filtros.tipo.toUpperCase()) return false;
    if (filtros.estadoInterno && (m.estadoInterno || "").toUpperCase() !== filtros.estadoInterno.toUpperCase()) return false;
    if (filtros.desde && m.fecha && m.fecha.substring(0, 10) < filtros.desde) return false;
    if (filtros.hasta && m.fecha && m.fecha.substring(0, 10) > filtros.hasta) return false;
    return true;
  });
}

async function loadTable(filters) {
  const tbody = document.querySelector("#movimientos-tbody");
  const data = await movimientosApi.list(filters);
  const filtrado = filtrar(data || [], filters);
  tbody.innerHTML = (filtrado && filtrado.length)
    ? filtrado.map(row).join("")
    : `<tr><td colspan="10" class="text-center text-muted">Sin movimientos</td></tr>`;
}

async function main() {
  await loadLayout("movimientos");
  const form = document.querySelector("#filtros-movimientos");
  const tbody = document.querySelector("#movimientos-tbody");
  try {
    empresas = await empresasApi.list();
    const empresaSelect = document.getElementById("fEmpresa");
    if (empresaSelect) {
      empresaSelect.innerHTML = `<option value=\"\">Todas</option>` + empresas.map((e) => `<option value=\"${e.id}\">${e.nombre || e.razonSocial || e.id}</option>`).join("");
    }
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

  document.addEventListener("movimientos:reload", async () => {
    await loadTable({});
  });

  tbody.addEventListener("click", async (ev) => {
    const btn = ev.target.closest("[data-delete]");
    if (!btn) return;
    const id = btn.dataset.delete;
    if (!id) return;
    if (!window.confirm("¿Eliminar movimiento?")) return;
    try {
      await movimientosApi.remove(id);
      await loadTable(readFilters(form));
      showSuccess("Movimiento eliminado");
    } catch (err) {
      showError(err.message);
    }
  });
}

function estadoBadge(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ACT")) return "badge-success";
  if (e.includes("INACT")) return "badge-secondary";
  return "badge-light";
}

document.addEventListener("DOMContentLoaded", main);
