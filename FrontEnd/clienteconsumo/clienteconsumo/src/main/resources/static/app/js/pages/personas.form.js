import { loadLayout } from "../ui/render.js";
import { personasApi } from "../api/personas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INTERNAL_CARGO = "PERSONA_EMPRESA";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

async function loadEmpresas(select) {
  try {
    const data = await empresasApi.list();
    select.innerHTML = `<option value="">Seleccione empresa</option>` + data.map((e) => `<option value="${e.id}">${e.nombre || e.id}</option>`).join("");
  } catch (err) {
    select.innerHTML = `<option value="">Sin empresas</option>`;
  }
}

async function loadPersona(id, form) {
  const data = await personasApi.getById(id);
  form.empresaId.value = data.empresaId ?? "";
  form.cedula.value = data.cedula ?? "";
  form.apellidos.value = data.apellidos ?? "";
  form.nombres.value = data.nombres ?? "";
  form.correo.value = data.correo ?? "";
  form.telefono.value = data.telefono ?? "";
  form.cargo.value = data.cargo ?? "TRGRTNRS";
}

async function main() {
  await loadLayout("personas");
  const form = document.querySelector("#persona-form");
  const genericaCheck = form.querySelector("#esGenerica");
  await loadEmpresas(form.empresaId);
  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar persona";
    try {
      await loadPersona(id, form);
    } catch (err) {
      showError(err.message);
    }
  }

  const aplicarGenerica = () => {
    if (!genericaCheck || !genericaCheck.checked) return;
    if (!form.cedula.value.trim()) {
      form.cedula.value = `GEN-${form.empresaId.value || "0"}`;
    }
    form.apellidos.value = "PERSONAL";
    form.nombres.value = "INTERNO GENERICO";
    form.cargo.value = INTERNAL_CARGO;
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
      empresaId: form.empresaId.value,
      cedula: form.cedula.value.trim(),
      apellidos: form.apellidos.value.trim(),
      nombres: form.nombres.value.trim(),
      correo: form.correo.value.trim() || null,
      telefono: form.telefono.value.trim() || null,
      cargo: form.cargo.value || INTERNAL_CARGO,
      estadoInterno: "ACTIVO_INTERNAL"
    };
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
