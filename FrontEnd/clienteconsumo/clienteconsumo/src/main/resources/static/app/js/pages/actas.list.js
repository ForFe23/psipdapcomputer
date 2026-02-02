import { loadLayout } from "../ui/render.js";
import { actasApi } from "../api/actas.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError } from "../ui/alerts.js";

let empresas = [];

function empresaNombre(id) {
  const e = empresas.find((em) => String(em.id) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
}

function row(a) {
  const empresaTxt = empresaNombre(a.empresaId ?? a.idCliente);
  return `
    <tr>
      <td>${a.id ?? ""}</td>
      <td>${a.codigo ?? ""}</td>
      <td><span class="badge ${estadoBadge(a.estado)}">${a.estado ?? ""}</span></td>
      <td>${empresaTxt}</td>
      <td>${a.idEquipo ?? ""}</td>
      <td>${a.fechaActa ?? a.fecha ?? ""}</td>
      <td>${a.tema ?? ""}</td>
      <td>
        <a class="btn btn-sm btn-outline-primary" href="./form.html?id=${a.id}">Editar</a>
        <a class="btn btn-sm btn-outline-secondary" href="../adjuntos/list.html?actaId=${a.id}">Adjuntos</a>
        <a class="btn btn-sm btn-outline-info" href="../acta-items/list.html?actaId=${a.id}">Items</a>
        <div class="btn-group">
          <button class="btn btn-sm btn-outline-success dropdown-toggle" data-toggle="dropdown">Acciones</button>
          <div class="dropdown-menu dropdown-menu-right">
            <a class="dropdown-item" href="#" data-estado="EMITIDA" data-id="${a.id}" data-codigo="${a.codigo ?? ""}">Marcar EMITIDA</a>
            <a class="dropdown-item" href="#" data-estado="CERRADA" data-id="${a.id}" data-codigo="${a.codigo ?? ""}">Cerrar acta</a>
            <a class="dropdown-item text-danger" href="#" data-estado="ANULADA" data-id="${a.id}" data-codigo="${a.codigo ?? ""}">Anular acta</a>
            <div class="dropdown-divider"></div>
            <button class="dropdown-item text-danger" data-delete="${a.id}">Eliminar</button>
          </div>
        </div>
      </td>
    </tr>
  `;
}

function estadoBadge(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("EMIT")) return "badge-success";
  if (e.includes("REG")) return "badge-warning";
  if (e.includes("ANU")) return "badge-danger";
  return "badge-secondary";
}

function leerFiltros() {
  const f = document.querySelector("#filtros-actas");
  return {
    codigo: f.fCodigo.value.trim() || undefined,
    empresaId: f.fEmpresa.value.trim() || undefined,
    equipoId: f.fEquipo.value.trim() || undefined,
    estado: f.fEstado.value || undefined,
  };
}

function aplicarFiltros(data, filtros) {
  return data.filter((a) => {
    if (filtros.codigo && !String(a.codigo ?? "").toUpperCase().includes(filtros.codigo.toUpperCase())) return false;
    if (filtros.empresaId && String(a.empresaId ?? a.idCliente ?? a.clienteId) !== filtros.empresaId) return false;
    if (filtros.equipoId && String(a.idEquipo ?? "").toUpperCase() !== filtros.equipoId.toUpperCase()) return false;
    if (filtros.estado && String(a.estado ?? "").toUpperCase() !== filtros.estado.toUpperCase()) return false;
    return true;
  });
}

async function main() {
  await loadLayout("actas");
  const tbody = document.querySelector("#actas-tbody");
  const formFiltros = document.querySelector("#filtros-actas");
  const modal = $("#estadoModal");
  const estadoInfo = document.getElementById("estadoModalInfo");
  const estadoSelect = document.getElementById("estadoSelect");
  const confirmEstadoBtn = document.getElementById("confirmEstadoBtn");
  let actaSeleccionada = null;
  try {
    empresas = await empresasApi.list();
    const empresaSelect = document.getElementById("fEmpresa");
    if (empresaSelect) {
      empresaSelect.innerHTML = `<option value="">Todas</option>` + empresas.map((e) => `<option value="${e.id}">${e.nombre || e.razonSocial || e.id}</option>`).join("");
    }
    const data = await actasApi.list();
    const filtrado = aplicarFiltros(data, leerFiltros());
    tbody.innerHTML = filtrado.map(row).join("");
    actualizarKpis(filtrado);
  } catch (err) {
    showError(err.message);
  }

  formFiltros.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    try {
      const data = await actasApi.list();
      const filtrado = aplicarFiltros(data, leerFiltros());
      tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="8" class="text-center text-muted">Sin actas</td></tr>`;
      actualizarKpis(filtrado);
    } catch (err) {
      showError(err.message);
    }
  });

  document.addEventListener("actas:reload", async () => {
    const data = await actasApi.list();
    const filtrado = aplicarFiltros(data, leerFiltros());
    tbody.innerHTML = filtrado.map(row).join("");
    actualizarKpis(filtrado);
  });

  tbody.addEventListener("click", async (ev) => {
    const eliminar = ev.target.closest("[data-delete]");
    const accionEstado = ev.target.closest("[data-estado]");
    if (eliminar) {
      const id = eliminar.dataset.delete;
      if (!id) return;
      if (!window.confirm("¿Eliminar acta?")) return;
      try {
        await actasApi.remove(id);
        const data = await actasApi.list(leerFiltros());
        tbody.innerHTML = data.length ? data.map(row).join("") : `<tr><td colspan="8" class="text-center text-muted">Sin actas</td></tr>`;
        actualizarKpis(data);
      } catch (err) {
        showError(err.message);
      }
      return;
    }
    if (accionEstado) {
      actaSeleccionada = {
        id: accionEstado.dataset.id,
        codigo: accionEstado.dataset.codigo,
        nuevoEstado: accionEstado.dataset.estado
      };
      if (estadoInfo) estadoInfo.textContent = `Acta ${actaSeleccionada.codigo || actaSeleccionada.id}: cambiar a ${actaSeleccionada.nuevoEstado}`;
      if (estadoSelect) estadoSelect.value = actaSeleccionada.nuevoEstado;
      modal.modal("show");
    }
  });

  if (confirmEstadoBtn) {
    confirmEstadoBtn.addEventListener("click", async () => {
      if (!actaSeleccionada) return;
      const nuevoEstado = estadoSelect.value;
      try {
        const actual = await actasApi.getById(actaSeleccionada.id);
        const payload = { ...actual, estado: nuevoEstado };
        await actasApi.update(actaSeleccionada.id, payload);
        modal.modal("hide");
        const data = await actasApi.list(leerFiltros());
        const filtrado = aplicarFiltros(data, leerFiltros());
        tbody.innerHTML = filtrado.length ? filtrado.map(row).join("") : `<tr><td colspan="8" class="text-center text-muted">Sin actas</td></tr>`;
        actualizarKpis(filtrado);
      } catch (err) {
        showError(err.message);
      }
    });
  }
}

function actualizarKpis(data = []) {
  const emitidas = data.filter(a => (a.estado || "").toUpperCase().includes("EMIT")).length;
  const registradas = data.filter(a => (a.estado || "").toUpperCase().includes("REG")).length;
  const anuladas = data.filter(a => (a.estado || "").toUpperCase().includes("ANU")).length;
  const set = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  };
  set("kpi-emitidas", emitidas);
  set("kpi-registradas", registradas);
  set("kpi-anuladas", anuladas);
}

document.addEventListener("DOMContentLoaded", main);
