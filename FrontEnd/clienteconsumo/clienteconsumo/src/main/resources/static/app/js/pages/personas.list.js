import { loadLayout } from "../ui/render.js";
import { personasApi } from "../api/personas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";
import { enforceRole, getAccessScope } from "../auth.js";

let clientes = [];
let empresas = [];
let scope = null;

function badgeEstado(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ACT")) return "badge-success";
  return "badge-secondary";
}

function matchesScope(p) {
  if (!scope) return true;
  const clienteVal = p.clienteId ?? p.idCliente;
  const empresaVal = p.empresaId;
  if (scope.clienteId && clienteVal && String(clienteVal) !== String(scope.clienteId)) return false;
  if (scope.empresaId && empresaVal && String(empresaVal) !== String(scope.empresaId)) return false;
  return true;
}

function row(p) {
  const cliente = clientes.find((c) => String(c.id) === String(p.clienteId));
  const empresa = empresas.find((e) => String(e.id) === String(p.empresaId));
  const empresaTxt = empresa ? empresa.nombre || empresa.razonSocial || empresa.id : p.empresaId || "";
  const clienteTxt = cliente ? cliente.nombre || cliente.razonSocial || cliente.id : p.clienteId;
  return `
    <tr>
      <td>${p.id ?? ""}</td>
      <td>${clienteTxt ?? ""}</td>
      <td>${empresaTxt ?? ""}</td>
      <td>${p.cedula ?? ""}</td>
      <td>${[p.apellidos, p.nombres].filter(Boolean).join(" ")}</td>
      <td>${p.cargo ?? ""}</td>
      <td><span class="badge ${badgeEstado(p.estadoInterno)}">${p.estadoInterno ?? ""}</span></td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${p.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${p.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

async function loadClientesEmpresas() {
  try {
    clientes = await clientesApi.list();
    empresas = await empresasApi.list();
    const selCliente = document.getElementById("fCliente");
    const selEmpresa = document.getElementById("fEmpresa");
    if (selCliente) {
      selCliente.innerHTML = `<option value=\"\">Todos</option>` + clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      if (scope?.clienteId) {
        selCliente.value = scope.clienteId;
        selCliente.disabled = true;
      }
    }
    if (selEmpresa) {
      selEmpresa.innerHTML = `<option value=\"\">Todas</option>` + empresas.map((e) => `<option value="${e.id}">${e.nombre || e.id}</option>`).join("");
      if (scope?.empresaId) {
        selEmpresa.value = scope.empresaId;
        selEmpresa.disabled = true;
      }
    }
  } catch (err) {
    clientes = [];
    empresas = [];
  }
}

async function loadCargosDatalist() {
  const sel = document.getElementById("fCargo");
  if (!sel) return;
  try {
    const data = await personasApi.list(scope?.clienteId ? { clienteId: scope.clienteId } : {});
    const cargos = [...new Set(data.map((p) => p.cargo).filter(Boolean))];
    sel.innerHTML = `<option value=\"\">Todos</option>` + cargos.map((c) => `<option value="${c}">${c}</option>`).join("");
  } catch (err) {
    sel.innerHTML = `<option value=\"\">Todos</option>`;
  }
}

async function loadData(filters = {}) {
  const tbody = document.querySelector("#personas-tbody");
  try {
    const data = await personasApi.list(filters);
    const filtrado = data.filter((p) => {
      if (filters.cargo && filters.cargo !== p.cargo) return false;
      return matchesScope(p);
    });
    tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="8" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        try {
          await personasApi.remove(btn.dataset.delete);
          showSuccess("Persona eliminada");
          await loadData(filters);
        } catch (err) {
          showError(err.message);
        }
      });
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">${err.message}</td></tr>`;
  }
}

async function main() {
  enforceRole(["ADMIN_GLOBAL", "TECNICO_GLOBAL", "TECNICO_CLIENTE", "CLIENTE_ADMIN"]);
  scope = getAccessScope();
  await loadLayout("personas");
  await Promise.all([loadClientesEmpresas(), loadCargosDatalist()]);
  await loadData();

  document.querySelector("#filtros-personas").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const clienteId = ev.target.fCliente?.value || undefined;
    const empresaId = ev.target.fEmpresa.value || undefined;
    const cargo = ev.target.fCargo.value || undefined;
    await loadData({ clienteId, empresaId, cargo });
  });

  document.addEventListener("personas:reload", async () => {
    await loadData();
  });
}

document.addEventListener("DOMContentLoaded", main);


