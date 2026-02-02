import { loadLayout } from "../ui/render.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { actasApi } from "../api/actas.api.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { personasApi } from "../api/personas.api.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

let empresas = [];
let equipos = [];
let usuarios = [];
let ubicaciones = [];
let actas = [];
let actaDatalist;
let btnActasLibres;
let panelActasLibres;
let closeActasLibres;
let actasLibresBody;
let personasUsuarios = [];
let personasUsuariosList;
let pendingUsuarioId = null;
let pendingEquipoId = null;

async function loadMovimiento(id, form) {
  const data = await movimientosApi.getById(id);
  form.equipoId.value = data.idEquipo ?? data.equipoId ?? "";
  pendingEquipoId = data.idEquipo ?? data.equipoId ?? null;
  if (form.serieEquipo) form.serieEquipo.value = data.serieEquipo ?? data.serie ?? "";
  if (form.empresaId) form.empresaId.value = data.empresaId ?? data.clienteId ?? data.idCliente ?? "";
  pendingUsuarioId =
    data.ejecutadoPorId ??
    data.idUsuarioOrigen ??
    data.idUsuarioDestino ??
    data.personaOrigenId ??
    data.personaDestinoId ??
    null;
  form.usuarioId.value = pendingUsuarioId ?? "";
  form.actaId.value = data.idActa ?? "";
  form.tipo.value = data.tipo ?? "";
  form.origen.value = data.ubicacionOrigen ?? data.origen ?? "";
  form.destino.value = data.ubicacionDestino ?? data.destino ?? "";
  form.observaciones.value = data.observacion ?? data.observaciones ?? "";
  if (form.fecha) {
    const fecha = data.fecha ?? "";
    form.fecha.value = fecha ? String(fecha).slice(0, 10) : "";
  }
  if (form.origenId) form.origenId.dataset.pendingValue = data.ubicacionOrigenId ?? "";
  if (form.destinoId) form.destinoId.dataset.pendingValue = data.ubicacionDestinoId ?? "";
  if (form.usuarioId) form.usuarioId.dataset.pendingValue = pendingUsuarioId ?? "";
  return data;
}

function renderPersonasUsuarios(list = []) {
  if (!personasUsuariosList) return;
  personasUsuariosList.innerHTML = list
    .map((p) => `<option value="${p.id}" label="${p.label}">${p.label}</option>`)
    .join("");
}

function applyPendingUsuario(input) {
  if (!input) return;
  if (!pendingUsuarioId) return;
  const match = personasUsuarios.find((p) => String(p.id) === String(pendingUsuarioId));
  if (match) {
    input.value = match.label || match.id;
  } else {
    input.value = pendingUsuarioId;
  }
}

async function loadPersonasUsuarios(empresaId) {
  try {
    if (!empresaId) {
      personasUsuarios = [];
      renderPersonasUsuarios(personasUsuarios);
      return;
    }
    const [pers, usrs] = await Promise.all([
      personasApi.listByEmpresa(empresaId),
      usuariosApi.listByEmpresa(empresaId)
    ]);
    personasUsuarios = [
      ...pers.map((p) => ({ id: p.id, label: `${p.nombres || ""} ${p.apellidos || ""}`.trim() })),
      ...usrs.map((u) => ({ id: u.id, label: `${u.nombres || ""} ${u.apellidos || ""}`.trim() }))
    ].filter((x) => x.label);
    renderPersonasUsuarios(personasUsuarios);
    applyPendingUsuario(document.querySelector("#usuarioId"));
  } catch (err) {
    renderPersonasUsuarios([]);
    showError("No se pudieron cargar personas/usuarios: " + err.message);
  }
}

function withinNextWeek(fechaStr) {
  if (!fechaStr) return false;
  const today = new Date();
  const day = today.getDay();
  const mondayOffset = day === 0 ? -6 : 1 - day;
  const start = new Date(today);
  start.setHours(0, 0, 0, 0);
  start.setDate(today.getDate() + mondayOffset);
  const end = new Date(start);
  end.setDate(start.getDate() + 7);
  const fecha = new Date(fechaStr);
  return fecha >= start && fecha <= end;
}

function renderActaDatalist(data = []) {
  if (!actaDatalist) return;
  actaDatalist.innerHTML = data
    .map(
      (a) =>
        `<option value="${a.id}" label="${(a.tema || 'ACTA').toUpperCase()} | ${a.fechaActa || ''}">${(a.tema || '').toUpperCase()}</option>`
    )
    .join("");
}

function renderActasLibresPanel(list = []) {
  if (!actasLibresBody) return;
  if (!list.length) {
    actasLibresBody.innerHTML = `<p class="text-muted mb-0">No hay actas sin movimiento en el rango solicitado.</p>`;
    return;
  }
  actasLibresBody.innerHTML = list
    .map(
      (a) =>
        `<div class="acta-item" data-id="${a.id}" data-tema="${a.tema || ""}">
          <div><strong>${(a.tema || "SIN TEMA").toUpperCase()}</strong></div>
          <small>ID: ${a.id} · Fecha: ${a.fechaActa || ""}</small>
        </div>`
    )
    .join("");
}

async function loadActas(empresaId) {
  actas = await actasApi.list();
  const movimientoData = await movimientosApi.list();
  const usados = new Set(movimientoData.map((m) => m.idActa).filter(Boolean));

  const ventana = actas.filter((a) => withinNextWeek(a.fechaActa));
  ventana.sort((a, b) => (a.tema || "").localeCompare(b.tema || ""));
  renderActaDatalist(ventana);

  const libres = actas.filter((a) => !usados.has(a.id));
  libres.sort((a, b) => (a.tema || "").localeCompare(b.tema || ""));
  renderActasLibresPanel(libres);

  if (panelActasLibres) {
    panelActasLibres.dataset.empresaid = empresaId || "";
  }
}
async function loadUbicacionesByEmpresa(empresaId, origenSelect, destinoSelect, pendingOrigen, pendingDestino, afterRender) {
  if (!origenSelect && !destinoSelect) return;
  try {
    if (!empresaId) {
      ubicaciones = [];
      const emptyOption = `<option value=\"\">Seleccione ubicación</option>`;
      if (origenSelect) origenSelect.innerHTML = emptyOption;
      if (destinoSelect) destinoSelect.innerHTML = emptyOption;
      if (afterRender) afterRender();
      return;
    }
    ubicaciones = await ubicacionesApi.listByEmpresa(empresaId);
    const options =
      `<option value=\"\">Seleccione ubicación</option>` +
      ubicaciones.map((u) => `<option value=\"${u.id}\">${u.nombre || u.id}</option>`).join("");
    if (origenSelect) {
      origenSelect.innerHTML = options;
      if (pendingOrigen) {
        origenSelect.value = pendingOrigen;
        origenSelect.dataset.pendingValue = "";
      }
    }
    if (destinoSelect) {
      destinoSelect.innerHTML = options;
      if (pendingDestino) {
        destinoSelect.value = pendingDestino;
        destinoSelect.dataset.pendingValue = "";
      }
    }
    if (afterRender) afterRender();
  } catch (err) {
    if (origenSelect) origenSelect.innerHTML = `<option value=\"\">Sin ubicaciones</option>`;
    if (destinoSelect) destinoSelect.innerHTML = `<option value=\"\">Sin ubicaciones</option>`;
    showError("No se pudieron cargar ubicaciones: " + err.message);
  }
}

function syncUbicacionInputFromSelect(select, input) {
  if (!select || !input) return;
  const opt = select.selectedOptions[0];
  if (opt && opt.value) {
    input.value = opt.textContent;
  } else {
    input.value = "";
  }
}

function setOrigenDesdeEquipo(eq, origenSelect, origenInput) {
  if (!eq) return;
  const idUbic = eq.ubicacionActualId ?? eq.ubicacionId;
  const texto = eq.ubicacionUsuario || "";
  if (origenSelect) {
    origenSelect.dataset.pendingValue = idUbic ?? "";
    if (idUbic) origenSelect.value = idUbic;
  }
  if (origenInput) origenInput.value = texto;
}

function setDestinoDesdeActa(acta, destinoSelect, destinoInput) {
  if (!acta) return;
  const idUbic = acta.ubicacionId ?? null;
  const texto = acta.ubicacionDestino || acta.ubicacion || "";
  if (destinoSelect) {
    destinoSelect.dataset.pendingValue = idUbic ?? "";
    if (idUbic) destinoSelect.value = idUbic;
  }
  if (destinoInput) destinoInput.value = texto;
}

function resolvePersonaUsuarioId(value) {
  if (!value) return null;
  if (!Number.isNaN(Number(value))) return Number(value);
  const match = personasUsuarios.find((p) => p.label.toUpperCase() === value.toUpperCase());
  return match ? match.id : null;
}

async function main() {
  await loadLayout("movimientos");
  const form = document.querySelector("#movimiento-form");
  const empresaSelect = document.querySelector("#empresaSelect");
  const serieInput = document.querySelector("#serieEquipo");
  const seriesList = document.querySelector("#seriesList");
  const equipoHidden = document.querySelector("#equipoId");
  const usuarioInput = document.querySelector("#usuarioId");
  const origenSelect = document.querySelector("#origenId");
  const destinoSelect = document.querySelector("#destinoId");
  const origenInput = document.querySelector("#origen");
  const destinoInput = document.querySelector("#destino");
  actaDatalist = document.querySelector("#actasList");
  btnActasLibres = document.querySelector("#btnActasLibres");
  panelActasLibres = document.querySelector("#panel-actas-libres");
  closeActasLibres = document.querySelector("#closeActasLibres");
  actasLibresBody = document.querySelector("#actasLibresBody");
  const actaInput = form.actaId;
  personasUsuariosList = document.querySelector("#personas-usuarios-datalist");

  const loadClientes = async () => {
    try {
      empresas = await empresasApi.list();
      if (empresaSelect) {
        empresaSelect.innerHTML =
          `<option value="">Seleccione empresa</option>` +
          empresas.map((e) => `<option value="${e.id}">${e.nombre || e.id}</option>`).join("");
      }
    } catch (err) {
      showError("No se pudieron cargar empresas");
    }
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      showError("No se pudieron cargar equipos");
    }
  };

  const renderSeries = (empresaId) => {
    if (!seriesList) return;
    const filtered = empresaId ? equipos.filter((e) => String(e.empresaId || e.idCliente) === String(empresaId)) : equipos;
    const sugerencia = `<option value="⬇SUGERENCIAS⬇"></option>`;
    seriesList.innerHTML =
      sugerencia +
      filtered
        .map((e) => {
          const serie = (e.serie || e.serieEquipo || "").trim();
          return serie ? `<option value="${serie}">${serie}</option>` : "";
        })
        .join("");
  };

  const syncEmpresaFromSerie = (serieVal) => {
    if (!serieVal) {
      if (equipoHidden) equipoHidden.value = "";
      return;
    }
    const eq = equipos.find((e) => (e.serie || e.serieEquipo || "").toUpperCase() === serieVal.toUpperCase());
    if (eq) {
      if (empresaSelect) empresaSelect.value = eq.empresaId ?? eq.idCliente ?? "";
      if (equipoHidden) equipoHidden.value = eq.id ?? "";
      pendingEquipoId = eq.id ?? pendingEquipoId;
      renderSeries(eq.empresaId ?? eq.idCliente);
      setOrigenDesdeEquipo(eq, origenSelect, origenInput);
      loadUbicacionesByEmpresa(empresaSelect?.value || "", origenSelect, destinoSelect, origenSelect?.dataset.pendingValue, destinoSelect?.dataset.pendingValue, () => {
        syncUbicacionInputFromSelect(origenSelect, origenInput);
        syncUbicacionInputFromSelect(destinoSelect, destinoInput);
      });
    } else if (equipoHidden) {
      equipoHidden.value = "";
    }
  };

  await Promise.all([loadClientes(), loadEquipos()]);
  renderSeries(empresaSelect?.value || "");
  await loadPersonasUsuarios(empresaSelect?.value || "");
  await loadUbicacionesByEmpresa(
    empresaSelect?.value || "",
    origenSelect,
    destinoSelect,
    origenSelect?.dataset.pendingValue,
    destinoSelect?.dataset.pendingValue,
    () => {
      syncUbicacionInputFromSelect(origenSelect, origenInput);
      syncUbicacionInputFromSelect(destinoSelect, destinoInput);
    }
  );
  await loadActas(empresaSelect?.value || "");

  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar movimiento";
    try {
      const data = await loadMovimiento(id, form);
      const empresaDato = form.empresaId?.value || data?.empresaId || data?.idCliente;
      await loadPersonasUsuarios(empresaDato);
      await loadUbicacionesByEmpresa(
        empresaDato,
        origenSelect,
        destinoSelect,
        origenSelect?.dataset.pendingValue,
        destinoSelect?.dataset.pendingValue,
        () => {
          syncUbicacionInputFromSelect(origenSelect, origenInput);
          syncUbicacionInputFromSelect(destinoSelect, destinoInput);
        }
      );
      // Prellenar destino con datos del acta asociada
      const actaSel = actas.find((a) => String(a.id) === String(form.actaId.value || ""));
      if (actaSel) setDestinoDesdeActa(actaSel, destinoSelect, destinoInput);
    } catch (err) {
      showError(err.message);
    }
  }

  if (serieInput) {
    serieInput.addEventListener("input", () => {
      syncEmpresaFromSerie(serieInput.value);
      const empresaIdActual = empresaSelect?.value || "";
      loadPersonasUsuarios(empresaIdActual);
      loadUbicacionesByEmpresa(empresaIdActual, origenSelect, destinoSelect, null, null, () => {
        syncUbicacionInputFromSelect(origenSelect, origenInput);
        syncUbicacionInputFromSelect(destinoSelect, destinoInput);
      });
    });
  }
  if (empresaSelect) {
    empresaSelect.addEventListener("change", () => {
      renderSeries(empresaSelect.value);
      loadPersonasUsuarios(empresaSelect.value);
      loadUbicacionesByEmpresa(empresaSelect.value, origenSelect, destinoSelect, null, null, () => {
        syncUbicacionInputFromSelect(origenSelect, origenInput);
        syncUbicacionInputFromSelect(destinoSelect, destinoInput);
      });
      loadActas(empresaSelect.value);
    });
  }
  if (origenSelect) {
    origenSelect.addEventListener("change", () => syncUbicacionInputFromSelect(origenSelect, origenInput));
  }
  if (destinoSelect) {
    destinoSelect.addEventListener("change", () => syncUbicacionInputFromSelect(destinoSelect, destinoInput));
  }
  if (actaInput) {
    actaInput.addEventListener("change", () => {
      const actaSel = actas.find((a) => String(a.id) === String(actaInput.value || ""));
      setDestinoDesdeActa(actaSel, destinoSelect, destinoInput);
    });
  }
  if (btnActasLibres && panelActasLibres) {
    btnActasLibres.addEventListener("click", () => {
      panelActasLibres.style.display = "block";
    });
  }
  if (closeActasLibres && panelActasLibres) {
    closeActasLibres.addEventListener("click", () => {
      panelActasLibres.style.display = "none";
    });
  }
  if (actasLibresBody) {
    actasLibresBody.addEventListener("click", (ev) => {
      const item = ev.target.closest(".acta-item");
      if (!item) return;
      const id = item.dataset.id;
      const tema = item.dataset.tema || "";
      if (id && form.actaId) {
        form.actaId.value = id;
      }
      if (form.tipo) {
        form.tipo.value = form.tipo.value || "ENTREGA";
      }
      panelActasLibres.style.display = "none";
    });
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const empresaSeleccionada = empresas.find((e) => String(e.id) === String(form.empresaId?.value || ""));
    const empresaId = form.empresaId?.value ? Number(form.empresaId.value) : null;
    const clienteId = empresaSeleccionada?.clienteId ? Number(empresaSeleccionada.clienteId) : empresaId;
    let equipoId = form.equipoId.value.trim() ? Number(form.equipoId.value.trim()) : null;
    if (!equipoId) {
      const serieSel = form.serieEquipo ? form.serieEquipo.value.trim() : "";
      if (serieSel) {
        const eqMatch = equipos.find((e) => (e.serie || e.serieEquipo || "").toUpperCase() === serieSel.toUpperCase());
        if (eqMatch) equipoId = eqMatch.id ?? equipoId;
      }
    }
    if (!equipoId && pendingEquipoId) {
      equipoId = pendingEquipoId;
    }
    const ubicacionOrigenTxt = (origenInput?.value || "").trim() || (origenSelect?.selectedOptions?.[0]?.textContent || "");
    const ubicacionDestinoTxt = (destinoInput?.value || "").trim() || (destinoSelect?.selectedOptions?.[0]?.textContent || "");
    const fechaEnvio = form.fecha && form.fecha.value ? `${form.fecha.value}T00:00:00` : null;
    const payload = {
      idEquipo: equipoId,
      serieEquipo: form.serieEquipo ? form.serieEquipo.value.trim() : undefined,
      idActa: form.actaId.value.trim() ? Number(form.actaId.value.trim()) : null,
      ejecutadoPorId: resolvePersonaUsuarioId(usuarioInput?.value || ""),
      ubicacionOrigenId: origenSelect?.value ? Number(origenSelect.value) : null,
      ubicacionDestinoId: destinoSelect?.value ? Number(destinoSelect.value) : null,
      empresaId,
      idCliente: clienteId,
      tipo: form.tipo.value.trim(),
      ubicacionOrigen: ubicacionOrigenTxt,
      ubicacionDestino: ubicacionDestinoTxt,
      observacion: form.observaciones.value.trim(),
      fecha: fechaEnvio
    };
    try {
      if (id) {
        await movimientosApi.update(id, payload);
        showSuccess("Movimiento actualizado");
      } else {
        await movimientosApi.create(payload);
        showSuccess("Movimiento creado");
      }
      setTimeout(() => (window.location.href = "./list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
