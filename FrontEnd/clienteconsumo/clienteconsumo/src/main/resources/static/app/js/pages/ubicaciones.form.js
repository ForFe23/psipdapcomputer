import { loadLayout } from "../ui/render.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadClientes(sel) {
  try {
    const data = await clientesApi.list();
    sel.innerHTML = `<option value=\"\">Seleccione cliente</option>` + data.map((c) => `<option value=\"${c.id}\">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch {
    sel.innerHTML = `<option value=\"\">Sin clientes</option>`;
  }
}

async function loadEmpresas(select, clienteId) {
  try {
    const data = clienteId ? await empresasApi.listByCliente(clienteId) : await empresasApi.list();
    select.innerHTML = `<option value=\"\">Seleccione empresa (opcional)</option>` + data.map((e) => `<option value=\"${e.id}\">${e.nombre || e.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value=\"\">Sin empresas</option>`;
  }
}

async function loadUbicacion(id, form) {
  const data = await ubicacionesApi.getById(id);
  form.clienteId.value = data.clienteId ?? "";
  await loadEmpresas(form.empresaId, data.clienteId ?? "");
  form.empresaId.value = data.empresaId ?? "";
  form.nombre.value = data.nombre ?? "";
  form.direccion.value = data.direccion ?? "";
  form.estado.value = data.estado ?? "ACTIVO";
}

async function main() {
  await loadLayout("ubicaciones");
  const form = document.querySelector("#ubicacion-form");
  await loadClientes(form.clienteId);
  const params = new URLSearchParams(window.location.search);
  const clienteParam = params.get("clienteId") || "";
  const empresaParam = params.get("empresaId") || "";
  if (clienteParam) {
    form.clienteId.value = clienteParam;
    form.clienteId.readOnly = true;
  }
  await loadEmpresas(form.empresaId, form.clienteId.value || "");
  if (empresaParam) {
    form.empresaId.value = empresaParam;
    form.empresaId.readOnly = true;
  }
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar ubicación";
    try {
      await loadUbicacion(id, form);
    } catch (err) {
      showError(err.message);
    }
  }
  if (form.clienteId) {
    form.clienteId.addEventListener("change", () => {
      loadEmpresas(form.empresaId, form.clienteId.value);
      if (!form.clienteId.value) form.empresaId.value = "";
    });
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      clienteId: form.clienteId.value || null,
      empresaId: form.empresaId.value || null,
      nombre: form.nombre.value.trim(),
      direccion: form.direccion.value.trim() || null,
      estado: form.estado.value || "ACTIVO"
    };
    if (!payload.clienteId && !payload.empresaId) {
      showError("Selecciona cliente o empresa.");
      return;
    }
    try {
      if (id) {
        await ubicacionesApi.update(id, payload);
        showSuccess("Ubicación actualizada");
      } else {
        await ubicacionesApi.create(payload);
        showSuccess("Ubicación creada");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);


