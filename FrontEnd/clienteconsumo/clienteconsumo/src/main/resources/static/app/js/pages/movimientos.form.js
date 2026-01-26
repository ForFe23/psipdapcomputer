import { loadLayout } from "../ui/render.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadMovimiento(id, form) {
  const data = await movimientosApi.getById(id);
  form.equipoId.value = data.equipoId ?? "";
  form.usuarioId.value = data.usuarioId ?? "";
  form.actaId.value = data.actaId ?? "";
  form.tipo.value = data.tipo ?? "";
  form.origen.value = data.origen ?? "";
  form.destino.value = data.destino ?? "";
  form.observaciones.value = data.observaciones ?? "";
  form.fecha.value = data.fecha ?? "";
}

async function main() {
  await loadLayout("movimientos");
  const form = document.querySelector("#movimiento-form");
  const id = getId();
  if (id) {
    form.querySelector("[data-form-title]").textContent = "Editar movimiento";
    try {
      await loadMovimiento(id, form);
    } catch (err) {
      showError(err.message);
    }
  }
  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const payload = {
      equipoId: form.equipoId.value.trim(),
      usuarioId: form.usuarioId.value.trim(),
      actaId: form.actaId.value.trim(),
      tipo: form.tipo.value.trim(),
      origen: form.origen.value.trim(),
      destino: form.destino.value.trim(),
      observaciones: form.observaciones.value.trim(),
      fecha: form.fecha.value
    };
    try {
      if (id) {
        await movimientosApi.update(id, payload);
        showSuccess("Movimiento actualizado");
      } else {
        await movimientosApi.create(payload);
        showSuccess("Movimiento creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
