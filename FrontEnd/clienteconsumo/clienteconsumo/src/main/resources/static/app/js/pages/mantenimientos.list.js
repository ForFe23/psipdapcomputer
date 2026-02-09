import { loadLayout } from "../ui/render.js";
import { mantenimientosApi } from "../api/mantenimientos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

const getParam = (name) => new URL(window.location.href).searchParams.get(name) || "";

const parseDate = (val) => (val ? new Date(val) : null);
const fmtDateTime = (val) => (val ? String(val).replace("T", " ").slice(0, 19) : "");
const addDays = (d, days) => {
  if (!d || isNaN(days)) return null;
  const copy = new Date(d.getTime());
  copy.setDate(copy.getDate() + days);
  return copy;
};

function computeStats(items) {
  const total = items.length;
  const recurrentes = items.filter((m) => m.frecuenciaDias && m.frecuenciaDias > 0).length;
  const unicos = total - recurrentes;
  const proximos7 = items.filter((m) => {
    const f = parseDate(m.fechaProgramada);
    if (!f) return false;
    const now = new Date();
    const diff = (f - now) / (1000 * 60 * 60 * 24);
    return diff >= 0 && diff <= 7;
  }).length;
  return { total, recurrentes, unicos, proximos7 };
}

const empresaNombre = (id, empresas) => {
  const c = empresas.find((cl) => String(cl.id) === String(id));
  return c ? c.nombre || c.razonSocial || c.id : id;
};

function rowTemplate(m, empresas) {
  const fecha = fmtDateTime(m.fechaProgramada);
  const prox = m.frecuenciaDias ? fmtDateTime(addDays(parseDate(m.fechaProgramada), m.frecuenciaDias)) : "";
  const estado = (m.estado || "").toUpperCase();
  const btnIniciar = estado === "PROGRAMADO" || estado === "PENDIENTE";
  const btnCompletar = estado === "EN_CURSO";
  return `
    <tr>
      <td>
        <div class="btn-group btn-group-sm" role="group">
          <a class="btn btn-outline-primary" href="./form.html?id=${m.id}">Editar</a>
          ${btnIniciar ? `<button class="btn btn-outline-success btn-start" data-id="${m.id}">Iniciar</button>` : ""}
          ${btnCompletar ? `<button class="btn btn-outline-info btn-complete" data-id="${m.id}">Completar</button>` : ""}
          <button class="btn btn-outline-danger btn-del" data-id="${m.id}">Eliminar</button>
        </div>
      </td>
      <td>${m.id ?? ""}</td>
      <td>${m.serieEquipo ?? ""}</td>
      <td>${empresaNombre(m.empresaId ?? m.idCliente, empresas) ?? ""}</td>
      <td>${fecha}</td>
      <td>${m.frecuenciaDias ?? ""}</td>
      <td>${m.estado ?? ""}</td>
      <td>${m.descripcion ?? ""}</td>
      <td>${prox}</td>
    </tr>
  `;
}

async function main() {
  await loadLayout("mantenimientos");
  const serieParam = getParam("serie");
  const inputSerie = document.querySelector("#serieFilter");
  const selectEmpresa = document.querySelector("#fEmpresa");
  if (inputSerie) inputSerie.value = serieParam;

  const tbody = document.querySelector("#mantenimientos-tbody");
  let empresas = [];
  let datos = [];

  const renderStats = (items) => {
    const { total, recurrentes, unicos, proximos7 } = computeStats(items);
    const set = (id, val) => {
      const el = document.getElementById(id);
      if (el) el.textContent = val;
    };
    set("stat-total", total);
    set("stat-recurrentes", recurrentes);
    set("stat-unicos", unicos);
    set("stat-proximos", proximos7);
  };

  const renderTable = (items) => {
    if (!tbody) return;
    if (!items.length) {
      tbody.innerHTML = `<tr><td colspan="9" class="text-center text-muted">Sin datos</td></tr>`;
      return;
    }
    tbody.innerHTML = items.map((m) => rowTemplate(m, empresas)).join("");
  };

  const applyFilters = () => {
    const serie = (inputSerie?.value || "").trim().toUpperCase();
    const empresaSel = selectEmpresa?.value || "";
    const estadoSel = document.querySelector("#estadoFilter")?.value || "";
    let filtered = [...datos];
    if (serie) filtered = filtered.filter((m) => (m.serieEquipo || "").toUpperCase().includes(serie));
    if (empresaSel) filtered = filtered.filter((m) => String(m.empresaId ?? m.idCliente) === empresaSel);
    if (estadoSel) filtered = filtered.filter((m) => (m.estado || "").toUpperCase() === estadoSel.toUpperCase());
    renderTable(filtered);
    renderStats(filtered);
  };

  const load = async () => {
    try {
      const serie = (inputSerie?.value || "").trim();
      const data = serie ? await mantenimientosApi.listBySerie(serie) : await mantenimientosApi.list();
      datos = (data || []).filter((m) => (m.estado || "").toUpperCase() !== INACTIVO_INTERNAL);
      renderTable(datos);
      renderStats(datos);
    } catch (err) {
      showError("No se pudo cargar mantenimientos");
    }
  };

  const loadEmpresas = async () => {
    try {
      empresas = await empresasApi.list();
      if (selectEmpresa) {
        selectEmpresa.innerHTML =
          `<option value="">Todas las empresas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      }
    } catch (err) {
      
    }
  };

  const form = document.querySelector("#filter-form");
  if (form) {
    form.addEventListener("submit", (ev) => {
      ev.preventDefault();
      applyFilters();
    });
  }
  document.querySelector("#estadoFilter")?.addEventListener("change", applyFilters);
  inputSerie?.addEventListener("input", applyFilters);
  selectEmpresa?.addEventListener("change", applyFilters);

  document.addEventListener("click", async (ev) => {
    const btn = ev.target.closest(".btn-del");
    if (!btn) return;
    const id = btn.dataset.id;
    if (!id) return;
    if (!confirm("¿Eliminar mantenimiento?")) return;
    try {
      await mantenimientosApi.remove(id);
      showSuccess("Eliminado");
      await load();
      applyFilters();
    } catch (err) {
      showError("No se pudo eliminar");
    }
  });

  document.addEventListener("click", async (ev) => {
    const startBtn = ev.target.closest(".btn-start");
    const completeBtn = ev.target.closest(".btn-complete");
    try {
      if (startBtn) {
        const id = startBtn.dataset.id;
        await mantenimientosApi.iniciar(id);
        showSuccess("Mantenimiento en curso");
        await load();
        applyFilters();
      } else if (completeBtn) {
        const id = completeBtn.dataset.id;
        await mantenimientosApi.completar(id);
        showSuccess("Mantenimiento completado");
        await load();
        applyFilters();
      }
    } catch (err) {
      showError("No se pudo actualizar estado");
    }
  });

  await loadEmpresas();
  await load();
  applyFilters();
}

document.addEventListener("DOMContentLoaded", main);


