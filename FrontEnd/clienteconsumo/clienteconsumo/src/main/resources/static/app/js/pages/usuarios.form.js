import { loadLayout } from "../ui/render.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { rolesApi } from "../api/roles.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];
let clientes = [];
let rolesLoaded = false;
const rolesCache = {};
const INTERNAL_ROLE_CODE = "PERSONAL_INTERNO";
const ROLE_LABELS = {
  TRGRTNRS: "Administrador",
  CMPRSGRS: "Cliente (solo sus equipos)",
  BLTRCPLS: "Técnico (incidentes/mantenimientos)",
  ADMIN_GLOBAL: "Admin global (todos los clientes)",
  TECNICO_GLOBAL: "Técnico global"
};
const ALLOWED_ROLE_CODES = Object.keys(ROLE_LABELS);

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadEmpresas(select, clienteFilter) {
  try {
    if (!empresas.length) empresas = await empresasApi.list();
    const data = clienteFilter ? empresas.filter((e) => String(e.clienteId || e.idCliente) === String(clienteFilter)) : empresas;
    select.innerHTML =
      `<option value="">Todas las empresas del cliente</option>` +
      data.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin empresas</option>`;
  }
}

async function loadClientes(select) {
  try {
    clientes = await clientesApi.list();
    select.innerHTML = `<option value="">Seleccione cliente</option>` + clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin clientes</option>`;
  }
}

function seedRoles(select) {
  const opts = ALLOWED_ROLE_CODES.map((code) => `<option value="${code}" data-codigo="${code}">${ROLE_LABELS[code]}</option>`).join("");
  select.innerHTML = `<option value="">Seleccione rol</option>` + opts;
}

async function loadRoles(select) {
  seedRoles(select);
  rolesLoaded = true;
  try {
    const data = await rolesApi.list();
    data.forEach((r) => {
      const code = (r.codigo || r.id || "").toUpperCase();
      if (!ALLOWED_ROLE_CODES.includes(code) || r.id == null) return;
      rolesCache[code] = r.id;
      const opt = select.querySelector(`option[data-codigo="${code}"]`);
      if (opt) {
        opt.value = r.id;
        opt.dataset.id = r.id;
      }
    });
  } catch (err) {

  }
}

function lockToGlobalRoles(select) {
  Array.from(select.options).forEach((opt) => {
    const code = (opt.dataset.codigo || "").toUpperCase();
    const allow = code === "ADMIN_GLOBAL" || code === "TECNICO_GLOBAL" || opt.value === "";
    opt.hidden = !allow;
  });
  const visible = Array.from(select.options).find((o) => !o.hidden && o.value);
  if (visible) select.value = visible.value;
}

function unlockRoles(select) {
  Array.from(select.options).forEach((opt) => (opt.hidden = false));
  select.value = "";
}

async function loadUsuario(id, form) {
  const data = await usuariosApi.getById(id);
  form.clienteId.value = data.idCliente ?? "";
  await loadEmpresas(form.empresaId, form.clienteId.value || null);
  form.empresaId.value = data.empresaId ?? "";
  const rolVal = data.rolId ?? data.rolCodigo ?? "";
  const matchById = Array.from(form.rolId.options).find((opt) => String(opt.value) === String(rolVal));
  const matchByCode = Array.from(form.rolId.options).find((opt) => (opt.dataset.codigo || "").toUpperCase() === (data.rolCodigo || "").toUpperCase());
  if (matchById) {
    form.rolId.value = matchById.value;
  } else if (matchByCode) {
    form.rolId.value = matchByCode.value;
  } else {
    form.rolId.value = "";
  }
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
  await Promise.all([loadClientes(form.clienteId), loadEmpresas(form.empresaId), loadRoles(form.rolId)]);
  const id = getId();
  const dapClient = () => clientes.find((c) => (c.nombre || "").toUpperCase().includes("DAPCOM"));
  form.clienteId.addEventListener("change", async () => {
    const selected = form.clienteId.value;
    const dap = dapClient();
    const isDap = dap && String(dap.id) === String(selected);
    await loadEmpresas(form.empresaId, isDap ? dap.id : selected || null);
    if (isDap) {
      const dapEmpresa = empresas.find((e) => String(e.clienteId || e.idCliente) === String(dap.id) && (e.nombre || "").toUpperCase().includes("DAPCOM"));
      if (dapEmpresa) form.empresaId.value = dapEmpresa.id;
      lockToGlobalRoles(form.rolId);
    } else {
      unlockRoles(form.rolId);
    }
  });
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
  form.clienteId.addEventListener("change", () => {
    const clienteSel = form.clienteId.value || null;
    loadEmpresas(form.empresaId, clienteSel);
  });

  async function hashSecret(secret) {
    if (!secret) return null;
    const enc = new TextEncoder().encode(secret);
    const digest = await crypto.subtle.digest("SHA-256", enc);
    const bytes = Array.from(new Uint8Array(digest));
    return bytes.map((b) => b.toString(16).padStart(2, "0")).join("");
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    aplicarGenerico();
    const rolOption = form.rolId.options[form.rolId.selectedIndex];
    if (!rolOption || !rolOption.dataset.codigo) {
      showError("Selecciona un rol válido");
      return;
    }
    const codigoRol = rolOption.dataset.codigo;
    let rolIdNum = Number(form.rolId.value);
    if (!Number.isFinite(rolIdNum)) {
      rolIdNum = rolesCache[codigoRol] || null;
    }
    if (!form.clienteId.value) {
      showError("Selecciona un cliente");
      return;
    }
    const clienteId = form.clienteId.value ? Number(form.clienteId.value) : null;
    const empresaSeleccionada = empresas.find((e) => String(e.id) === String(form.empresaId.value || ""));
    const empresaId = form.empresaId.value ? Number(form.empresaId.value) : null;
    const payload = {
      idCliente: clienteId,
      empresaId,
      rolId: rolIdNum,
      rolCodigo: codigoRol || (genericoCheck?.checked ? INTERNAL_ROLE_CODE : undefined),
      estatus: form.estatus.value,
      cedula: form.cedula.value.trim(),
      apellidos: form.apellidos.value.trim(),
      nombres: form.nombres.value.trim(),
      correo: form.correo.value.trim() || null,
      telefono: form.telefono.value.trim() || null,
      solfrnrf: await hashSecret(form.solfrnrf.value.trim())
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


