import { loadLayout } from "../ui/render.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadCliente(id, form) {
  const data = await clientesApi.getById(id);
  form.nombre.value = data.nombre ?? "";
  form.email.value = data.email ?? "";
  form.contrasena.value = data.contrasena ?? "";
  form.licencia.value = data.licencia ?? "";
  form.fechaLicencia.value = data.fechaLicencia ? String(data.fechaLicencia).slice(0, 10) : "";
  form.estadoInterno.value = data.estadoInterno ?? "";
  form.calle.value = data.calle ?? "";
  form.ciudad.value = data.ciudad ?? "";
}

async function main() {
  await loadLayout("clientes");
  const form = document.querySelector("#cliente-form");
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar cliente";
    try {
      await loadCliente(id, form);
    } catch (err) {
      showError(err.message);
    }
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      nombre: form.nombre.value.trim(),
      email: form.email.value.trim() || null,
      contrasena: form.contrasena.value.trim() || null,
      licencia: form.licencia.value.trim() || null,
      fechaLicencia: form.fechaLicencia.value || null,
      estadoInterno: form.estadoInterno.value || null,
      calle: form.calle.value.trim() || null,
      ciudad: form.ciudad.value.trim() || null
    };
    try {
      if (id) {
        await clientesApi.update(id, payload);
        showSuccess("Cliente actualizado");
      } else {
        await clientesApi.create(payload);
        showSuccess("Cliente creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
