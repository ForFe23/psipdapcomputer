import { loadLayout } from "../ui/render.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let clientes = [];

function clienteNombre(id) {
  const c = clientes.find((cl) => String(cl.id) === String(id));
  return c ? c.nombre || c.razonSocial || c.id : "";
}

function row(e) {
  return `
    <tr>
      <td>${e.id ?? ""}</td>
      <td>${clienteNombre(e.clienteId)}</td>
      <td>${e.nombre ?? ""}</td>
      <td><span class="badge ${estadoBadge(e.estadoInterno || e.estado)}">${e.estadoInterno ?? e.estado ?? ""}</span></td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${e.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${e.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

function estadoBadge(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ACT")) return "badge-success";
  if (e.includes("INACT")) return "badge-secondary";
  return "badge-light";
}

async function loadData() {
  const tbody = document.querySelector("#empresas-tbody");
  try {
    const data = await empresasApi.list();
    tbody.innerHTML = data.length ? data.map(row).join("") : `<tr><td colspan="5" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.delete;
        if (!confirm("¿Eliminar empresa?")) return;
        try {
          await empresasApi.remove(id);
          showSuccess("Empresa eliminada");
          await loadData();
        } catch (err) {
          showError(err.message);
        }
      });
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${err.message}</td></tr>`;
  }
}

async function main() {
  await loadLayout("empresas");
  try {
    clientes = await clientesApi.list();
  } catch {
    clientes = [];
  }
  await loadData();
}

document.addEventListener("DOMContentLoaded", main);


