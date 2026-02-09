import { loadLayout } from "../ui/render.js";
import { globalApi } from "../api/global.api.js";
import { showError } from "../ui/alerts.js";

document.addEventListener("DOMContentLoaded", main);

async function main() {
  await loadLayout("global");
  const filterInput = document.getElementById("filter-cliente");
  const clearBtn = document.getElementById("btn-clear");
  let data = [];

  try {
    data = await globalApi.resumen();
    renderStats(data);
    renderList(data);
  } catch (err) {
    showError("No se pudo cargar el resumen global");
  }

  const applyFilter = () => {
    const term = (filterInput?.value || "").trim().toUpperCase();
    const filtered = term ? data.filter((c) => (c.nombre || "").toUpperCase().includes(term)) : data;
    renderList(filtered);
  };

  filterInput?.addEventListener("input", applyFilter);
  clearBtn?.addEventListener("click", () => {
    if (filterInput) filterInput.value = "";
    applyFilter();
  });
}

function renderStats(data) {
  const totalClientes = data.length;
  const totalEmpresas = data.reduce((acc, c) => acc + (c.empresas || 0), 0);
  const totalEquipos = data.reduce((acc, c) => acc + (c.equipos || 0), 0);
  setText("stat-clientes", totalClientes);
  setText("stat-empresas", totalEmpresas);
  setText("stat-equipos", totalEquipos);
}

function renderList(clientes) {
  const cont = document.getElementById("clientes-list");
  if (!cont) return;
  if (!clientes.length) {
    cont.innerHTML = `<div class="list-group-item text-muted text-center">Sin clientes</div>`;
    return;
  }
  const html = clientes
    .map((c) => {
      const empresas = (c.detalleEmpresas || []).map((e) => empresaRow(e)).join("");
      return `
        <div class="list-group-item">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <strong>${c.nombre}</strong>
              <div class="text-muted small">Empresas: ${c.empresas} · Equipos: ${c.equipos} · Usuarios: ${c.usuarios}</div>
            </div>
            <span class="badge badge-primary">${c.actas} actas</span>
          </div>
          <div class="mt-2">
            ${empresas || `<div class="text-muted small">Sin empresas</div>`}
          </div>
        </div>`;
    })
    .join("");
  cont.innerHTML = html;
}

function empresaRow(e) {
  return `
    <div class="d-flex justify-content-between align-items-center border rounded p-2 mb-2">
      <div>
        <div class="font-weight-bold">${e.nombre}</div>
        <div class="text-muted small">Equipos: ${e.equipos} · Usuarios: ${e.usuarios} · Ubicaciones: ${e.ubicaciones}</div>
      </div>
      <div class="text-right">
        <div class="small">Actas: <strong>${e.actas}</strong></div>
        <div class="small">Incidentes: <strong>${e.incidentes}</strong></div>
        <div class="small">Mantenimientos: <strong>${e.mantenimientos}</strong></div>
      </div>
    </div>
  `;
}

function setText(id, val) {
  const el = document.getElementById(id);
  if (el) el.textContent = val ?? "--";
}


