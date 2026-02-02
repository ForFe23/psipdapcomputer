import { loadLayout } from "../ui/render.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadEmpresas(select) {
  try {
    const data = await empresasApi.list();
    select.innerHTML = `<option value="">Seleccione empresa</option>` + data.map((e) => `<option value="${e.id}">${e.nombre || e.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin empresas</option>`;
  }
}

async function loadUbicacion(id, form) {
  const data = await ubicacionesApi.getById(id);
  form.empresaId.value = data.empresaId ?? "";
  form.nombre.value = data.nombre ?? "";
  form.direccion.value = data.direccion ?? "";
  form.estado.value = data.estado ?? "ACTIVO";
}

async function main() {
  await loadLayout("ubicaciones");
  const form = document.querySelector("#ubicacion-form");
  await loadEmpresas(form.empresaId);
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

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      empresaId: form.empresaId.value,
      nombre: form.nombre.value.trim(),
      direccion: form.direccion.value.trim() || null,
      estado: form.estado.value || "ACTIVO"
    };
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
