import { loadLayout } from "../ui/render.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";
import { clientesApi } from "../api/clientes.api.js";

const ACTIVO_INTERNAL = "ACTIVO_INTERNAL";

let estadoInternoActual = ACTIVO_INTERNAL;

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

function showStep(step) {
  document.querySelectorAll(".wizard-step").forEach((s) => {
    s.classList.toggle("active", s.dataset.step === String(step));
  });
}

async function loadEquipo(id, form) {
  const data = await equiposApi.getById(id);
  estadoInternoActual = data.estadoInterno || ACTIVO_INTERNAL;
  form.idCliente.value = data.idCliente ?? "";
  form.codigoInterno.value = data.codigoInterno ?? data.activo ?? "";
  form.serie.value = data.serie ?? "";
  form.tipo.value = data.tipo ?? "";
  form.marca.value = data.marca ?? "";
  form.modelo.value = data.modelo ?? "";
  if (form.nombreEquipo) form.nombreEquipo.value = data.nombreEquipo ?? data.nombre ?? "";
  if (form.alias) form.alias.value = data.alias ?? "";
  form.estado.value = (data.estado || data.status || "").toUpperCase() || "";
  form.procesador.value = data.procesador ?? "";
  form.memoria.value = data.memoria ?? "";
  form.disco.value = data.disco ?? "";
  form.ip.value = data.ip ?? "";
  form.so.value = data.sistemaOperativo ?? data.so ?? "";
  form.office.value = data.office ?? "";
  form.fechaCompra.value = data.fechaCompra ? String(data.fechaCompra).slice(0, 10) : "";
  form.costo.value = data.costo ?? "";
  form.factura.value = data.factura ?? "";
  form.proveedor.value = data.nombreProveedor ?? data.proveedor ?? "";
  if (form.direccionProveedor) form.direccionProveedor.value = data.direccionProveedor ?? "";
  form.telefonoProveedor.value = data.telefonoProveedor ?? "";
  form.correoProveedor.value = data.contactoProveedor ?? data.correoProveedor ?? "";
  if (form.notas) form.notas.value = data.notas ?? "";
  form.ciudad.value = data.ciudad ?? "";
  form.ubicacionUsuario.value = data.ubicacionUsuario ?? "";
  form.departamentoUsuario.value = data.departamentoUsuario ?? "";
  form.nombreUsuario.value = data.nombreUsuario ?? "";
  
  form.querySelectorAll("input[type='text'], input[type='email']").forEach((el) => {
    el.value = (el.value || "").toUpperCase();
  });
}

function bindWizard() {
  let step = 1;
  showStep(step);
  document.querySelectorAll("[data-next]").forEach((btn) =>
    btn.addEventListener("click", (ev) => {
      const current = ev.currentTarget.closest(".wizard-step");
      if (!validateStep(current)) return;
      step = Math.min(4, step + 1);
      showStep(step);
    })
  );
  document.querySelectorAll("[data-prev]").forEach((btn) =>
    btn.addEventListener("click", () => {
      step = Math.max(1, step - 1);
      showStep(step);
    })
  );
}

function validateStep(stepEl) {
  if (!stepEl) return true;
  const inputs = Array.from(stepEl.querySelectorAll("input, select"));
  const empty = inputs.filter((i) => !i.value.trim());
  if (!empty.length) return true;
  const names = empty.map((i) => i.name || i.id || "campo").join(", ");
  return window.confirm(`Estás omitiendo: ${names}. ¿Deseas continuar?`);
}

function applyUppercase(form) {
  const shouldUpper = (el) => {
    const t = (el.type || "").toLowerCase();
    return ["text", "search", "email", ""].includes(t);
  };
  const inputs = Array.from(form.querySelectorAll("input"));
  inputs.forEach((el) => {
    if (!shouldUpper(el)) return;
    el.value = el.value.toUpperCase();
    el.addEventListener("input", () => {
      const start = el.selectionStart;
      el.value = el.value.toUpperCase();
      if (start !== null) el.setSelectionRange(start, start);
    });
  });
}

async function main() {
  await loadLayout("equipos");
  bindWizard();
  const form = document.querySelector("#equipo-form");
  const clienteSelect = form?.idCliente;
  applyUppercase(form);

  const datalists = {
    tipos: document.querySelector("#tipos-datalist"),
    marcas: document.querySelector("#marcas-datalist"),
    modelos: document.querySelector("#modelos-datalist"),
    nombres: document.querySelector("#nombres-datalist"),
    alias: document.querySelector("#alias-datalist"),
    procesadores: document.querySelector("#procesadores-datalist"),
    memorias: document.querySelector("#memorias-datalist"),
    discos: document.querySelector("#discos-datalist"),
    so: document.querySelector("#so-datalist"),
    office: document.querySelector("#office-datalist"),
    usuarios: document.querySelector("#usuarios-datalist")
  };

  
  try {
    const clientes = await clientesApi.list();
    clienteSelect.innerHTML = `<option value=\"\">Seleccione cliente</option>` + clientes.map(c => `<option value=\"${c.id}\">${c.nombre ?? c.razonSocial ?? c.id}</option>`).join("");
  } catch (err) {
    showError("No se pudo cargar clientes: " + err.message);
  }

  
  try {
    const equipos = await equiposApi.list();
    const uniq = (arr) => Array.from(new Set(arr.filter(Boolean))).slice(0, 50);
    datalists.tipos.innerHTML = uniq(equipos.map(e => e.tipo || e.tipoEquipo)).map(v => `<option value="${v}">`).join("");
    datalists.marcas.innerHTML = uniq(equipos.map(e => e.marca || e.marcaEquipo)).map(v => `<option value="${v}">`).join("");
    datalists.modelos.innerHTML = uniq(equipos.map(e => e.modelo || e.modeloEquipo)).map(v => `<option value="${v}">`).join("");
    datalists.nombres.innerHTML = uniq(equipos.map(e => e.nombreEquipo || e.nombre)).map(v => `<option value="${v}">`).join("");
    datalists.alias.innerHTML = uniq(equipos.map(e => e.alias)).map(v => `<option value="${v}">`).join("");
    datalists.procesadores.innerHTML = uniq(equipos.map(e => e.procesador)).map(v => `<option value="${v}">`).join("");
    datalists.memorias.innerHTML = uniq(equipos.map(e => e.memoria)).map(v => `<option value="${v}">`).join("");
    datalists.discos.innerHTML = uniq(equipos.map(e => e.disco)).map(v => `<option value="${v}">`).join("");
    datalists.so.innerHTML = uniq(equipos.map(e => e.so || e.sistemaOperativo)).map(v => `<option value="${v}">`).join("");
    datalists.office.innerHTML = uniq(equipos.map(e => e.office)).map(v => `<option value="${v}">`).join("");
    datalists.usuarios.innerHTML = uniq(equipos.map(e => e.nombreUsuario)).map(v => `<option value="${v}">`).join("");
  } catch (err) {
    
  }

  const id = getId();
  if (id) {
    const titleEl = form.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar equipo";
    try {
      await loadEquipo(id, form);
    } catch (err) {
      showError(err.message);
    }
  }
  
  const btnFechaHoy = document.querySelector("#btnFechaHoy");
  if (btnFechaHoy) {
    btnFechaHoy.addEventListener("click", () => {
      const today = new Date();
      const iso = today.toISOString().slice(0, 10);
      form.fechaCompra.value = iso;
    });
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!form.idCliente.value) {
      showError("Selecciona un cliente (multiempresa).");
      showStep(1);
      return;
    }
    if (!form.serie.value.trim() || !form.tipo.value.trim() || !form.marca.value.trim()) {
      showError("Completa serie, tipo y marca.");
      return;
    }
    if (!validateStep(form.querySelector(".wizard-step.active"))) return;

    const v = (el) => (el && el.value ? el.value.trim() : "");
    const valOrEmpty = (el) => v(el);

    
    const payload = {
      idCliente: form.idCliente.value ? Number(form.idCliente.value) : null,
      codigoInterno: v(form.codigoInterno),
      activo: v(form.codigoInterno),
      serie: v(form.serie),
      tipo: v(form.tipo).toUpperCase(),
      marca: v(form.marca),
      modelo: v(form.modelo),
      estado: v(form.estado).toUpperCase(),
      estadoInterno: estadoInternoActual || ACTIVO_INTERNAL,
      nombre: valOrEmpty(form.nombreEquipo),
      alias: valOrEmpty(form.alias),
      procesador: valOrEmpty(form.procesador),
      memoria: valOrEmpty(form.memoria),
      disco: valOrEmpty(form.disco),
      ip: valOrEmpty(form.ip),
      sistemaOperativo: valOrEmpty(form.so),
      office: valOrEmpty(form.office),
      fechaCompra: v(form.fechaCompra) ? `${v(form.fechaCompra)}T00:00:00` : "",
      costo: valOrEmpty(form.costo),
      factura: valOrEmpty(form.factura),
      nombreProveedor: valOrEmpty(form.proveedor),
      direccionProveedor: valOrEmpty(form.direccionProveedor),
      telefonoProveedor: valOrEmpty(form.telefonoProveedor),
      contactoProveedor: valOrEmpty(form.correoProveedor),
      notas: valOrEmpty(form.notas),
      ciudad: valOrEmpty(form.ciudad),
      ubicacionUsuario: valOrEmpty(form.ubicacionUsuario),
      departamentoUsuario: valOrEmpty(form.departamentoUsuario),
      nombreUsuario: valOrEmpty(form.nombreUsuario)
    };
    Object.keys(payload).forEach((k) => payload[k] === undefined && delete payload[k]);

    try {
      if (id) {
        await equiposApi.update(id, payload);
        showSuccess("Equipo actualizado");
      } else {
        await equiposApi.create(payload);
        showSuccess("Equipo creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
