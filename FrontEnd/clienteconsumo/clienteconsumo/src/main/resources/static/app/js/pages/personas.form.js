import { loadLayout } from "../ui/render.js";
import { personasApi } from "../api/personas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";
import { enforceRole, getAccessScope } from "../auth.js";

const DEFAULT_CARGO = "PERSONAL_INTERNO";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadClientes(select) {
  try {
    const data = await clientesApi.list();
    select.innerHTML = `<option value="">Seleccione cliente</option>` + data.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
  } catch {
    select.innerHTML = `<option value=\"\">Sin clientes</option>`;
  }
}

async function loadEmpresas(select, clienteId) {
  try {
    const data = clienteId ? await empresasApi.listByCliente(clienteId) : await empresasApi.list();
    select.innerHTML = `<option value="">Seleccione empresa (opcional)</option>` + data.map((e) => `<option value="${e.id}">${e.nombre || e.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin empresas</option>`;
  }
}

async function loadCargos(select, clienteId) {
  try {
    const data = await personasApi.list(clienteId ? { clienteId } : {});
    const cargos = [...new Set(data.map((p) => p.cargo).filter(Boolean))];
    select.innerHTML = cargos.map((c) => `<option value="${c}">`).join("");
  } catch (err) {
    select.innerHTML = "";
  }
}

async function loadPersona(id, form) {
  const data = await personasApi.getById(id);
  form.clienteId.value = data.clienteId ?? "";
  await loadEmpresas(form.empresaId, data.clienteId ?? "");
  form.empresaId.value = data.empresaId ?? "";
  form.cedula.value = data.cedula ?? "";
  form.apellidos.value = data.apellidos ?? "";
  form.nombres.value = data.nombres ?? "";
  form.correo.value = data.correo ?? "";
  form.telefono.value = data.telefono ?? "";
  form.cargo.value = data.cargo ?? "";
}

async function main() {
  enforceRole(["ADMIN_GLOBAL", "TECNICO_GLOBAL", "TECNICO_CLIENTE", "CLIENTE_ADMIN"]);
  const scope = getAccessScope();
  await loadLayout("personas");
  const form = document.querySelector("#persona-form");
  const genericaCheck = form.querySelector("#esGenerica");
  await loadClientes(form.clienteId);
  const cargoDatalist = document.querySelector("#cargo-datalist");
  await loadCargos(cargoDatalist, scope?.clienteId || "");
  const params = new URLSearchParams(window.location.search);
  const clienteParam = params.get("clienteId") || scope?.clienteId || "";
  const empresaParam = params.get("empresaId") || "";
  if (clienteParam) {
    form.clienteId.value = clienteParam;
    form.clienteId.readOnly = true;
  }
  await loadEmpresas(form.empresaId, form.clienteId.value || "");
  await loadCargos(cargoDatalist, form.clienteId.value || scope?.clienteId || "");
  if (empresaParam) {
    form.empresaId.value = empresaParam;
    form.empresaId.readOnly = true;
  }
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar persona";
    try {
      await loadPersona(id, form);
      await loadCargos(cargoDatalist, form.clienteId.value || scope?.clienteId || "");
    } catch (err) {
      showError(err.message);
    }
  }
  if (form.clienteId) {
    form.clienteId.addEventListener("change", () => {
      loadEmpresas(form.empresaId, form.clienteId.value);
      loadCargos(cargoDatalist, form.clienteId.value || "");
      if (!form.clienteId.value) {
        form.empresaId.value = "";
      }
    });
  }

  const aplicarGenerica = () => {
    if (!genericaCheck || !genericaCheck.checked) return;
    if (!form.cedula.value.trim()) {
      form.cedula.value = `GEN-${form.empresaId.value || "0"}`;
    }
    form.apellidos.value = "PERSONAL";
    form.nombres.value = "INTERNO GENERICO";
    form.cargo.value = DEFAULT_CARGO;
  };

  if (genericaCheck) {
    genericaCheck.addEventListener("change", () => {
      if (genericaCheck.checked) {
        aplicarGenerica();
      }
    });
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    aplicarGenerica();
    const payload = {
      clienteId: form.clienteId.value || null,
      empresaId: form.empresaId.value || null,
      cedula: form.cedula.value.trim(),
      apellidos: form.apellidos.value.trim(),
      nombres: form.nombres.value.trim(),
      correo: form.correo.value.trim() || null,
      telefono: form.telefono.value.trim() || null,
      cargo: form.cargo.value.trim() || DEFAULT_CARGO,
      estadoInterno: "ACTIVO_INTERNAL"
    };
    if (!payload.clienteId && !payload.empresaId) {
      showError("Selecciona cliente o empresa.");
      return;
    }
    try {
      if (id) {
        await personasApi.update(id, payload);
        showSuccess("Persona actualizada");
      } else {
        await personasApi.create(payload);
        showSuccess("Persona creada");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);


