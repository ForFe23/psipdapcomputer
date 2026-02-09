import { loadLayout } from "../ui/render.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { ubicacionesApi } from "../api/ubicaciones.api.js";
import { personasApi } from "../api/personas.api.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { enforceRole, getAccessScope } from "../auth.js";

const ACTIVO_INTERNAL = "ACTIVO_INTERNAL";
const allowedRoles = ["ADMIN_GLOBAL", "TECNICO_GLOBAL", "TECNICO_CLIENTE", "CLIENTE_ADMIN", "CLIENTE_VISOR"];
const session = enforceRole(allowedRoles);
const scope = getAccessScope();
const EMAIL_REGEX = /^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$/i;

let estadoInternoActual = ACTIVO_INTERNAL;
let empresas = [];
let ubicaciones = [];
let personasUsuarios = [];
let equipoCargado = null;
const urlParams = new URLSearchParams(window.location.search);
const paramClienteId = urlParams.get("clienteId") || urlParams.get("idCliente");
let clienteNombre = "";
let clientes = [];

function getId() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id");
}

function applyScopeDefaults(form) {
  const url = new URL(window.location.href);
  const clienteParam = url.searchParams.get("clienteId") || url.searchParams.get("idCliente");
  const empresaParam = url.searchParams.get("empresaId") || url.searchParams.get("idEmpresa");
  const clienteVal = clienteParam || (scope?.clienteId ? String(scope.clienteId) : "");
  const empresaVal = empresaParam || (scope?.empresaId ? String(scope.empresaId) : "");
  if (form.empresaId && empresaVal) {
    form.empresaId.value = empresaVal;
    form.empresaId.readOnly = true;
  }
  if (form.clienteId && clienteVal) {
    form.clienteId.value = clienteVal;
    form.clienteId.readOnly = true;
  }
}

function showStep(step) {
  document.querySelectorAll(".wizard-step").forEach((s) => {
    const active = s.dataset.step === String(step);
    s.classList.toggle("active", active);
    s.querySelectorAll("[required]").forEach((el) => {
      el.required = active;
      if (!active) {
        el.classList.remove("is-invalid");
      }
    });
  });
}

const scopeClienteId = scope?.clienteId ? String(scope.clienteId) : null;
const scopeEmpresaId = scope?.empresaId ? String(scope.empresaId) : null;

function matchesScope(item = {}) {
  if (!scope) return true;
  const clienteVal = item.idCliente ?? item.clienteId ?? item.idcliente ?? item.id_cliente;
  const empresaVal = item.empresaId ?? item.idEmpresa ?? item.idempresa;
  if (scopeClienteId && clienteVal && String(clienteVal) !== scopeClienteId) return false;
  if (scopeEmpresaId && empresaVal && String(empresaVal) !== scopeEmpresaId) return false;
  return true;
}

function lockForm(form, message) {
  if (!form) return;
  form.querySelectorAll("input, select, textarea, button").forEach((el) => {
    if (el.id === "logout-btn") return;
    el.disabled = true;
  });
  if (message) showError(message);
}

async function loadEquipo(id, form) {
  const data = await equiposApi.getById(id);
  equipoCargado = data;
  if (!matchesScope(data)) {
    lockForm(form, "No puedes editar equipos de otro cliente/empresa.");
    return data;
  }
  estadoInternoActual = data.estadoInterno || ACTIVO_INTERNAL;
  if (form.clienteId && data.idCliente) form.clienteId.value = data.idCliente;
  if (form.empresaId) form.empresaId.value = data.empresaId ?? "";
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
  form.costo.value = data.costo ?? "0";
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
  if (form.ubicacionActualId) {
    form.ubicacionActualId.dataset.pendingValue = data.ubicacionActualId ?? "";
  }
  
  form.querySelectorAll("input[type='text'], input[type='email']").forEach((el) => {
    el.value = (el.value || "").toUpperCase();
  });

  return data;
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
    return ["text", "search", ""].includes(t);
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

async function loadUbicacionesByEmpresa(empresaId, select, onAfterRender, clienteIdFallback = null) {
  if (!select) return;
  if (!empresaId) {
    if (!clienteIdFallback) {
      ubicaciones = [];
      select.innerHTML = `<option value=\"\">Matriz del cliente (sin empresa)</option>`;
      if (onAfterRender) onAfterRender();
      return;
    }
    try {
      ubicaciones = await ubicacionesApi.listByCliente(clienteIdFallback);
      if (!ubicaciones.length) {
        select.innerHTML = `<option value=\"\">Matriz del cliente (sin empresa)</option>`;
      } else {
        select.innerHTML =
          `<option value=\"\">Seleccione ubicación</option>` +
          ubicaciones.map((u) => `<option value=\"${u.id}\">${u.nombre || u.id}</option>`).join("");
        if (ubicaciones.length === 1) {
          select.value = String(ubicaciones[0].id);
        }
      }
      if (onAfterRender) onAfterRender();
      return;
    } catch (err) {
      select.innerHTML = `<option value=\"\">Matriz del cliente (sin empresa)</option>`;
      if (onAfterRender) onAfterRender();
      return;
    }
  }
  try {
    ubicaciones = await ubicacionesApi.listByEmpresa(empresaId);
    select.innerHTML =
      `<option value=\"\">Seleccione ubicación</option>` +
      ubicaciones.map((u) => `<option value=\"${u.id}\">${u.nombre || u.id}</option>`).join("");
    if (ubicaciones.length === 1) {
      select.value = String(ubicaciones[0].id);
    }
    if (onAfterRender) onAfterRender();
  } catch (err) {
    select.innerHTML = `<option value=\"\">Sin ubicaciones</option>`;
    showError("No se pudieron cargar ubicaciones: " + err.message);
  }
}

async function main() {
  if (!session) return;
  await loadLayout("equipos");
  bindWizard();
  const form = document.querySelector("#equipo-form");
  applyScopeDefaults(form);
  const clienteSelect = form?.clienteId;
  const empresaSelect = form?.empresaId;
  const ubicacionSelect = form?.ubicacionActualId;
  const clienteContext = document.getElementById("cliente-context");
  const confirmSaveBox = document.getElementById("confirm-save");
  const confirmSaveYes = document.getElementById("btn-confirm-save-yes");
  const confirmSaveNo = document.getElementById("btn-confirm-save-no");
  applyUppercase(form);
  const costoInput = form?.costo;
  const correoProveedorInput = form?.correoProveedor;
  const btnCorreoNobrinda = document.querySelector("#btnCorreoNobrinda");
  let allowEmpresaOpcional = Boolean(scopeClienteId || paramClienteId);
  const updateEmpresaRequired = () => {
    allowEmpresaOpcional = Boolean(scopeClienteId || paramClienteId || clienteSelect?.value);
    if (empresaSelect) {
      empresaSelect.required = !allowEmpresaOpcional;
    }
  };
  updateEmpresaRequired();
  const validateCorreoProveedor = (raw) => {
    const val = (raw || "").trim();
    if (!val) return true;
    if (val.toUpperCase() === "NOBRINDA@CORREO.COM") return true;
    if (!val) return true;
    if (!EMAIL_REGEX.test(val)) {
      showError(`Correo de proveedor inválido. Usa formato nombre@dominio.com (valor: ${val}).`);
      return false;
    }
    return true;
  };
  const sanitizeCosto = () => {
    if (!costoInput) return;
    const clean = (costoInput.value || "").replace(/[^0-9]/g, "");
    costoInput.value = clean === "" ? "0" : clean;
  };
  sanitizeCosto();
  if (costoInput) {
    costoInput.addEventListener("input", sanitizeCosto);
    costoInput.addEventListener("blur", sanitizeCosto);
  }
  if (correoProveedorInput) {
    correoProveedorInput.addEventListener("blur", () => {
      validateCorreoProveedor(correoProveedorInput.value);
    });
  }
  if (btnCorreoNobrinda && correoProveedorInput) {
    btnCorreoNobrinda.addEventListener("click", () => {
      correoProveedorInput.value = "nobrinda@correo.com";
      validateCorreoProveedor(correoProveedorInput.value);
    });
  }
  const renderContext = (empresaIdValue) => {
    if (!clienteContext) return;
    if (empresaIdValue) {
      const emp = empresas.find((e) => String(e.id) === String(empresaIdValue));
      const nombreEmp = emp?.nombre || emp?.razonSocial || `EMPRESA ${empresaIdValue}`;
      clienteContext.textContent = `Empresa seleccionada: ${nombreEmp}`;
      return;
    }
    const lblCliente = clienteNombre || (scopeClienteId ? `Cliente ${scopeClienteId}` : paramClienteId ? `Cliente ${paramClienteId}` : "Cliente");
    clienteContext.textContent = `Sin empresa: se guardará en MATRIZ del ${lblCliente}.`;
  };

  const fetchClienteNombre = async (clienteId) => {
    if (!clienteId) {
      clienteNombre = "";
      renderContext(empresaSelect?.value);
      return;
    }
    try {
      const cli = await clientesApi.getById(clienteId);
      clienteNombre = cli?.nombre || cli?.razonSocial || `Cliente ${clienteId}`;
    } catch {
      clienteNombre = `Cliente ${clienteId}`;
    }
    renderContext(empresaSelect?.value);
  };
  const syncUbicacionUsuario = () => {
    if (!ubicacionSelect || !form.ubicacionUsuario) return;
    const opt = ubicacionSelect.selectedOptions[0];
    if (opt) {
      form.ubicacionUsuario.value = opt.textContent || "";
    } else {
      form.ubicacionUsuario.value = "";
    }
  };

  const personasUsuariosList = document.querySelector("#personas-usuarios-datalist");

  const loadClientes = async () => {
    clientes = await clientesApi.list();
    if (clienteSelect) {
      clienteSelect.innerHTML =
        `<option value=\"\">Seleccione cliente</option>` +
        clientes.map((c) => `<option value=\"${c.id}\">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      if (scopeClienteId) {
        clienteSelect.value = scopeClienteId;
        clienteSelect.disabled = true;
      } else if (paramClienteId) {
        clienteSelect.value = paramClienteId;
      }
      updateEmpresaRequired();
    }
  };

  const loadPersonasUsuarios = async (empresaId) => {
    const clienteContextId = clienteSelect?.value || scopeClienteId || paramClienteId || "";
    if (!empresaId && !clienteContextId) {
      personasUsuarios = [];
      if (personasUsuariosList) personasUsuariosList.innerHTML = `<option value="⬇SUGERENCIAS⬇"></option>`;
      return;
    }
    try {
      const [pers, usrs] = await Promise.all([
        empresaId ? personasApi.listByEmpresa(empresaId) : personasApi.listByCliente(clienteContextId),
        empresaId ? usuariosApi.listByEmpresa(empresaId) : usuariosApi.listByCliente(clienteContextId)
      ]);
      personasUsuarios = [
        ...pers.map((p) => ({ id: p.id, label: `${p.nombres || ""} ${p.apellidos || ""}`.trim() })),
        ...usrs.map((u) => ({ id: u.id, label: `${u.nombres || ""} ${u.apellidos || ""}`.trim() }))
      ].filter((x) => x.label);
      if (personasUsuariosList) {
        personasUsuariosList.innerHTML =
          `<option value="⬇SUGERENCIAS⬇"></option>` +
          personasUsuarios.map((p) => `<option value="${p.label}" data-id="${p.id}">${p.label}</option>`).join("");
      }
    } catch (err) {
      if (personasUsuariosList) personasUsuariosList.innerHTML = `<option value="⬇SUGERENCIAS⬇"></option>`;
    }
  };

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
    personasUsuarios: personasUsuariosList
  };

  
  const loadEmpresasByCliente = async (clienteId) => {
    empresas = clienteId ? await empresasApi.listByCliente(clienteId) : await empresasApi.list();
  };

  try {
    await loadClientes();
    const initialCliente = scopeClienteId || paramClienteId || clienteSelect?.value || "";
    await fetchClienteNombre(initialCliente);
    await loadEmpresasByCliente(initialCliente);
    if (scopeEmpresaId) {
      empresas = empresas.filter((c) => String(c.id) === scopeEmpresaId);
    }
    if (!empresas.length && scopeEmpresaId && !allowEmpresaOpcional) {
      lockForm(form, "No hay empresas disponibles para tu rol.");
      return;
    }
    if (empresaSelect) {
      const placeholder = scopeClienteId ? "Seleccione empresa del cliente" : "Seleccione empresa";
      const opcionMatriz = allowEmpresaOpcional ? `<option value="">Matriz (sólo cliente)</option>` : `<option value="">${placeholder}</option>`;
      empresaSelect.innerHTML =
        opcionMatriz +
        empresas.map((c) => `<option value="${c.id}">${c.nombre ?? c.razonSocial ?? c.id}</option>`).join("");
      if (scopeEmpresaId) {
        empresaSelect.value = scopeEmpresaId;
        empresaSelect.disabled = true;
      } else if (!empresaSelect.value && empresas.length === 1 && scopeClienteId) {
        empresaSelect.value = empresas[0].id;
      }
    }
    const clienteContextId = scopeClienteId || paramClienteId || initialCliente || clienteSelect?.value || "";
    const empresaForData = empresaSelect?.value || scopeEmpresaId || "";
    renderContext(empresaForData);
    await loadPersonasUsuarios(empresaForData);
    await loadUbicacionesByEmpresa(empresaForData, ubicacionSelect, () => {
      const pending = ubicacionSelect?.dataset.pendingValue;
      if (pending) {
        ubicacionSelect.value = pending;
        syncUbicacionUsuario();
      }
    }, clienteContextId);
  } catch (err) {
    showError("No se pudo cargar empresas: " + err.message);
  }

  if (empresaSelect) {
    empresaSelect.addEventListener("change", () => {
      const clienteContextId = scopeClienteId || paramClienteId || clienteSelect?.value || "";
      loadUbicacionesByEmpresa(empresaSelect.value, ubicacionSelect, () => {
        ubicacionSelect.dataset.pendingValue = "";
        syncUbicacionUsuario();
      }, clienteContextId);
      loadPersonasUsuarios(empresaSelect.value);
      renderContext(empresaSelect.value);
    });
  }
  if (clienteSelect && !scopeClienteId) {
    clienteSelect.addEventListener("change", async () => {
      const clienteVal = clienteSelect.value;
      await loadEmpresasByCliente(clienteVal);
      empresaSelect.innerHTML =
        (allowEmpresaOpcional ? `<option value="">Matriz (sólo cliente)</option>` : `<option value="">Seleccione empresa</option>`) +
        empresas.map((c) => `<option value="${c.id}">${c.nombre ?? c.razonSocial ?? c.id}</option>`).join("");
      loadUbicacionesByEmpresa("", ubicacionSelect, () => {
        ubicacionSelect.dataset.pendingValue = "";
        syncUbicacionUsuario();
      }, clienteVal);
      await loadPersonasUsuarios(null);
      await fetchClienteNombre(clienteVal);
      renderContext(empresaSelect.value);
      updateEmpresaRequired();
    });
  }
  if (ubicacionSelect) {
    ubicacionSelect.addEventListener("change", syncUbicacionUsuario);
  }

  
  try {
    const equipos = (await equiposApi.list()).filter(matchesScope);
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
  } catch (err) {
    
  }

  const id = getId();
  if (id) {
    const titleEl = document.querySelector("[data-form-title]");
    if (titleEl) titleEl.textContent = "Editar equipo";
    try {
      const data = await loadEquipo(id, form);
      if (!matchesScope(data)) return;
      if (!clienteNombre && data?.idCliente) {
        await fetchClienteNombre(data.idCliente);
      }
      renderContext(data?.empresaId || form.empresaId?.value || "");
      await loadPersonasUsuarios(data?.empresaId || form.empresaId?.value || "");
      await loadUbicacionesByEmpresa(data?.empresaId || form.empresaId?.value || "", ubicacionSelect, () => {
        if (ubicacionSelect && ubicacionSelect.dataset.pendingValue) {
          ubicacionSelect.value = ubicacionSelect.dataset.pendingValue;
          syncUbicacionUsuario();
        }
      }, data?.idCliente || paramClienteId || scopeClienteId || "");
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

  const doSubmit = async () => {
    const empresaValSubmit = form.empresaId.value || scopeEmpresaId || "";
    if (!validateStep(form.querySelector(".wizard-step.active"))) return;
    if (!id) {
      if (!form.serie.value.trim()) {
        showError("Completa la serie para crear.");
        showStep(1);
        return;
      }
    } else {
      if (!form.serie.value.trim()) {
        showError("La serie es obligatoria al editar.");
        showStep(1);
        return;
      }
    }
    const emailProveedor = (form.correoProveedor?.value || "").trim();
    if (!validateCorreoProveedor(emailProveedor)) return;
    if (form.costo) {
      const clean = (form.costo.value || "").replace(/[^0-9]/g, "");
      form.costo.value = clean === "" ? "0" : clean;
    }
    const costoValor = form.costo?.value || "0";

    const v = (el) => (el && el.value ? el.value.trim() : "");
    const valOrEmpty = (el) => v(el);
    let tipoVal = v(form.tipo);
    if (!tipoVal) {
      tipoVal = id ? (equipoCargado?.tipo || equipoCargado?.tipoEquipo || "NA") : "NA";
    }
    let estadoVal = v(form.estado);
    if (!estadoVal) {
      const defaultEstado = "REGISTRADO";
      const knownEstados = ["OPERATIVO", "REGISTRADO", "ACTIVO", "BAJA", "INACTIVO"];
      const fallback = id ? (equipoCargado?.estado || equipoCargado?.status || defaultEstado) : defaultEstado;
      estadoVal = knownEstados.includes(fallback?.toUpperCase?.() || "") ? fallback : defaultEstado;
    }
    let marcaVal = v(form.marca);
    if (!marcaVal && id) {
      marcaVal = equipoCargado?.marca || equipoCargado?.marcaEquipo || "";
    }

    const empresaIdValue = scopeEmpresaId || form.empresaId.value;
    const empresaSeleccionada = empresas.find((e) => String(e.id) === String(empresaIdValue));
    const empresaId = empresaIdValue ? Number(empresaIdValue) : null;
    const clienteId =
      scopeClienteId
        ? Number(scopeClienteId)
        : empresaSeleccionada?.clienteId
          ? Number(empresaSeleccionada.clienteId)
          : paramClienteId
            ? Number(paramClienteId)
            : clienteSelect?.value
              ? Number(clienteSelect.value)
            : equipoCargado?.idCliente
              ? Number(equipoCargado.idCliente)
              : null;

    if (!clienteId) {
      showError("Falta cliente. Vuelve a abrir desde el cliente o selecciona una empresa que tenga cliente.");
      showStep(1);
      return;
    }

    const payload = {
      idCliente: clienteId,
      empresaId,
      codigoInterno: v(form.codigoInterno),
      activo: v(form.codigoInterno),
      serie: v(form.serie) || (id ? equipoCargado?.serie || "" : ""),
      tipo: tipoVal.toUpperCase(),
      marca: marcaVal,
      modelo: v(form.modelo),
      estado: estadoVal.toUpperCase(),
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
      costo: costoValor,
      factura: valOrEmpty(form.factura),
      nombreProveedor: valOrEmpty(form.proveedor),
      direccionProveedor: valOrEmpty(form.direccionProveedor),
      telefonoProveedor: valOrEmpty(form.telefonoProveedor),
      contactoProveedor: emailProveedor,
      notas: valOrEmpty(form.notas),
      ciudad: valOrEmpty(form.ciudad),
      ubicacionActualId: form.ubicacionActualId?.value ? Number(form.ubicacionActualId.value) : null,
      ubicacionUsuario: valOrEmpty(form.ubicacionUsuario) || (ubicacionSelect?.selectedOptions?.[0]?.textContent ?? ""),
      departamentoUsuario: valOrEmpty(form.departamentoUsuario),
      nombreUsuario: valOrEmpty(form.nombreUsuario)
    };
    if (!empresaId) {
      payload.empresaId = null;
      if (!payload.ubicacionUsuario) {
        const baseClienteLabel = clienteNombre || `CLIENTE ${clienteId}`;
        payload.ubicacionUsuario = `MATRIZ - ${baseClienteLabel}`.toUpperCase();
      }
    }
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
  };

  let pendingPayload = null;

  form.addEventListener("submit", (ev) => {
    ev.preventDefault();
    const empresaValSubmit = form.empresaId.value || scopeEmpresaId || "";
    const selectedClienteVal = form.clienteId?.value || scopeClienteId || paramClienteId || "";
    const hasClienteContext = Boolean(selectedClienteVal);
    if (!empresaValSubmit && !hasClienteContext) {
      showError("Selecciona cliente y empresa o abre el formulario desde un cliente.");
      showStep(1);
      return;
    }
    if (!empresaValSubmit && hasClienteContext && confirmSaveBox) {
      pendingPayload = {};
      confirmSaveBox.classList.remove("d-none");
      return;
    }
    doSubmit();
  });

  if (confirmSaveYes) {
    confirmSaveYes.addEventListener("click", () => {
      confirmSaveBox.classList.add("d-none");
      doSubmit();
    });
  }
  if (confirmSaveNo) {
    confirmSaveNo.addEventListener("click", () => {
      pendingPayload = null;
      confirmSaveBox.classList.add("d-none");
    });
  }
}

document.addEventListener("DOMContentLoaded", main);


