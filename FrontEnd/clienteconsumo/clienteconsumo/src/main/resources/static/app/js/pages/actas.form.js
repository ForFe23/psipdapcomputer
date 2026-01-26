import { loadLayout } from "../ui/render.js";
import { actasApi } from "../api/actas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadActa(id, form) {
  const data = await actasApi.getById(id);
  form.clienteId.value = data.idCliente ?? data.clienteId ?? "";
  form.emisorId.value = data.idUsuarioEmisor ?? data.emisorId ?? "";
  form.receptorId.value = data.idUsuarioReceptor ?? data.receptorId ?? "";
  form.ubicacion.value = data.ubicacionDestino ?? data.ubicacion ?? "";
  form.observaciones.value = data.observaciones ?? "";
  form.estado.value = data.estado ?? "";
}

async function main() {
  await loadLayout("actas");
  const form = document.querySelector("#acta-form");
  const id = getId();
  if (id) {
    form.querySelector("[data-form-title]").textContent = "Editar acta";
    try {
      await loadActa(id, form);
    } catch (err) {
      showError(err.message);
    }
  }
  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      clienteId: form.clienteId.value.trim(),
      emisorId: form.emisorId.value.trim(),
      receptorId: form.receptorId.value.trim(),
      ubicacionDestino: form.ubicacion.value.trim(),
      observaciones: form.observaciones.value.trim(),
      estado: form.estado.value.trim()
    };
    try {
      if (id) {
        await actasApi.update(id, payload);
        showSuccess("Acta actualizada");
      } else {
        await actasApi.create(payload);
        showSuccess("Acta creada");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
