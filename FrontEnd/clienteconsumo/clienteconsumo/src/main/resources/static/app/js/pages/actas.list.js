import { loadLayout } from "../ui/render.js";
import { actasApi } from "../api/actas.api.js";
import { showError } from "../ui/alerts.js";

function row(a) {
  return `
    <tr>
      <td>${a.id ?? ""}</td>
      <td>${a.codigo ?? ""}</td>
      <td>${a.estado ?? ""}</td>
      <td>${a.idCliente ?? a.clienteId ?? ""}</td>
      <td>${a.fecha ?? ""}</td>
      <td>
        <a class="btn btn-sm btn-outline-primary" href="./form.html?id=${a.id}">Editar</a>
        <a class="btn btn-sm btn-outline-secondary" href="../adjuntos/list.html?actaId=${a.id}">Adjuntos</a>
      </td>
    </tr>
  `;
}

async function main() {
  await loadLayout("actas");
  const tbody = document.querySelector("#actas-tbody");
  try {
    const data = await actasApi.list();
    tbody.innerHTML = data.map(row).join("");
  } catch (err) {
    showError(err.message);
  }
}

document.addEventListener("DOMContentLoaded", main);
