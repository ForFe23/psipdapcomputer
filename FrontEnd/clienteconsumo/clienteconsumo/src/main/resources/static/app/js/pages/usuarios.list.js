import { loadLayout } from "../ui/render.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { rolesApi } from "../api/roles.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];
let roles = [];

function badgeEstatus(e) {
  const val = (e || "").toUpperCase();
  if (val === "ACTIVO") return "badge-success";
  if (val === "INACTIVO") return "badge-secondary";
  return "badge-light";
}

function lookupEmpresa(id) {
  const c = empresas.find((cl) => String(cl.id) === String(id));
  return c ? c.nombre || c.razonSocial || c.id : id;
}

function lookupRol(id) {
  const r = roles.find((ro) => String(ro.id) === String(id));
  return r ? r.nombre || r.codigo || r.id : id;
}

function row(u) {
  return `
    <tr>
      <td>${u.id ?? ""}</td>
      <td>${lookupEmpresa(u.empresaId ?? u.idCliente)}</td>
      <td>${[u.apellidos, u.nombres].filter(Boolean).join(" ")}</td>
      <td>${lookupRol(u.rolId)}</td>
      <td><span class="badge ${badgeEstatus(u.estatus)}">${u.estatus ?? ""}</span></td>
      <td>${u.correo ?? ""}</td>
      <td class="text-right">
        <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${u.id}">Editar</a>
        <button class="btn btn-sm btn-outline-danger" data-delete="${u.id}">Eliminar</button>
      </td>
    </tr>
  `;
}

async function loadCatalogos() {
  try {
    empresas = await empresasApi.list();
    const sel = document.getElementById("fEmpresa");
    if (sel) sel.innerHTML = `<option value="">Todas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    empresas = [];
  }
  try {
    roles = await rolesApi.list();
  } catch (err) {
    roles = [];
  }
}

async function loadData(filters = {}) {
  const tbody = document.querySelector("#usuarios-tbody");
  try {
    const data = await usuariosApi.list();
    const filtrado = data.filter((u) => {
      if (filters.empresaId && String(u.empresaId ?? u.idCliente) !== String(filters.empresaId)) return false;
      if (filters.estatus && filters.estatus !== u.estatus) return false;
      return true;
    });
    tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="7" class="text-center text-muted">Sin datos</td></tr>`;
    tbody.querySelectorAll("[data-delete]").forEach((btn) => {
      btn.addEventListener("click", async () => {
        try {
          await usuariosApi.remove(btn.dataset.delete);
          showSuccess("Usuario eliminado");
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
  await loadLayout("usuarios");
  await loadCatalogos();
  await loadData();

  document.querySelector("#filtros-usuarios").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const empresaId = ev.target.fEmpresa.value || undefined;
    const estatus = ev.target.fEstatus.value || undefined;
    await loadData({ empresaId, estatus });
  });

  document.addEventListener("usuarios:reload", async () => {
    await loadData();
  });
}

document.addEventListener("DOMContentLoaded", main);
