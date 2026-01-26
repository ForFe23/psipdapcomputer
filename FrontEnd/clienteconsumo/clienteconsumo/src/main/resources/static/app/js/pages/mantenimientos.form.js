import { loadLayout } from "../ui/render.js";
import { mantenimientosApi } from "../api/mantenimientos.api.js";
import { clientesApi } from "../api/clientes.api.js";
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
  const clienteSelect = document.querySelector("#clienteSelect");
  const seriesList = document.querySelector("#seriesList");
  const serieInput = form?.serieEquipo;
  const equipoHidden = document.querySelector("#equipoId");
  const estadoSelect = document.querySelector("#estadoSelect");
  const id = getParam("id");
  const serieParam = getParam("serie");
  const clienteParam = getParam("idCliente");
  const sugerenciaCards = document.querySelectorAll("[data-plan]");
  let clientePreset = clienteParam || "";
  let mantenimientoData = null;

  
  const presetFreq = getParam("frecuencia");
  const presetDesc = getParam("desc");
  const presetEstado = getParam("estado");

  let equipos = [];
  let clientesCache = [];

  
  const loadClientes = async () => {
    try {
      const clientes = await clientesApi.list();
      clientesCache = clientes;
      if (clienteSelect) {
        clienteSelect.innerHTML =
          `<option value="">Seleccione empresa</option>` +
          clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
        if (clientePreset) clienteSelect.value = clientePreset;
      }
      return clientes;
    } catch (err) {
      showError("No se pudo cargar clientes");
    }
    return [];
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      
    }
  };

  const renderSeries = (clienteId) => {
    if (!seriesList) return;
    const filtered = clienteId ? equipos.filter((e) => String(e.idCliente) === String(clienteId)) : equipos;
    const sugerencia = `<option value="⬇SUGERENCIAS⬇"></option>`;
    seriesList.innerHTML = sugerencia + filtered
      .map((e) => {
        const serie = toUpper(e.serie || e.serieEquipo || "");
        return serie ? `<option value="${serie}">${serie}</option>` : "";
      })
      .join("");
  };

  const syncClienteFromSerie = (serieVal) => {
    if (!serieVal) {
      if (clienteSelect) clienteSelect.value = "";
      if (equipoHidden) equipoHidden.value = "";
      return;
    }
    const eq = equipos.find((e) => String(e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
    if (eq && clienteSelect) {
      clienteSelect.value = eq.idCliente ?? "";
      renderSeries(eq.idCliente);
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
      clientePreset = mantenimientoData.idCliente ?? clientePreset;
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
    if (!clienteSelect?.value) {
      showError("El mantenimiento requiere la empresa del equipo.");
      return;
    }
    const payload = {
      equipoId: equipoHidden?.value ? Number(equipoHidden.value) : null,
      serieSnapshot: form.serieEquipo.value.trim(),
      idCliente: Number(clienteSelect?.value || mantenimientoData?.idCliente || 0),
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

  
  await Promise.all([loadClientes(), loadEquipos()]);
  if (mantenimientoData) {
    if (clienteSelect) clienteSelect.value = clientePreset || "";
    renderSeries(clientePreset);
    if (serieInput) {
      serieInput.value = toUpper(mantenimientoData.serieEquipo || serieInput.value);
      syncClienteFromSerie(serieInput.value);
      serieInput.readOnly = true;
    }
    if (clienteSelect) {
      clienteSelect.disabled = true;
    }
  } else {
    if (serieParam && equipos.length) {
      serieInput.value = toUpper(serieParam);
      syncClienteFromSerie(serieParam);
    }
    if (clienteSelect) clienteSelect.disabled = false;
    renderSeries(clienteSelect?.value || clienteParam);
  }

  if (clienteSelect) {
    clienteSelect.addEventListener("change", (ev) => {
      renderSeries(ev.target.value);
      const currentSerie = form.serieEquipo.value.trim();
      if (currentSerie) {
        const belongs = equipos.some(
          (e) =>
            String(e.serie || e.serieEquipo || "").toUpperCase() === currentSerie.toUpperCase() &&
            String(e.idCliente) === String(ev.target.value || "")
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
      if (clienteSelect) clienteSelect.value = "";
      return;
    }
    syncClienteFromSerie(serieVal);
  });
}

document.addEventListener("DOMContentLoaded", main);
