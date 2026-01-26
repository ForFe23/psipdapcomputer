import { loadLayout } from "../ui/render.js";
import { incidentesApi } from "../api/incidentes.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { usuariosApi } from "../api/usuarios.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

function getParam(name) {
  const url = new URL(window.location.href);
  return url.searchParams.get(name);
}

async function main() {
  const DEFAULT_USER_ID = "1";
  await loadLayout("incidentes");
  const form = document.querySelector("#incidente-form");
  function removeOptionalClientFields() {
    ["clienteNombre", "clienteDetalle"].forEach((id) => {
      const el = document.getElementById(id);
      if (el) {
        const group = el.closest(".form-group") || el.parentElement;
        if (group) group.remove();
      }
    });
    document.querySelectorAll("label").forEach((lbl) => {
      const txt = (lbl.textContent || "").trim().toLowerCase();
      if (txt.includes("cliente (nombre opcional)") || txt.includes("detalle cliente")) {
        const group = lbl.closest(".form-group");
        if (group) group.remove();
      }
    });
  }
  removeOptionalClientFields();
  const serie = getParam("serie") || "";
  const idClienteParam = getParam("idCliente") || "";
  const incidenteId = getParam("id");

  form.serieEquipo.value = serie;
  form.idUsuario.value = DEFAULT_USER_ID;
  document.getElementById("res-usuario").textContent = `Usuario fijo (id ${DEFAULT_USER_ID})`;

  async function asignarUsuarioPorCliente(idCliente, preferenciaId) {
    let usuarios = [];
    try {
      usuarios = idCliente ? await usuariosApi.listByCliente(idCliente) : await usuariosApi.list();
    } catch (err) {
      usuarios = [];
    }
    const filtrados = (usuarios || []).filter((u) => (u.estadoInterno || "").toUpperCase() !== "INACTIVO_INTERNAL");
    const elegido = filtrados.find((u) => preferenciaId && String(u.id) === String(preferenciaId)) || filtrados[0];
    if (elegido) {
      form.idUsuario.value = elegido.id;
      document.getElementById("res-usuario").textContent = `${elegido.nombres || ""} ${elegido.apellidos || ""}`.trim() || `Usuario ${elegido.id}`;
    }
    return !!elegido;
  }

  async function rellenarClienteDesdeEquipo() {
    if (!form.serieEquipo.value) return;
    try {
      const equipo = await equiposApi.getBySerie(form.serieEquipo.value);
      if (equipo) {
        form.serieEquipo.value = equipo.serie || form.serieEquipo.value;
        form.idCliente.value = equipo.idCliente ? String(equipo.idCliente) : form.idCliente.value;
        document.getElementById("res-serie").textContent = form.serieEquipo.value || "-";
        const nombreCliente = equipo.cliente || equipo.idCliente || form.idCliente.value || "-";
        document.getElementById("res-cliente").textContent = nombreCliente;
        await asignarUsuarioPorCliente(form.idCliente.value);
      }
    } catch (err) {
      
    }
  }

  try {
    const clientes = await clientesApi.list();
    const cliente = clientes.find((c) => String(c.id) === idClienteParam);
    form.idCliente.value = idClienteParam || (cliente?.id ?? form.idCliente.value);
    document.getElementById("res-cliente").textContent = cliente ? (cliente.nombre || cliente.razonSocial || cliente.id) : idClienteParam || form.idCliente.value || "-";
  } catch (err) {
    document.getElementById("res-cliente").textContent = idClienteParam || form.idCliente.value || "-";
  }

  if (form.serieEquipo.value) {
    await rellenarClienteDesdeEquipo();
  }
  if (form.idCliente.value) {
    const asignado = await asignarUsuarioPorCliente(form.idCliente.value, form.idUsuario.value);
    if (!asignado) {
      form.idUsuario.value = DEFAULT_USER_ID;
      document.getElementById("res-usuario").textContent = `Usuario fijo (id ${DEFAULT_USER_ID})`;
    }
  }

  const now = new Date();
  const localISO = new Date(now.getTime() - now.getTimezoneOffset() * 60000)
    .toISOString()
    .slice(0, 19);
  form.fechaIncidente.value = localISO;
  document.getElementById("res-fecha").textContent = localISO.replace("T", " ");
  document.getElementById("res-serie").textContent = form.serieEquipo.value || "-";

  if (incidenteId) {
    try {
      const inc = await incidentesApi.getById(incidenteId);
      form.serieEquipo.value = inc.serieEquipo || serie;
      form.idCliente.value = inc.idCliente ?? idClienteParam;
      let usuarioAsignado = await asignarUsuarioPorCliente(form.idCliente.value, inc.idUsuario ?? null);
      if (!usuarioAsignado && inc.idUsuario) {
        try {
          const usr = await usuariosApi.getById(inc.idUsuario);
          if (usr) {
            form.idUsuario.value = usr.id;
            document.getElementById("res-usuario").textContent = `${usr.nombres || ""} ${usr.apellidos || ""}`.trim() || `Usuario ${usr.id}`;
            usuarioAsignado = true;
          }
        } catch (err) {
          
        }
      }
      if (!usuarioAsignado && !form.idUsuario.value) {
        form.idUsuario.value = DEFAULT_USER_ID;
        document.getElementById("res-usuario").textContent = `Usuario fijo (id ${DEFAULT_USER_ID})`;
      }
      form.fechaIncidente.value = inc.fechaIncidente ? String(inc.fechaIncidente).slice(0, 19) : localISO;
      form.detalle.value = inc.detalle || "";
      form.costo.value = inc.costo || "";
      form.tecnico.value = inc.tecnico || "";
      form.responsable.value = inc.responsable || "";
      document.getElementById("res-fecha").textContent = (form.fechaIncidente.value || localISO).replace("T", " ");
      document.getElementById("res-serie").textContent = form.serieEquipo.value || "-";
      if (!document.getElementById("res-cliente").textContent || document.getElementById("res-cliente").textContent === "-") {
        document.getElementById("res-cliente").textContent = form.idCliente.value || "-";
      }
      if (!form.idCliente.value && form.serieEquipo.value) {
        await rellenarClienteDesdeEquipo();
      }
    } catch (err) {
      showError("No se pudo cargar el incidente");
    }
  }

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    if (!form.idCliente.value || !form.idUsuario.value || !form.detalle.value.trim() || !form.serieEquipo.value.trim()) {
      showError("Serie, cliente, usuario y detalle son obligatorios.");
      return;
    }
    const serieVal = (form.serieEquipo.value || "").trim().toUpperCase();
    const detalleVal = (form.detalle.value || "").trim().toUpperCase();
    const tecnicoVal = (form.tecnico.value || "").trim().toUpperCase();
    const respVal = (form.responsable.value || "").trim().toUpperCase();
    const payload = {
      serieEquipo: serieVal,
      idCliente: Number(form.idCliente.value),
      idUsuario: Number(form.idUsuario.value),
      fechaIncidente:
        form.fechaIncidente.value ||
        new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
          .toISOString()
          .slice(0, 19),
      detalle: detalleVal,
      costo: form.costo.value.trim() || undefined,
      tecnico: tecnicoVal || undefined,
      responsable: respVal || undefined
    };
    Object.keys(payload).forEach((k) => payload[k] === undefined && delete payload[k]);
    try {
      if (incidenteId) {
        await incidentesApi.update(incidenteId, payload);
        showSuccess("Incidente actualizado");
      } else {
        await incidentesApi.create(payload);
        showSuccess("Incidente creado");
      }
      setTimeout(() => (window.location.href = "../equipos/list.html"), 500);
    } catch (err) {
      showError(err.message);
    }
  });
}

document.addEventListener("DOMContentLoaded", main);
