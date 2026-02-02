import { loadLayout } from "../ui/render.js";
import { actaItemsApi } from "../api/acta-items.api.js";
import { actasApi } from "../api/actas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let actaId = null;
let equipos = [];
let empresas = [];

function empresaNombre(id) {
  const e = empresas.find((em) => String(em.id) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
}

function getParam(name) {
  const url = new URL(window.location.href);
  return url.searchParams.get(name);
}

function renderRow(item) {
  return `
    <tr>
      <td>${item.id ?? ""}</td>
      <td>${item.itemNumero ?? ""}</td>
      <td>${item.tipo ?? ""}</td>
      <td>${item.serie ?? item.equipoSerie ?? ""}</td>
      <td>${item.modelo ?? ""}</td>
      <td>${item.equipoId ?? ""}</td>
      <td>${item.observacion ?? ""}</td>
      <td class="text-right">
        <button class="btn btn-sm btn-outline-primary mr-1" data-edit="${item.id}">Editar</button>
        <button class="btn btn-sm btn-outline-danger" data-delete="${item.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

function renderSeries(empresaId) {
  const seriesList = document.querySelector("#seriesList");
  if (!seriesList) return;
  const filtered = empresaId ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(empresaId)) : equipos;
  const sugerencia = `<option value="⬇SUGERENCIAS⬇"></option>`;
  seriesList.innerHTML = sugerencia + filtered.map((e) => `<option value="${e.serie || e.serieEquipo}">${e.serie || e.serieEquipo}</option>`).join("");
}

function syncEmpresaFromSerie(serieVal) {
  const equipoHidden = document.querySelector("#equipoId");
  const clienteSelect = document.querySelector("#clienteSelect");
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

async function loadCatalogos() {
  try {
    empresas = await empresasApi.list();
    const sel = document.querySelector("#clienteSelect");
    if (sel) sel.innerHTML = `<option value="">Seleccione empresa</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    empresas = [];
  }
  try {
    equipos = await equiposApi.list();
    renderSeries("");
  } catch (err) {
    equipos = [];
  }
}

async function loadActaInfo() {
  if (!actaId) return;
  try {
    const data = await actasApi.getById(actaId);
    const info = document.getElementById("acta-info");
    if (info) info.textContent = `Acta ${data.codigo || actaId} · Empresa ${empresaNombre(data.empresaId ?? data.idCliente ?? data.clienteId)}`;
  } catch (err) {
    
  }
}

async function loadItems() {
  const tbody = document.querySelector("#items-tbody");
  try {
    const data = await actaItemsApi.list(actaId);
    tbody.innerHTML = data.length ? data.map(renderRow).join("") : `<tr><td colspan="8" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-edit]").forEach((btn) => {
      btn.addEventListener("click", () => fillForm(data.find((i) => String(i.id) === btn.dataset.edit)));
    });
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        try {
          await actaItemsApi.remove(actaId, btn.dataset.delete);
          showSuccess("Item eliminado");
          await loadItems();
        } catch (err) {
          showError(err.message);
        }
      });
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="8" class="text-center text-danger">${err.message}</td></tr>`;
  }
}

function fillForm(item) {
  const form = document.querySelector("#item-form");
  if (!item) return;
  document.querySelector("#itemId").value = item.id;
  form.itemNumero.value = item.itemNumero ?? "";
  form.tipo.value = item.tipo ?? "";
  form.equipoSerie.value = item.serie ?? item.equipoSerie ?? "";
  form.modelo.value = item.modelo ?? "";
  form.observacion.value = item.observacion ?? "";
  const equipoHidden = document.querySelector("#equipoId");
  if (equipoHidden) equipoHidden.value = item.equipoId ?? "";
}

async function main() {
  actaId = getParam("actaId");
  await loadLayout("actas");
  if (!actaId) {
    showError("Falta actaId");
    return;
  }
  await loadCatalogos();
  await loadActaInfo();
  await loadItems();

  const form = document.querySelector("#item-form");
  const serieInput = document.querySelector("#equipoSerie");
  const clienteSelect = document.querySelector("#clienteSelect");
  if (serieInput) serieInput.addEventListener("input", () => syncEmpresaFromSerie(serieInput.value));
  if (clienteSelect) clienteSelect.addEventListener("change", () => renderSeries(clienteSelect.value));

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!form.equipoId.value) {
      showError("Selecciona un equipo válido");
      return;
    }
    if (!form.equipoSerie.value.trim()) {
      showError("La serie es obligatoria");
      return;
    }
    const itemId = document.querySelector("#itemId").value;
    const payload = {
      itemNumero: Number(form.itemNumero.value),
      tipo: form.tipo.value.trim(),
      serie: form.equipoSerie.value.trim(),
      modelo: form.modelo.value.trim() || null,
      observacion: form.observacion.value.trim() || null,
      equipoId: Number(form.equipoId.value),
      equipoSerie: form.equipoSerie.value.trim()
    };
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
      renderSeries("");
      await loadItems();
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
