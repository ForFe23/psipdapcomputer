import { loadLayout } from "../ui/render.js";
import { perifericosApi } from "../api/perifericos.api.js";
import { actasApi } from "../api/actas.api.js";
import { actaItemsApi } from "../api/acta-items.api.js";
import { actaPerifericosApi } from "../api/acta-perifericos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";
const toUpper = (v) => (v ? v.trim().toUpperCase() : "");
const getParam = (name) => new URL(window.location.href).searchParams.get(name) || "";

const empresaNombre = (id, empresas) => {
  const c = empresas.find((cl) => String(cl.id) === String(id));
  return c ? c.nombre || c.razonSocial || c.id : id;
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
        ${p.origen === "ACTA"
          ? `<a class="btn btn-outline-secondary" href="../acta-items/list.html?actaId=${p.actaId}">Ver acta</a>`
          : `<a class="btn btn-outline-primary" href="./form.html?id=${p.id}&serie=${encodeURIComponent(p.serieEquipo || "")}">Editar</a>`}
        ${p.origen === "ACTA" ? "" : `<button class="btn btn-outline-danger btn-del" data-id="${p.id}" data-serie="${p.serieEquipo}">Eliminar</button>`}
      </div>
      ${p.origen === "ACTA" ? `<div><span class="badge badge-info mt-1">Desde acta ${p.actaCodigo || p.actaId || ""}</span></div>` : ""}
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
    if (serie) filtered = filtered.filter((p) => toUpper(p.serieEquipo || p.serieMonitor).includes(serie));
    if (empresaSel) filtered = filtered.filter((p) => String(p.empresaId) === empresaSel);
    filtered = filtered.map((p) => ({
      ...p,
      serieEquipo: p.serieEquipo || p.serieMonitor,
      origen: p.origen || (String(p.id || "").startsWith("acta-") ? "ACTA" : "INVENTARIO"),
    }));
    renderTable(filtered);
    renderStats(filtered);
  };

  const loadEmpresas = async () => {
    try {
      empresas = await empresasApi.list();
      if (empresaSelect) {
        empresaSelect.innerHTML =
          `<option value=\"\">Todas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      }
    } catch (err) {
      empresas = [];
    }
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      equipos = [];
    }
  };

  const dedupe = (list) => {
    const map = new Map();
    list.forEach((p) => {
      const key = `${p.id}-${p.serieEquipo || ""}-${p.equipoId || ""}`;
      if (!map.has(key)) map.set(key, p);
    });
    return Array.from(map.values());
  };

const mapActaItemToPeripheral = (acta, item) => ({
  id: `acta-${acta.id}-item-${item.id}`,
  serieEquipo: item.serie || item.equipoSerie || "-",
  empresaId: acta.empresaId ?? acta.idCliente,
  equipoId: item.equipoId ?? acta.idEquipo,
  serieMonitor: item.serie || item.equipoSerie || "-",
  marcaMonitor: item.marca || "",
  modeloMonitor: item.modelo || "",
  estadoInterno: item.estadoInterno,
  origen: "ACTA",
  actaId: acta.id,
  actaCodigo: acta.codigo,
  itemNumero: item.itemNumero,
});

  const loadActaItemsAsPeripherals = async (serieFilter, empresaFilter) => {
    try {
      const actas = await actasApi.list();
      const candidatas = actas.filter((a) => {
        const est = (a.estado || "").toUpperCase();
        return est.includes("CER") || est.includes("EMIT") || est.includes("REG");
      });
      const result = [];
      for (const a of candidatas) {
        const items = await actaItemsApi.list(a.id);
        items
          .filter((it) => it.accesorio || it.registrarPeriferico !== false)
          .forEach((it) => {
            const mapped = mapActaItemToPeripheral(a, it);
            if (serieFilter && !toUpper(mapped.serieEquipo).includes(serieFilter)) return;
            if (empresaFilter && String(mapped.empresaId) !== empresaFilter) return;
            result.push(mapped);
          });
      }
      return result;
    } catch (err) {
      return [];
    }
  };

  const loadActaPeripheralsEndpoint = async () => {
    try {
      const data = await actaPerifericosApi.list();
      return data.map((p) => ({
        ...p,
        origen: p.origen || "ACTA",
      }));
    } catch (err) {
      return [];
    }
  };

  const load = async () => {
    try {
      const serie = toUpper(serieInput?.value);
      const empresaId = empresaSelect?.value || "";
      const params = {};
      if (serie) params.serie = serie;
      if (empresaId) params.empresaId = empresaId;

      const [basePer, actaExtraEndpoint, actaExtraDerived] = await Promise.all([
        perifericosApi.list(params),
        loadActaPeripheralsEndpoint(),
        loadActaItemsAsPeripherals(serie, empresaId),
      ]);

      const mergeEmpresa = (p) => {
        const eq =
          equipos.find((e) => String(e.id) === String(p.equipoId)) ||
          equipos.find((e) => toUpper(e.serie || e.serieEquipo) === toUpper(p.serieEquipo));
        const empresaIdMerged = eq?.empresaId ?? eq?.idCliente ?? p.empresaId ?? p.idCliente ?? null;
        return { ...p, empresaId: empresaIdMerged };
      };

      datos = dedupe(
        [...(basePer || []), ...(actaExtraEndpoint || []), ...(actaExtraDerived || [])]
          .filter((p) => (p.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL)
          .map(mergeEmpresa)
      );
      applyFilters();
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
    } catch (err) {
      showError("No se pudo eliminar el periférico");
    }
  });

  await Promise.all([loadEmpresas(), loadEquipos()]);
  await load();
}


