import { loadLayout } from "../ui/render.js";
import { showError } from "../ui/alerts.js";
import { getDashboardData, clearDashboardCache, fetchAdjuntosRecientes } from "../viewdashboard/dashboard.datasource.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

async function main() {
  try {
    await loadLayout("inicio");
  } catch (err) {
    await fallbackLayout();
  }
  initProtocolGuard();
  initTooltips();
  await renderDashboard();
}

function basePath() {
  const path = window.location.pathname;
  if (!path.includes("/app/")) return "/";
  return path.split("/app/")[0] + "/app/";
}

async function fallbackLayout() {
  const base = basePath();
  await Promise.all([
    manualInject("[data-include='header']", `${base}layout/header.html`),
    manualInject("[data-include='sidebar']", `${base}layout/sidebar.html`),
    manualInject("[data-include='footer']", `${base}layout/footer.html`)
  ]);
  manualPatchAssets();
  manualPatchNav();
  initSidebarFallback();
}

async function manualInject(selector, url) {
  const host = document.querySelector(selector);
  if (!host) return;
  const res = await fetch(`${url}?v=${Date.now()}`);
  const html = await res.text();
  host.innerHTML = html;
}

function manualPatchAssets() {
  document.querySelectorAll("[data-asset-src]").forEach((el) => {
    const raw = el.dataset.assetSrc || "";
    const normalized = raw.replace(/^\/?assets\/?/, "");
    el.src = `${basePath()}/assets/${normalized}`;
  });
}

function manualPatchNav() {
  document.querySelectorAll("[data-path]").forEach((link) => {
    const target = link.dataset.path || "";
    link.href = `${basePath()}${target}`;
  });
}

function initSidebarFallback() {
  const sidebar = document.querySelector(".side-nav");
  if (!sidebar) return;
  let overlay = document.querySelector(".app-drawer-overlay");
  if (!overlay) {
    overlay = document.createElement("button");
    overlay.type = "button";
    overlay.className = "app-drawer-overlay";
    overlay.setAttribute("aria-label", "Cerrar menú");
    document.body.appendChild(overlay);
  }
  const closeSidebar = () => document.body.classList.remove("sidebar-open");
  const toggleSidebar = () => document.body.classList.toggle("sidebar-open");
  document.querySelectorAll("[data-sidebar-toggle]").forEach((btn) => {
    btn.addEventListener("click", toggleSidebar);
  });
  overlay.addEventListener("click", closeSidebar);
  sidebar.querySelectorAll("a[data-menu], a[data-path]").forEach((link) => {
    link.addEventListener("click", closeSidebar);
  });
  window.addEventListener("resize", () => {
    if (window.innerWidth >= 992) closeSidebar();
  });
}

function initProtocolGuard() {
  if (window.location.protocol === "file:") {
    showError("Sirve el sitio desde http://localhost:8090/app/index.html para evitar CORS de file://");
  }
}

function initTooltips() {
  if (window.$ && $.fn.tooltip) {
    $('[data-toggle="tooltip"]').tooltip();
  }
}

async function loadKpis() {
  const data = await getDashboardData();
  const equipos = data.equipos.filter((e) => (e.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
  const countByEstado = (estado) =>
    equipos.filter((e) => (e.estado || e.status || e.statusequipo || e.statusEquipo || "").toUpperCase() === estado).length;
  const fechaHace24h = new Date(Date.now() - 24 * 60 * 60 * 1000);

  const mantActivos = data.mantenimientos.filter(
    (m) =>
      (m.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL &&
      (m.estado || "").toUpperCase() !== INACTIVO_INTERNAL
  );
  const mov24h = data.movimientos.filter((m) => {
    const f = m.fecha ? new Date(m.fecha) : null;
    return f && f >= fechaHace24h;
  });
  const clientesActivos = data.clientes.filter((c) => (c.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
  const usuariosActivos = new Set(
    equipos
      .map((e) => e.idUsuario)
      .filter((id) => id !== null && id !== undefined)
  );

  setKpi("kpi-activos", countByEstado("ACTIVO"));
  setKpi("kpi-mantenimiento", mantActivos.length);
  setKpi("kpi-traslado", mov24h.length);
  setKpi(
    "kpi-baja",
    equipos.filter(
      (e) => (e.estadoInterno || "").toUpperCase() === "INACTIVO_INTERNAL" || (e.estado || "").toUpperCase() === "BAJA"
    ).length
  );
  setKpi("kpi-usuarios", usuariosActivos.size);
  setKpi("kpi-clientes", clientesActivos.length);
  setKpi("kpi-adjuntos", data.actas.length);

  bindKpiLinks();
}

function setKpi(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = value ?? "--";
}

function bindKpiLinks() {
  document.querySelectorAll("[data-kpi-link]").forEach((btn) => {
    const estado = btn.dataset.kpiLink;
    btn.addEventListener("click", () => {
      
      if (estado && estado.toUpperCase() === "MANTENIMIENTO") {
        const target = new URL("./pages/mantenimientos/list.html", window.location.href);
        window.location.href = target.toString();
        return;
      }
      const target = new URL("./pages/equipos/list.html", window.location.href);
      if (estado) target.searchParams.set("estado", estado);
      window.location.href = target.toString();
    });
  });
}

document.addEventListener("DOMContentLoaded", main);
function empresaNombre(empresas, id) {
  const e = empresas.find((em) => String(em.id) === String(id) || String(em.idCliente) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
}

function badgeEstadoActa(estado) {
  const e = (estado || "").toUpperCase();
  if (e.includes("ANU")) return "badge badge-danger";
  if (e.includes("CER")) return "badge badge-primary";
  if (e.includes("EMI")) return "badge badge-success";
  return "badge badge-warning text-dark";
}

function iconoMovimiento(tipo) {
  const t = (tipo || "").toUpperCase();
  if (t === "TRASLADO") return { icon: "las la-random text-primary", label: "Traslado" };
  if (t === "ENTREGA" || t === "ASIGNACION") return { icon: "las la-arrow-right text-success", label: "Entrega" };
  if (t === "RETIRO") return { icon: "las la-arrow-left text-warning", label: "Retiro" };
  if (t === "ANULACION") return { icon: "las la-ban text-danger", label: "Anulación" };
  return { icon: "las la-exchange-alt text-info", label: tipo || "Movimiento" };
}

function setProgress(idCount, idBar, value, total) {
  const countEl = document.getElementById(idCount);
  if (countEl) countEl.textContent = value ?? "--";
  const bar = document.getElementById(idBar);
  if (bar) {
    const pct = total > 0 ? Math.min(100, Math.round((value / total) * 100)) : 0;
    bar.style.width = `${pct}%`;
  }
}

function setList(containerId, itemsHtml) {
  const el = document.getElementById(containerId);
  if (!el) return;
  el.innerHTML = itemsHtml || `<div class="timeline-item text-muted">Sin datos</div>`;
}

async function renderDashboard() {
  clearDashboardCache();
  const data = await getDashboardData();
  await loadKpis();

  const actasOrdenadas = [...data.actas].sort((a, b) => new Date(b.fechaActa || b.fecha || 0) - new Date(a.fechaActa || a.fecha || 0));
  const actasBody = document.getElementById("actas-recientes-body");
  if (actasBody) {
    const rows = actasOrdenadas.slice(0, 4).map((a) => {
      return `<tr>
        <td>${a.codigo || a.id || ""}</td>
        <td>${empresaNombre(data.empresas, a.empresaId || a.idCliente)}</td>
        <td>${a.entregadoPor || a.emisor || ""}</td>
        <td>${a.recibidoPor || a.receptor || ""}</td>
        <td><span class="${badgeEstadoActa(a.estado)}">${(a.estado || "").toUpperCase()}</span></td>
        <td>${a.fechaActa || a.fecha || ""}</td>
      </tr>`;
    });
    actasBody.innerHTML = rows.length ? rows.join("") : `<tr><td colspan="6" class="text-center text-muted">Sin actas</td></tr>`;
  }

  const movList = document.getElementById("movimientos-recientes-list");
  if (movList) {
    const movs = [...data.movimientos].sort((a, b) => new Date(b.fecha || 0) - new Date(a.fecha || 0)).slice(0, 4);
    const items = movs.map((m) => {
      const icon = iconoMovimiento(m.tipo);
      const fechaTxt = m.fecha ? new Date(m.fecha).toLocaleString() : "";
      return `<li class="list-group-item d-flex align-items-center">
        <i class="${icon.icon} mr-3"></i>
        <div>
          <strong>${icon.label}</strong> ${m.serieEquipo || ""} ${m.ubicacionDestino ? "→ " + m.ubicacionDestino : ""}
          <div class="text-muted small">${m.ejecutadoPorId ? "Usuario " + m.ejecutadoPorId : ""}${fechaTxt ? " - " + fechaTxt : ""}</div>
        </div>
      </li>`;
    });
    movList.innerHTML = items.length ? items.join("") : `<li class="list-group-item text-center text-muted">Sin movimientos</li>`;
  }

  const adjBody = document.getElementById("adjuntos-recientes-body");
  let adjuntosCount = 0;
  if (adjBody) {
    const adjuntos = await fetchAdjuntosRecientes(actasOrdenadas);
    adjuntosCount = adjuntos.length;
    const rows = adjuntos.map((a) => {
      return `<tr>
        <td>${a.actaCodigo || a.actaId || ""}</td>
        <td>${a.nombre || a.archivo || a.url || ""}</td>
        <td>${a.tipo || ""}</td>
        <td>${a.id || ""}</td>
      </tr>`;
    });
    adjBody.innerHTML = rows.length ? rows.join("") : `<tr><td colspan="4" class="text-center text-muted">Sin adjuntos</td></tr>`;
  }

  const backlog = actasOrdenadas.filter((a) => (a.estado || "").toUpperCase() === "REGISTRADA" || (a.estado || "").toUpperCase() === "EMITIDA").length;
  const movHoy = data.movimientos.filter((m) => {
    if (!m.fecha) return false;
    const f = new Date(m.fecha);
    const today = new Date();
    return f.getFullYear() === today.getFullYear() && f.getMonth() === today.getMonth() && f.getDate() === today.getDate();
  }).length;
  const adjCount = adjuntosCount;
  const incidentesCount = data.incidentes.length;
  setProgress("backlog-actas", "backlog-actas-bar", backlog, data.actas.length || 1);
  setProgress("movimientos-hoy", "movimientos-hoy-bar", movHoy, data.movimientos.length || 1);
  setProgress("adjuntos-pendientes", "adjuntos-pendientes-bar", adjCount, Math.max(adjCount, 1));
  setProgress("incidentes-abiertos", "incidentes-abiertos-bar", incidentesCount, Math.max(incidentesCount, 1));

  const incidentesList = data.incidentes.slice(0, 3).map((i) => {
    const fecha = i.fechaIncidente || i.fecha || "";
    return `<div class="timeline-item">
      <div class="d-flex justify-content-between">
        <strong>INC-${i.id || ""}</strong>
        <span class="badge badge-danger">Pendiente</span>
      </div>
      <p class="mb-1 text-muted">${i.detalle || ""}</p>
      <small>${fecha}</small>
    </div>`;
  }).join("");
  setList("incidentes-list", incidentesList);

  const actasCompletadas = actasOrdenadas.filter((a) => (a.estado || "").toUpperCase() === "CERRADA").length;
  const movMes = data.movimientos.filter((m) => {
    if (!m.fecha) return false;
    const f = new Date(m.fecha);
    const desde = new Date();
    desde.setDate(desde.getDate() - 30);
    return f >= desde;
  }).length;
  const equiposLiberados = data.equipos.filter(
    (e) => (e.estadoInterno || "").toUpperCase() === INACTIVO_INTERNAL || (e.estado || "").toUpperCase() === "BAJA"
  ).length;
  setKpi("actas-completadas", actasCompletadas);
  setKpi("movimientos-log", movMes);
  setKpi("equipos-liberados", equiposLiberados);

  const equiposActivos = data.equipos.filter(
    (e) => (e.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL && (e.estado || "").toUpperCase() !== "BAJA"
  );

  const clientesList = document.getElementById("resumen-clientes");
  if (clientesList) {
    const agrupado = equiposActivos.reduce((acc, e) => {
      const key = e.empresaId || e.idCliente;
      if (!key) return acc;
      acc[key] = (acc[key] || 0) + 1;
      return acc;
    }, {});
    const clientesOrdenados = Object.entries(agrupado)
      .map(([id, count]) => ({ id, count, nombre: empresaNombre(data.empresas, id) }))
      .sort((a, b) => b.count - a.count)
      .map(
        (c) =>
          `<li class="list-group-item d-flex justify-content-between align-items-center">
            ${c.nombre}
            <span class="badge badge-primary badge-pill">${c.count} equipos</span>
          </li>`
      )
      .join("");
    clientesList.innerHTML = clientesOrdenados || `<li class="list-group-item text-center text-muted">Sin equipos</li>`;
    const badgeClientes = document.getElementById("badge-clientes-total");
    if (badgeClientes) badgeClientes.textContent = Object.keys(agrupado).length || "--";
  }

  const ubicacionesList = document.getElementById("ubicaciones-destacadas");
  if (ubicacionesList) {
    const agrupado = equiposActivos.reduce((acc, e) => {
      const key = (e.ubicacionUsuario || e.ubicacion || "SIN UBICACION").toUpperCase();
      acc[key] = (acc[key] || 0) + 1;
      return acc;
    }, {});
    const ubicacionesOrdenadas = Object.entries(agrupado)
      .map(([nombre, count]) => ({ nombre, count }))
      .sort((a, b) => b.count - a.count)
      .map(
        (u) =>
          `<div class="list-group-item d-flex align-items-center">
            <i class="las la-map-marker-alt text-primary mr-3"></i>
            <div>
              <strong>${u.nombre}</strong>
              <div class="text-muted small">${u.count} equipos</div>
            </div>
          </div>`
      )
      .join("");
    ubicacionesList.innerHTML = ubicacionesOrdenadas || `<div class="list-group-item text-center text-muted">Sin ubicaciones</div>`;
    const badgeUbicaciones = document.getElementById("badge-ubicaciones-total");
    if (badgeUbicaciones) badgeUbicaciones.textContent = Object.keys(agrupado).length || "--";
  }
}


