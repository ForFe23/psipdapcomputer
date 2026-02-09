import { loadLayout } from "../ui/render.js";
import { rolesApi } from "../api/roles.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadRol(id, form) {
  const data = await rolesApi.getById(id);
  form.codigo.value = data.codigo ?? "";
  form.nombre.value = data.nombre ?? "";
}

async function main() {
  await loadLayout("roles");
  const form = document.querySelector("#rol-form");
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar rol";
    try {
      await loadRol(id, form);
    } catch (err) {
      showError(err.message);
    }
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      codigo: form.codigo.value.trim(),
      nombre: form.nombre.value.trim()
    };
    try {
      if (id) {
        await rolesApi.update(id, payload);
        showSuccess("Rol actualizado");
      } else {
        await rolesApi.create(payload);
        showSuccess("Rol creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);


