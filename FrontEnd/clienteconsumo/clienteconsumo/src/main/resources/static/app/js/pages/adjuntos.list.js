import { loadLayout } from "../ui/render.js";
import { adjuntosApi } from "../api/adjuntos.api.js";
import { showError } from "../ui/alerts.js";

function getActaId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("actaId");
}

function row(item) {
  return `
    <tr>
      <td>${item.id ?? ""}</td>
      <td>${item.nombre ?? ""}</td>
      <td>${item.tipo ?? ""}</td>
      <td>${item.tamano ?? ""}</td>
    </tr>
  `;
}

async function loadTable(actaId) {
  const tbody = document.querySelector("#adjuntos-tbody");
  const data = await adjuntosApi.listByActa(actaId);
  tbody.innerHTML = data.map(row).join("");
}

async function main() {
  await loadLayout("adjuntos");
  const form = document.querySelector("#buscar-adjuntos");
  const actaInput = form.actaId;
  const initial = getActaId();
  if (initial) {
    actaInput.value = initial;
    try {
      await loadTable(initial);
    } catch (err) {
      showError(err.message);
    }
  }
  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!actaInput.value.trim()) return;
    try {
      await loadTable(actaInput.value.trim());
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
