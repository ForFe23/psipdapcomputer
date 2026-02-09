import { loadLayout } from "../ui/render.js";
import { actaItemsApi } from "../api/acta-items.api.js";
import { actasApi } from "../api/actas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let actaId = null;
let equipos = [];
let empresas = [];
let acta = null;
let equipoBase = null;

const getParam = (name) => new URL(window.location.href).searchParams.get(name);

const empresaNombre = (id) => {
  const e = empresas.find((em) => String(em.id) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
};

const renderRow = (item) => `
  <tr>
    <td>${item.id ?? ""}</td>
    <td>${item.itemNumero ?? ""}</td>
    <td>${item.tipo ?? ""}</td>
    <td>${item.serie ?? item.equipoSerie ?? ""}</td>
    <td>${item.marca ?? ""}</td>
    <td>${item.modelo ?? ""}</td>
    <td>${item.equipoId ?? ""}</td>
    <td>${item.observacion ?? ""}</td>
    <td class="text-right">
      <button class="btn btn-sm btn-outline-primary mr-1" data-edit="${item.id}">Editar</button>
      <button class="btn btn-sm btn-outline-danger" data-delete="${item.id}">Eliminar</button>
    </td>
  </tr>
`;

const nextItemNumero = (items = []) => {
  const max = items.reduce((m, i) => Math.max(m, Number(i.itemNumero || 0)), 0);
  return max + 1;
};

const renderSeries = (empresaId) => {
  const list = document.querySelector("#seriesList");
  if (!list) return;
  const filtered = empresaId
    ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(empresaId))
    : equipos;
  list.innerHTML =
    `<option value=\"⬇SUGERENCIAS⬇\"></option>` +
    filtered.map((e) => `<option value="${e.serie || e.serieEquipo}">${e.serie || e.serieEquipo}</option>`).join("");
};

const fillBase = () => {
  const empresaTxt = document.getElementById("empresaTxt");
  const equipoBaseTxt = document.getElementById("equipoBaseTxt");
  const baseEquipoId = document.getElementById("baseEquipoId");
  if (empresaTxt) empresaTxt.value = empresaNombre(acta?.empresaId ?? acta?.idCliente ?? "");
  if (equipoBaseTxt) equipoBaseTxt.value = equipoBase ? `${equipoBase.serie || ""} · ID ${equipoBase.id}` : "";
  if (baseEquipoId) baseEquipoId.value = equipoBase?.id || "";
  renderSeries(acta?.empresaId ?? acta?.idCliente);
};

const syncFromSerie = (serieVal) => {
  if (!serieVal) return;
  const eq = equipos.find((e) => (e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
  if (eq) {
    document.getElementById("tipo").value ||= eq.tipo || "";
    document.getElementById("modelo").value ||= eq.modelo || "";
  }
};

const loadItems = async () => {
  const tbody = document.querySelector("#items-tbody");
  try {
    const data = await actaItemsApi.list(actaId);
    tbody.innerHTML = data.length ? data.map(renderRow).join("") : `<tr><td colspan="9" class="text-center text-muted">Sin datos</td></tr>`;
    
    const itemInput = document.getElementById("itemNumero");
    if (itemInput) itemInput.value = nextItemNumero(data);

    tbody.querySelectorAll("[data-edit]").forEach((btn) =>
      btn.addEventListener("click", () => fillForm(data.find((i) => String(i.id) === btn.dataset.edit)))
    );
    tbody.querySelectorAll("[data-delete]").forEach((btn) =>
      btn.addEventListener("click", async () => {
        try {
          await actaItemsApi.remove(actaId, btn.dataset.delete);
          showSuccess("Item eliminado");
          await loadItems();
        } catch (err) {
          showError(err.message);
        }
      })
    );
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="9" class="text-center text-danger">${err.message}</td></tr>`;
  }
};

const fillForm = (item) => {
  const form = document.querySelector("#item-form");
  if (!item) return;
  document.querySelector("#itemId").value = item.id;
  form.itemNumero.value = item.itemNumero ?? "";
  form.tipo.value = item.tipo ?? "";
  form.equipoSerie.value = item.serie ?? item.equipoSerie ?? "";
  form.marca.value = item.marca ?? "";
  form.modelo.value = item.modelo ?? "";
  form.observacion.value = item.observacion ?? "";
  document.getElementById("registrarPeriferico").checked = item.registrarPeriferico !== false;
};

const loadCatalogos = async () => {
  try {
    empresas = await empresasApi.list();
  } catch (err) {
    empresas = [];
  }
  try {
    equipos = await equiposApi.list();
  } catch (err) {
    equipos = [];
  }
};

const loadActa = async () => {
  acta = await actasApi.getById(actaId);
  if (acta?.idEquipo) {
    try {
      equipoBase = await equiposApi.getById(acta.idEquipo);
    } catch (err) {
      equipoBase = null;
    }
  }
  const info = document.getElementById("acta-info");
  if (info) info.textContent = `Acta ${acta.codigo || actaId} · Empresa ${empresaNombre(acta.empresaId ?? acta.idCliente ?? acta.clienteId)}`;
  fillBase();
};

const handleSubmit = async (ev) => {
  ev.preventDefault();
  const form = ev.target;
  const baseEquipoId = document.getElementById("baseEquipoId").value;
  if (!baseEquipoId) {
    showError("Falta equipo base del acta");
    return;
  }
  const payload = {
    itemNumero: Number(form.itemNumero.value),
    tipo: form.tipo.value.trim(),
    serie: form.equipoSerie.value.trim() || "NO SERIALIZADO",
    marca: form.marca.value.trim() || null,
    modelo: form.modelo.value.trim() || null,
    observacion: form.observacion.value.trim() || null,
    equipoId: Number(baseEquipoId),
    equipoSerie: form.equipoSerie.value.trim() || "NO SERIALIZADO",
    estadoInterno: "ACTIVO_INTERNAL",
  };
  const itemId = document.querySelector("#itemId").value;
  try {
    if (itemId) {
      await actaItemsApi.update(actaId, itemId, payload);
      showSuccess("Item actualizado");
    } else {
      await actaItemsApi.create(actaId, payload);
      showSuccess("Item creado");
    }
    document.querySelector("#itemId").value = "";
    form.reset();
    document.getElementById("registrarPeriferico").checked = true;
    fillBase();
    await loadItems();
  } catch (err) {
    showError(err.message);
  }
};

document.addEventListener("DOMContentLoaded", async () => {
  actaId = getParam("actaId");
  await loadLayout("actas");
  if (!actaId) {
    showError("Falta actaId");
    return;
  }
  try {
    await loadCatalogos();
    await loadActa();
    await loadItems();
  } catch (err) {
    showError(err.message);
  }

  const form = document.querySelector("#item-form");
  form.addEventListener("submit", handleSubmit);
  const serieInput = document.querySelector("#equipoSerie");
  serieInput?.addEventListener("input", () => syncFromSerie(serieInput.value));
});


