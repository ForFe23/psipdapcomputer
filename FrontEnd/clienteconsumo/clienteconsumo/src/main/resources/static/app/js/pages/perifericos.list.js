import { loadLayout } from "../ui/render.js";
import { perifericosApi } from "../api/perifericos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

const getParam = (name) => new URL(window.location.href).searchParams.get(name) || "";
const toUpper = (val) => (val ? val.trim().toUpperCase() : "");

const empresaNombre = (id, empresas) => {
  const c = empresas.find((cl) => String(cl.id) === String(id));
  return c ? (c.nombre || c.razonSocial || c.id) : id;
};

const deviceLabel = (serie, activo, marca, modelo) => {
  const parts = [];
  if (serie) parts.push(`<strong>${serie}</strong>`);
  if (activo) parts.push(`<span class="badge badge-light border">${activo}</span>`);
  const detalle = [marca, modelo].filter(Boolean).join(" / ");
  if (detalle) parts.push(`<span class="text-muted">${detalle}</span>`);
  return parts.length ? parts.join("<br>") : `<span class="text-muted">-</span>`;
};

const rowTemplate = (p, empresas) => `
  <tr>
    <td>
      <div class="btn-group btn-group-sm" role="group">
        <a class="btn btn-outline-primary" href="./form.html?id=${p.id}&serie=${encodeURIComponent(p.serieEquipo || "")}">Editar</a>
        <button class="btn btn-outline-danger btn-del" data-id="${p.id}" data-serie="${p.serieEquipo}">Eliminar</button>
      </div>
    </td>
    <td>${p.id ?? ""}</td>
    <td>${p.serieEquipo ?? ""}</td>
    <td>${empresaNombre(p.empresaId ?? p.idCliente, empresas) ?? ""}</td>
    <td>${deviceLabel(p.serieMonitor, p.activoMonitor, p.marcaMonitor, p.modeloMonitor)}</td>
    <td>${deviceLabel(p.serieTeclado, p.activoTeclado, p.marcaTeclado, p.modeloTeclado)}</td>
    <td>${deviceLabel(p.serieMouse, p.activoMouse, p.marcaMouse, p.modeloMouse)}</td>
    <td>${deviceLabel(p.serieTelefono, p.activoTelefono, p.marcaTelefono, p.modeloTelefono)}</td>
  </tr>
`;

document.addEventListener("DOMContentLoaded", main);

async function main() {
  await loadLayout("perifericos");
  const serieParam = getParam("serie");
  const serieInput = document.querySelector("#serieFilter");
  const empresaSelect = document.querySelector("#fEmpresa");
  const tbody = document.querySelector("#perifericos-tbody");

  if (serieInput) serieInput.value = serieParam;

  let empresas = [];
  let equipos = [];
  let datos = [];

  const setStat = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  };

  const renderStats = (items) => {
    const total = items.length;
    const conMonitor = items.filter((p) => p.serieMonitor || p.activoMonitor).length;
    const conTeclado = items.filter((p) => p.serieTeclado || p.activoTeclado).length;
    const conTelefono = items.filter((p) => p.serieTelefono || p.activoTelefono).length;
    setStat("stat-total", total);
    setStat("stat-monitor", conMonitor);
    setStat("stat-teclado", conTeclado);
    setStat("stat-telefono", conTelefono);
  };

  const renderTable = (items) => {
    if (!tbody) return;
    if (!items.length) {
      tbody.innerHTML = `<tr><td colspan="8" class="text-center text-muted">Sin periféricos registrados</td></tr>`;
      return;
    }
    tbody.innerHTML = items.map((p) => rowTemplate(p, empresas)).join("");
  };

  const applyFilters = () => {
    let filtered = [...datos];
    const serie = toUpper(serieInput?.value);
    const empresaSel = empresaSelect?.value;
    if (serie) filtered = filtered.filter((p) => toUpper(p.serieEquipo).includes(serie));
    if (empresaSel) filtered = filtered.filter((p) => String(p.empresaId) === empresaSel);
    renderTable(filtered);
    renderStats(filtered);
  };

  const loadEmpresas = async () => {
    try {
      empresas = await empresasApi.list();
      if (empresaSelect) {
        empresaSelect.innerHTML =
          `<option value="">Todas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      }
    } catch (err) {
      
    }
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      equipos = [];
    }
  };

  const load = async () => {
    try {
      const params = {};
      const serie = toUpper(serieInput?.value);
      if (serie) params.serie = serie;
      const data = await perifericosApi.list(params);
      const mergeEmpresa = (p) => {
        const eq =
          equipos.find((e) => String(e.id) === String(p.equipoId)) ||
          equipos.find((e) => toUpper(e.serie || e.serieEquipo) === toUpper(p.serieEquipo));
        const empresaId = eq?.empresaId ?? eq?.idCliente ?? p.empresaId ?? p.idCliente ?? null;
        return { ...p, empresaId };
      };
      datos = (data || [])
        .filter((p) => (p.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL)
        .map(mergeEmpresa);
      renderTable(datos);
      renderStats(datos);
    } catch (err) {
      showError("No se pudieron cargar los periféricos");
    }
  };

  document.querySelector("#filter-form")?.addEventListener("submit", (ev) => {
    ev.preventDefault();
    applyFilters();
  });
  serieInput?.addEventListener("input", applyFilters);
  empresaSelect?.addEventListener("change", applyFilters);

  document.addEventListener("click", async (ev) => {
    const btn = ev.target.closest(".btn-del");
    if (!btn) return;
    const { id, serie } = btn.dataset;
    if (!id || !serie) return;
    if (!confirm("¿Eliminar periférico?")) return;
    try {
      await perifericosApi.remove(id, serie);
      showSuccess("Periférico eliminado");
      await load();
      applyFilters();
    } catch (err) {
      showError("No se pudo eliminar el periférico");
    }
  });

  await Promise.all([loadEmpresas(), loadEquipos()]);
  await load();
  applyFilters();
}
