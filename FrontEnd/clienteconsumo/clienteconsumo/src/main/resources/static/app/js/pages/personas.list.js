import { loadLayout } from "../ui/render.js";
import { personasApi } from "../api/personas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];

function badgeEstado(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ACT")) return "badge-success";
  return "badge-secondary";
}

function row(p) {
  const empresa = empresas.find((c) => String(c.id) === String(p.empresaId));
  const empresaTxt = empresa ? empresa.nombre || empresa.razonSocial || empresa.id : p.empresaId;
  return `
    <tr>
      <td>${p.id ?? ""}</td>
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

async function loadEmpresas() {
  try {
    empresas = await clientesApi.list();
    const sel = document.getElementById("fEmpresa");
    if (sel) sel.innerHTML = `<option value="">Todas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    empresas = [];
  }
}

async function loadData(filters = {}) {
  const tbody = document.querySelector("#personas-tbody");
  try {
    const data = await personasApi.list(filters.empresaId ? { empresaId: filters.empresaId } : undefined);
    const filtrado = data.filter((p) => {
      if (filters.cargo && filters.cargo !== p.cargo) return false;
      return true;
    });
    tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="7" class="text-center text-muted">Sin datos</td></tr>`;
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
  await loadLayout("personas");
  await loadEmpresas();
  await loadData();

  document.querySelector("#filtros-personas").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const empresaId = ev.target.fEmpresa.value || undefined;
    const cargo = ev.target.fCargo.value || undefined;
    await loadData({ empresaId, cargo });
  });

  document.addEventListener("personas:reload", async () => {
    await loadData();
  });
}

document.addEventListener("DOMContentLoaded", main);
