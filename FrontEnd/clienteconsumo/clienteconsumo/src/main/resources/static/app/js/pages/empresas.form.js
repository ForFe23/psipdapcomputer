import { loadLayout } from "../ui/render.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadClientes(select) {
  try {
    const data = await clientesApi.list();
    select.innerHTML = `<option value=\"\">Seleccione cliente</option>` + data.map((c) => `<option value=\"${c.id}\">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch {
    select.innerHTML = `<option value=\"\">Sin clientes</option>`;
  }
}

async function loadEmpresa(id, form) {
  const data = await empresasApi.getById(id);
  form.clienteId.value = data.clienteId ?? "";
  form.nombre.value = data.nombre ?? "";
  form.estado.value = data.estado ?? "ACTIVO";
}

async function main() {
  await loadLayout("empresas");
  const form = document.querySelector("#empresa-form");
  await loadClientes(form.clienteId);
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar empresa";
    try {
      await loadEmpresa(id, form);
    } catch (err) {
      showError(err.message);
    }
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      clienteId: form.clienteId.value,
      nombre: form.nombre.value.trim(),
      estado: form.estado.value || "ACTIVO"
    };
    try {
      if (id) {
        await empresasApi.update(id, payload);
        showSuccess("Empresa actualizada");
      } else {
        await empresasApi.create(payload);
        showSuccess("Empresa creada");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);


