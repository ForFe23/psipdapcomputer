import { loadLayout } from "../ui/render.js";
import { mantenimientosApi } from "../api/mantenimientos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const getParam = (name) => new URL(window.location.href).searchParams.get(name) || "";
const toUpper = (val) => (val ? val.trim().toUpperCase() : "");

function isoLocalNow() {
  return new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
}

async function main() {
  await loadLayout("mantenimientos");
  const form = document.querySelector("#mant-form");
  const empresaSelect = document.querySelector("#empresaSelect");
  const seriesList = document.querySelector("#seriesList");
  const serieInput = form?.serieEquipo;
  const equipoHidden = document.querySelector("#equipoId");
  const estadoSelect = document.querySelector("#estadoSelect");
  const id = getParam("id");
  const serieParam = getParam("serie");
  const empresaParam = getParam("empresaId") || getParam("idCliente");
  const sugerenciaCards = document.querySelectorAll("[data-plan]");
  let empresaPreset = empresaParam || "";
  let mantenimientoData = null;

  
  const presetFreq = getParam("frecuencia");
  const presetDesc = getParam("desc");
  const presetEstado = getParam("estado");

  let equipos = [];
  let empresasCache = [];

  
  const loadEmpresas = async () => {
    try {
      const empresas = await empresasApi.list();
      empresasCache = empresas;
      if (empresaSelect) {
        empresaSelect.innerHTML =
          `<option value="">Seleccione empresa</option>` +
          empresas.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
        if (empresaPreset) empresaSelect.value = empresaPreset;
      }
      return empresas;
    } catch (err) {
      showError("No se pudo cargar empresas");
    }
    return [];
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      
    }
  };

  const renderSeries = (empresaId) => {
    if (!seriesList) return;
    const filtered = empresaId ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(empresaId)) : equipos;
    const sugerencia = `<option value="⬇SUGERENCIAS⬇"></option>`;
    seriesList.innerHTML = sugerencia + filtered
      .map((e) => {
        const serie = toUpper(e.serie || e.serieEquipo || "");
        return serie ? `<option value="${serie}">${serie}</option>` : "";
      })
      .join("");
  };

  const syncEmpresaFromSerie = (serieVal) => {
    if (!serieVal) {
      if (empresaSelect) empresaSelect.value = "";
      if (equipoHidden) equipoHidden.value = "";
      return;
    }
    const eq = equipos.find((e) => String(e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
    if (eq && empresaSelect) {
      empresaSelect.value = eq.empresaId ?? eq.idCliente ?? "";
      renderSeries(eq.empresaId ?? eq.idCliente);
      if (equipoHidden) equipoHidden.value = eq.id ?? "";
    } else if (equipoHidden) {
      equipoHidden.value = "";
    }
  };

  if (serieParam) form.serieEquipo.value = serieParam;
  form.fechaProgramada.value = isoLocalNow();
  if (presetFreq) form.frecuenciaDias.value = presetFreq;
  if (presetDesc) form.descripcion.value = presetDesc;
  if (estadoSelect) estadoSelect.value = (presetEstado || "PROGRAMADO").toUpperCase();
  if (!form.frecuenciaDias.value) form.frecuenciaDias.value = "0";
  const enforceEstadoPorFecha = () => {
    if (!form.fechaProgramada.value) return;
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    const fechaProg = new Date(form.fechaProgramada.value);
    fechaProg.setSeconds(0, 0);
    if (fechaProg < hoy) {
      if (estadoSelect) estadoSelect.value = "COMPLETADO";
      form.frecuenciaDias.value = "0";
    }
  };
  form.fechaProgramada.addEventListener("change", enforceEstadoPorFecha);
  enforceEstadoPorFecha();

  sugerenciaCards.forEach((card) => {
    card.addEventListener("click", (ev) => {
      ev.preventDefault();
      const freq = card.dataset.freq || "0";
      const estado = (card.dataset.estado || "PROGRAMADO").toUpperCase();
      const desc = card.dataset.desc || "";
      form.frecuenciaDias.value = freq;
      if (estadoSelect) estadoSelect.value = estado;
      if (desc) form.descripcion.value = desc;
      form.fechaProgramada.value = isoLocalNow();
      enforceEstadoPorFecha();
    });
  });

  if (id) {
    document.getElementById("form-title").textContent = "Editar mantenimiento";
    try {
      mantenimientoData = await mantenimientosApi.getById(id);
      empresaPreset = mantenimientoData.empresaId ?? mantenimientoData.idCliente ?? empresaPreset;
      if (serieInput) serieInput.value = toUpper(mantenimientoData.serieSnapshot || mantenimientoData.serieEquipo || serieInput.value);
      if (equipoHidden) equipoHidden.value = mantenimientoData.equipoId ?? "";
      form.fechaProgramada.value = mantenimientoData.fechaProgramada
        ? String(mantenimientoData.fechaProgramada).slice(0, 16)
        : form.fechaProgramada.value;
      form.frecuenciaDias.value = mantenimientoData.frecuenciaDias ?? "";
      form.descripcion.value = mantenimientoData.descripcion ?? "";
      if (estadoSelect) estadoSelect.value = (mantenimientoData.estado || "PROGRAMADO").toUpperCase();
    } catch (err) {
      showError("No se pudo cargar el mantenimiento");
    }
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!empresaSelect?.value) {
      showError("El mantenimiento requiere la empresa del equipo.");
      return;
    }
    const empresaSeleccionada = empresasCache.find((e) => String(e.id) === String(empresaSelect?.value || ""));
    const empresaId = empresaSelect?.value ? Number(empresaSelect.value) : null;
    const clienteId = empresaSeleccionada?.clienteId ? Number(empresaSeleccionada.clienteId) : (empresaId ?? Number(mantenimientoData?.idCliente || 0));
    const payload = {
      equipoId: equipoHidden?.value ? Number(equipoHidden.value) : null,
      serieSnapshot: form.serieEquipo.value.trim(),
      idCliente: clienteId,
      empresaId,
      fechaProgramada: form.fechaProgramada.value ? form.fechaProgramada.value + ":00" : null,
      frecuenciaDias: form.frecuenciaDias.value === "" ? null : Number(form.frecuenciaDias.value),
      descripcion: form.descripcion.value.trim() || null,
      estado: estadoSelect ? estadoSelect.value : "PROGRAMADO"
    };
    if (payload.fechaProgramada) {
      const ahora = new Date();
      const fechaProg = new Date(payload.fechaProgramada);
      if (fechaProg < ahora) {
        payload.estado = "COMPLETADO";
        if (estadoSelect) estadoSelect.value = "COMPLETADO";
        payload.frecuenciaDias = 0;
        form.frecuenciaDias.value = "0";
      }
    }
    if (payload.frecuenciaDias === null) {
      payload.frecuenciaDias = 0;
      form.frecuenciaDias.value = "0";
    }
    if (!payload.equipoId) {
      const eq = equipos.find((e) => String(e.serie || e.serieEquipo || "").toUpperCase() === payload.serieSnapshot.toUpperCase());
      if (eq) payload.equipoId = eq.id;
    }
    if (!payload.equipoId) {
      showError("Selecciona un equipo válido de la lista.");
      return;
    }
    try {
      if (id) {
        await mantenimientosApi.update(id, payload);
        showSuccess("Mantenimiento actualizado");
      } else {
        await mantenimientosApi.create(payload);
        showSuccess("Mantenimiento creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 400);
    } catch (err) {
      showError(err.message);
    }
  });

  
  await Promise.all([loadEmpresas(), loadEquipos()]);
  if (mantenimientoData) {
    if (empresaSelect) empresaSelect.value = empresaPreset || "";
    renderSeries(empresaPreset);
    if (serieInput) {
      serieInput.value = toUpper(mantenimientoData.serieEquipo || serieInput.value);
      syncEmpresaFromSerie(serieInput.value);
      serieInput.readOnly = true;
    }
    if (empresaSelect) {
      empresaSelect.disabled = true;
    }
  } else {
    if (serieParam && equipos.length) {
      serieInput.value = toUpper(serieParam);
      syncEmpresaFromSerie(serieParam);
    }
    if (empresaSelect) empresaSelect.disabled = false;
    renderSeries(empresaSelect?.value || empresaParam);
  }

  if (empresaSelect) {
    empresaSelect.addEventListener("change", (ev) => {
      renderSeries(ev.target.value);
      const currentSerie = form.serieEquipo.value.trim();
      if (currentSerie) {
        const belongs = equipos.some(
          (e) =>
            String(e.serie || e.serieEquipo || "").toUpperCase() === currentSerie.toUpperCase() &&
            String(e.empresaId || e.idCliente) === String(ev.target.value || "")
        );
        if (!belongs) {
          form.serieEquipo.value = "";
          if (equipoHidden) equipoHidden.value = "";
        }
      }
      if (!ev.target.value && equipoHidden) equipoHidden.value = "";
    });
  }

  
  serieInput?.addEventListener("input", () => {
    serieInput.value = toUpper(serieInput.value);
    const serieVal = serieInput.value.trim();
    if (!serieVal) {
      if (empresaSelect) empresaSelect.value = "";
      return;
    }
    syncEmpresaFromSerie(serieVal);
  });
}

document.addEventListener("DOMContentLoaded", main);
