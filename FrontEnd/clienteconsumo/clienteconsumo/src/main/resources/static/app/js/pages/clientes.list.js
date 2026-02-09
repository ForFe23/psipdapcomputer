import { loadLayout } from "../ui/render.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function row(c) {
  return `
    <tr>
      <td>${c.id ?? ""}</td>
      <td>${c.nombre ?? ""}</td>
      <td>${c.email ?? ""}</td>
      <td>${c.licencia ?? ""}</td>
      <td>${c.ciudad ?? ""}</td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${c.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${c.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

async function loadData() {
  const tbody = document.querySelector("#clientes-tbody");
  try {
    const data = await clientesApi.list();
    tbody.innerHTML = data.length ? data.map(row).join("") : `<tr><td colspan="6" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.delete;
        try {
          await clientesApi.remove(id);
          showSuccess("Cliente eliminado");
          await loadData();
        } catch (err) {
          showError(err.message);
        }
      });
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="6" class="text-center text-danger">${err.message}</td></tr>`;
  }
}

async function main() {
  await loadLayout("clientes");
  await loadData();
}

document.addEventListener("DOMContentLoaded", main);


