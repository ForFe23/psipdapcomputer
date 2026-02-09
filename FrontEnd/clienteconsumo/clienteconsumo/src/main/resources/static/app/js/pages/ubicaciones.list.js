import { loadLayout } from "../ui/render.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let clientes = [];
let empresas = [];

function badgeEstado(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ACT")) return "badge-success";
  return "badge-secondary";
}

function row(u) {
  const cliente = clientes.find((c) => String(c.id) === String(u.clienteId));
  const empresa = empresas.find((c) => String(c.id) === String(u.empresaId));
  const clienteTxt = cliente ? cliente.nombre || cliente.razonSocial || cliente.id : u.clienteId;
  const empresaTxt = empresa ? empresa.nombre || empresa.razonSocial || empresa.id : u.empresaId;
  return `
    <tr>
      <td>${u.id ?? ""}</td>
      <td>${clienteTxt ?? ""}</td>
      <td>${empresaTxt ?? ""}</td>
      <td>${u.nombre ?? ""}</td>
      <td>${u.direccion ?? ""}</td>
      <td><span class="badge ${badgeEstado(u.estado)}">${u.estado ?? ""}</span></td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${u.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${u.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

async function populateClientesEmpresas() {
  try {
    clientes = await clientesApi.list();
    const selCliente = document.getElementById("fCliente");
    const selEmpresa = document.getElementById("fEmpresa");
    if (selCliente) {
      selCliente.innerHTML = `<option value=\"\">Todos</option>` + clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
    }
    empresas = await empresasApi.list();
    if (selEmpresa) {
      selEmpresa.innerHTML = `<option value=\"\">Todas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
    }
  } catch (err) {
    clientes = [];
    empresas = [];
  }
}

async function loadData(filters) {
  const tbody = document.querySelector("#ubicaciones-tbody");
  try {
    const data = await ubicacionesApi.list(filters);
    const filtrado = data.filter((u) => {
      if (filters && filters.estado && filters.estado !== u.estado) return false;
      return true;
    }).sort((a, b) => {
      const eA = empresas.find((c) => String(c.id) === String(a.empresaId));
      const eB = empresas.find((c) => String(c.id) === String(b.empresaId));
      const nameA = (eA?.nombre || "").toUpperCase();
      const nameB = (eB?.nombre || "").toUpperCase();
      if (nameA === nameB) return (a.nombre || "").localeCompare(b.nombre || "");
      return nameA.localeCompare(nameB);
    });
    tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="7" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        try {
          await ubicacionesApi.remove(btn.dataset.delete);
          showSuccess("Ubicación eliminada");
          await loadData(filters);
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
  await loadLayout("ubicaciones");
  await populateClientesEmpresas();
  await loadData();

  document.querySelector("#filtros-ubic").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const clienteId = ev.target.fCliente.value || undefined;
    const empresaId = ev.target.fEmpresa.value || undefined;
    const estado = ev.target.fEstado.value || undefined;
    await loadData({ clienteId, empresaId, estado });
  });

  document.addEventListener("ubicaciones:reload", async () => {
    await loadData();
  });
}

document.addEventListener("DOMContentLoaded", main);


