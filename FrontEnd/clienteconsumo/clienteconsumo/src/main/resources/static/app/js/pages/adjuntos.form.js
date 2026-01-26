import { loadLayout } from "../ui/render.js";
import { adjuntosApi } from "../api/adjuntos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getActaId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("actaId");
}

async function main() {
  await loadLayout("adjuntos");
  const form = document.querySelector("#adjunto-form");
  const actaId = getActaId();
  if (actaId) {
    form.actaId.value = actaId;
  }
  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!form.actaId.value.trim()) {
      showError("Acta requerida");
      return;
    }
    if (!form.file.files[0]) {
      showError("Selecciona un archivo");
      return;
    }
    try {
      await adjuntosApi.upload(form.actaId.value.trim(), form.file.files[0]);
      showSuccess("Adjunto cargado");
      setTimeout(() => (window.location.href = `./list.html?actaId=${form.actaId.value.trim()}`), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
