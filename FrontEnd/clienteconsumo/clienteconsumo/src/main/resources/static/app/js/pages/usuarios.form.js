import { loadLayout } from "../ui/render.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { rolesApi } from "../api/roles.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];
const INTERNAL_ROLE_CODE = "PERSONAL_INTERNO";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadEmpresas(select) {
  try {
    empresas = await empresasApi.list();
    select.innerHTML = `<option value="">Seleccione empresa</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin empresas</option>`;
  }
}

async function loadRoles(select) {
  try {
    const data = await rolesApi.list();
    select.innerHTML = `<option value="">Seleccione rol</option>` + data.map((r) => `<option value="${r.id}" data-codigo="${r.codigo}">${r.nombre || r.codigo || r.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin roles</option>`;
  }
}

async function loadUsuario(id, form) {
  const data = await usuariosApi.getById(id);
  form.empresaId.value = data.empresaId ?? data.idCliente ?? "";
  form.rolId.value = data.rolId ?? "";
  form.estatus.value = data.estatus ?? "ACTIVO";
  form.cedula.value = data.cedula ?? "";
  form.apellidos.value = data.apellidos ?? "";
  form.nombres.value = data.nombres ?? "";
  form.correo.value = data.correo ?? "";
  form.telefono.value = data.telefono ?? "";
  form.solfrnrf.value = data.solfrnrf ?? "";
}

async function main() {
  await loadLayout("usuarios");
  const form = document.querySelector("#usuario-form");
  const genericoCheck = form.querySelector("#esGenerico");
  await Promise.all([loadEmpresas(form.empresaId), loadRoles(form.rolId)]);
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar usuario";
    try {
      await loadUsuario(id, form);
    } catch (err) {
      showError(err.message);
    }
  }

  const aplicarGenerico = () => {
    if (!genericoCheck || !genericoCheck.checked) return;
    if (!form.cedula.value.trim()) {
      form.cedula.value = `USR-GEN-${form.empresaId.value || "0"}`;
    }
    form.apellidos.value = "USUARIO";
    form.nombres.value = "INTERNO GENERICO";
    form.estatus.value = "ACTIVO";
    const rolOption = Array.from(form.rolId.options).find((opt) => opt.dataset.codigo === INTERNAL_ROLE_CODE);
    if (rolOption) {
      form.rolId.value = rolOption.value;
    } else {
      form.rolId.value = "";
    }
  };

  if (genericoCheck) {
    genericoCheck.addEventListener("change", () => {
      if (genericoCheck.checked) {
        aplicarGenerico();
      }
    });
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    aplicarGenerico();
    const rolOption = form.rolId.options[form.rolId.selectedIndex];
    const empresaSeleccionada = empresas.find((e) => String(e.id) === String(form.empresaId.value || ""));
    const empresaId = form.empresaId.value ? Number(form.empresaId.value) : null;
    const clienteId = empresaSeleccionada?.clienteId ? Number(empresaSeleccionada.clienteId) : empresaId;
    const payload = {
      idCliente: clienteId,
      empresaId,
      rolId: form.rolId.value ? Number(form.rolId.value) : null,
      rolCodigo: (rolOption ? rolOption.dataset.codigo : undefined) || (genericoCheck?.checked ? INTERNAL_ROLE_CODE : undefined),
      estatus: form.estatus.value,
      cedula: form.cedula.value.trim(),
      apellidos: form.apellidos.value.trim(),
      nombres: form.nombres.value.trim(),
      correo: form.correo.value.trim() || null,
      telefono: form.telefono.value.trim() || null,
      solfrnrf: form.solfrnrf.value.trim() || null
    };
    try {
      if (id) {
        await usuariosApi.update(id, payload);
        showSuccess("Usuario actualizado");
      } else {
        await usuariosApi.create(payload);
        showSuccess("Usuario creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
