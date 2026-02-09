import { loadLayout } from "../ui/render.js";
import { rolesApi } from "../api/roles.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function row(r) {
  return `
    <tr>
      <td>${r.id ?? ""}</td>
      <td>${r.codigo ?? ""}</td>
      <td>${r.nombre ?? ""}</td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${r.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${r.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

async function loadData() {
  const tbody = document.querySelector("#roles-tbody");
  try {
    const data = await rolesApi.list();
    tbody.innerHTML = data.length ? data.map(row).join("") : `<tr><td colspan="4" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.delete;
        try {
          await rolesApi.remove(id);
          showSuccess("Rol eliminado");
          await loadData();
        } catch (err) {
          showError(err.message);
        }
      });
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-center text-danger">${err.message}</td></tr>`;
  }
}

async function main() {
  await loadLayout("roles");
  await loadData();
}

document.addEventListener("DOMContentLoaded", main);


