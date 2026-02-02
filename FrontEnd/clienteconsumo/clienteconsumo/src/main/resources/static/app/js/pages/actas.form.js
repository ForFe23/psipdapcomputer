import { loadLayout } from "../ui/render.js";
import { actasApi } from "../api/actas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { personasApi } from "../api/personas.api.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

let ubicaciones = [];
const cargosGenericos = [
  "Administrador",
  "Analista",
  "Analista Senior",
  "Analista Junior",
  "Arquitecto TI",
  "Asesor Comercial",
  "Asistente Administrativo",
  "Asistente Contable",
  "Asistente de Bodega",
  "Asistente de Compras",
  "Asistente de Logistica",
  "Asistente de Marketing",
  "Asistente de Operaciones",
  "Asistente de RRHH",
  "Auditor Interno",
  "Auxiliar Administrativo",
  "Auxiliar de Limpieza",
  "Auxiliar de Sistemas",
  "Bodeguero",
  "Cajero",
  "Capacitador",
  "Chofer",
  "Cliente",
  "Coordinador Comercial",
  "Coordinador de Calidad",
  "Coordinador de Logistica",
  "Coordinador de Operaciones",
  "Coordinador de Proyectos",
  "Coordinador de RRHH",
  "Coordinador de Soporte",
  "Despachador",
  "Director Comercial",
  "Director Financiero",
  "Director General",
  "Director Operativo",
  "Encargado de Almacen",
  "Encargado de Compras",
  "Encargado de Inventarios",
  "Encargado de Mantenimiento",
  "Especialista BI",
  "Especialista Cloud",
  "Especialista DevOps",
  "Especialista Redes",
  "Especialista Seguridad",
  "Especialista Soporte",
  "Gerente Administrativo",
  "Gerente Comercial",
  "Gerente de Almacen",
  "Gerente de Calidad",
  "Gerente de Infraestructura",
  "Gerente de Logistica",
  "Gerente de Operaciones",
  "Gerente de Producto",
  "Gerente de Proyectos",
  "Gerente de RRHH",
  "Gerente Financiero",
  "Gerente General",
  "Ingeniero Campo",
  "Ingeniero de Datos",
  "Ingeniero de Redes",
  "Ingeniero de Soporte",
  "Ingeniero DevOps",
  "Ingeniero Preventa",
  "Jefe Comercial",
  "Jefe de Almacen",
  "Jefe de Calidad",
  "Jefe de Compras",
  "Jefe de Infraestructura",
  "Jefe de Logistica",
  "Jefe de Mantenimiento",
  "Jefe de Operaciones",
  "Jefe de Proyectos",
  "Jefe de RRHH",
  "Lider de Equipo",
  "Mensajero",
  "Operador",
  "Operador de Monitoreo",
  "Practicante",
  "Product Owner",
  "Programador",
  "Recepcionista",
  "Representante de Ventas",
  "Responsable de Cuenta",
  "Seguridad Fisica",
  "Soporte Nivel 1",
  "Soporte Nivel 2",
  "Soporte Nivel 3",
  "Supervisor",
  "Supervisor de Almacen",
  "Supervisor de Campo",
  "Supervisor de Operaciones",
  "Supervisor de Produccion",
  "Tecnico Campo",
  "Tecnico de Mantenimiento",
  "Tecnico de Redes",
  "Tecnico de Soporte",
  "Tester QA",
  "Tesorero",
  "Usuario Interno",
  "Ventas Internas",
  "Vicepresidente Comercial",
  "Vicepresidente Operaciones",
  "Visitador"
];
let personasUsuarios = [];
let personasUsuariosList;

function renderCargos() {
  const dl = document.querySelector("#cargos-list");
  if (!dl) return;
  dl.innerHTML = cargosGenericos.map((c) => `<option value="${c}">`).join("");
}

async function loadActa(id, form) {
  const data = await actasApi.getById(id);
  form.clienteId.value = data.empresaId ?? data.idCliente ?? data.clienteId ?? "";
  form.entregadoPor.value = data.entregadoPor ?? data.emisor ?? "";
  form.recibidoPor.value = data.recibidoPor ?? data.receptor ?? "";
  if (form.cargoEntrega) form.cargoEntrega.value = data.cargoEntrega ?? "";
  if (form.cargoRecibe) form.cargoRecibe.value = data.cargoRecibe ?? "";
  form.ubicacion.value = data.ubicacionDestino ?? data.ubicacion ?? "";
  if (form.ubicacionId) {
    form.ubicacionId.dataset.pendingValue = data.ubicacionId ?? "";
  }
  form.observaciones.value = data.observaciones ?? "";
  form.estado.value = data.estado ?? "";
  form.fechaActa.value = data.fechaActa ?? data.fecha ?? "";
  form.tema.value = data.tema ?? "";
  form.entregadoPor.value = data.entregadoPor ?? "";
  form.recibidoPor.value = data.recibidoPor ?? "";
  if (data.equipoSerie && form.serieEquipo) form.serieEquipo.value = data.equipoSerie;
  if (data.idEquipo && form.equipoId) form.equipoId.value = data.idEquipo;
  return data;
}

async function loadUbicacionesByEmpresa(empresaId, select, afterRender) {
  if (!select) return;
  if (!empresaId) {
    ubicaciones = [];
    select.innerHTML = `<option value=\"\">Seleccione ubicación</option>`;
    if (afterRender) afterRender();
    return;
  }
  try {
    ubicaciones = await ubicacionesApi.listByEmpresa(empresaId);
    select.innerHTML = `<option value=\"\">Seleccione ubicación del cliente</option>` + ubicaciones.map((u) => `<option value=\"${u.id}\">${u.nombre || u.id}</option>`).join("");
    if (afterRender) afterRender();
  } catch (err) {
    select.innerHTML = `<option value=\"\">Sin ubicaciones</option>`;
    showError("No se pudieron cargar ubicaciones: " + err.message);
  }
}

async function main() {
  await loadLayout("actas");
  renderCargos();
  const form = document.querySelector("#acta-form");
  const clienteSelect = document.querySelector("#clienteSelect");
  const serieInput = document.querySelector("#serieEquipo");
  const seriesList = document.querySelector("#seriesList");
  const equipoHidden = document.querySelector("#equipoId");
  const ubicacionSelect = document.querySelector("#ubicacionId");
  const ubicacionInput = document.querySelector("#ubicacion");
  personasUsuariosList = document.querySelector("#personas-usuarios-datalist");
  const entregadoPorInput = document.querySelector("#entregadoPor");
  const recibidoPorInput = document.querySelector("#recibidoPor");
  const personaInputs = [entregadoPorInput, recibidoPorInput];
  let equipos = [];
  const syncUbicacionInput = () => {
    if (!ubicacionSelect || !ubicacionInput) return;
    const opt = ubicacionSelect.selectedOptions[0];
    if (opt && opt.value) {
      ubicacionInput.value = opt.textContent;
    } else {
      ubicacionInput.value = "";
    }
  };

  const loadPersonasUsuarios = async (empresaId) => {
    if (!empresaId) {
      personasUsuarios = [];
      if (personasUsuariosList) personasUsuariosList.innerHTML = "";
      personaInputs.forEach((i) => i && (i.value = ""));
      return;
    }
    try {
      const [pers, usrs] = await Promise.all([
        personasApi.listByEmpresa(empresaId),
        usuariosApi.listByEmpresa(empresaId)
      ]);
      personasUsuarios = [
        ...pers.map((p) => ({ id: p.id, label: `${p.nombres || ""} ${p.apellidos || ""}`.trim() })),
        ...usrs.map((u) => ({ id: u.id, label: `${u.nombres || ""} ${u.apellidos || ""}`.trim() }))
      ].filter((x) => x.label);
      if (personasUsuariosList) {
        personasUsuariosList.innerHTML = personasUsuarios
          .map((p) => `<option value="${p.label}" data-id="${p.id}">${p.label}</option>`)
          .join("");
      }
      personaInputs.forEach((i) => i && personaInputs && (i.placeholder = "Selecciona o escribe nombre"));
    } catch (err) {
      if (personasUsuariosList) personasUsuariosList.innerHTML = "";
    }
  };

  const loadClientes = async () => {
    try {
      const empresas = await empresasApi.list();
      clienteSelect.innerHTML =
        `<option value=\"\">Seleccione empresa</option>` +
        empresas.map((e) => `<option value=\"${e.id}\">${e.nombre || e.id}</option>`).join("");
    } catch (err) {
      showError("No se pudieron cargar las empresas");
    }
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      showError("No se pudieron cargar equipos");
    }
  };

  const renderSeries = (clienteId) => {
    if (!seriesList) return;
    const filtered = clienteId ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(clienteId)) : equipos;
    const sugerencia = `<option value=\"⬇SUGERENCIAS⬇\"></option>`;
    seriesList.innerHTML =
      sugerencia +
      filtered
        .map((e) => {
          const serie = (e.serie || e.serieEquipo || "").trim();
          return serie ? `<option value=\"${serie}\">${serie}</option>` : "";
        })
        .join("");
  };

  const syncClienteFromSerie = (serieVal) => {
    if (!serieVal) {
      if (equipoHidden) equipoHidden.value = "";
      return;
    }
    const eq = equipos.find((e) => (e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
    if (eq) {
      if (clienteSelect) clienteSelect.value = eq.empresaId ?? eq.idCliente ?? "";
      if (equipoHidden) equipoHidden.value = eq.id ?? "";
      renderSeries(eq.empresaId ?? eq.idCliente);
      loadUbicacionesByEmpresa(eq.empresaId ?? eq.idCliente, ubicacionSelect, syncUbicacionInput);
    } else if (equipoHidden) {
      equipoHidden.value = "";
    }
  };

  await Promise.all([loadClientes(), loadEquipos()]);
  renderSeries("");
  await loadUbicacionesByEmpresa(clienteSelect?.value || "", ubicacionSelect, () => {
    const pending = ubicacionSelect?.dataset.pendingValue;
    if (pending) {
      ubicacionSelect.value = pending;
      syncUbicacionInput();
    }
  });
  await loadPersonasUsuarios(clienteSelect?.value || "");

  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar acta";
    try {
      const data = await loadActa(id, form);
      await loadUbicacionesByEmpresa(form.clienteId.value || data?.empresaId, ubicacionSelect, () => {
        if (ubicacionSelect && ubicacionSelect.dataset.pendingValue) {
          ubicacionSelect.value = ubicacionSelect.dataset.pendingValue;
          syncUbicacionInput();
        }
      });
      await loadPersonasUsuarios(form.clienteId.value || data?.empresaId);
    } catch (err) {
      showError(err.message);
    }
  }

  if (serieInput) {
    serieInput.addEventListener("input", () => syncClienteFromSerie(serieInput.value));
  }
  if (clienteSelect) {
    clienteSelect.addEventListener("change", () => {
      renderSeries(clienteSelect.value);
      loadUbicacionesByEmpresa(clienteSelect.value, ubicacionSelect, syncUbicacionInput);
      loadPersonasUsuarios(clienteSelect.value);
    });
  }

  const btnFechaHoy = document.querySelector("#btnFechaHoyActa");
  if (btnFechaHoy && form.fechaActa) {
    btnFechaHoy.addEventListener("click", () => {
      const today = new Date().toISOString().slice(0, 10);
      form.fechaActa.value = today;
    });
  }
  if (ubicacionSelect) {
    ubicacionSelect.addEventListener("change", syncUbicacionInput);
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const ubicacionTexto =
      (form.ubicacion?.value || "").trim() ||
      (form.ubicacionId?.selectedOptions?.[0]?.textContent || "");
    const payload = {
      empresaId: form.clienteId.value.trim(),
      clienteId: form.clienteId.value.trim(),
      idCliente: form.clienteId.value.trim(),
      idEquipo: form.equipoId.value.trim() || null,
      equipoSerie: form.serieEquipo.value.trim(),
      entregadoPor: entregadoPorInput.value.trim(),
      recibidoPor: recibidoPorInput.value.trim(),
      cargoEntrega: form.cargoEntrega?.value.trim() || "",
      cargoRecibe: form.cargoRecibe?.value.trim() || "",
      ubicacionId: form.ubicacionId?.value ? Number(form.ubicacionId.value) : null,
      ubicacionDestino: ubicacionTexto,
      observaciones: form.observaciones.value.trim(),
      estado: form.estado.value.trim(),
      fechaActa: form.fechaActa.value,
      tema: form.tema.value.trim(),
      entregadoPor: form.entregadoPor.value.trim(),
      recibidoPor: form.recibidoPor.value.trim()
    };
    try {
      if (id) {
        await actasApi.update(id, payload);
        showSuccess("Acta actualizada");
      } else {
        await actasApi.create(payload);
        showSuccess("Acta creada");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
