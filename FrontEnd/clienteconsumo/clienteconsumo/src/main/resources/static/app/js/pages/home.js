import { loadLayout } from "../ui/render.js";
import { showError } from "../ui/alerts.js";
import { equiposApi } from "../api/equipos.api.js";
import { mantenimientosApi } from "../api/mantenimientos.api.js";
import { movimientosApi } from "../api/movimientos.api.js";
import { clientesApi } from "../api/clientes.api.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

async function main() {
  try {
    await loadLayout("inicio");
  } catch (err) {
    await fallbackLayout();
  }
  initProtocolGuard();
  initTooltips();
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
    el.src = `${basePath()}../assets/${normalized}`;
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
  try {
    const [equiposRes, mantRes, movRes, clientesRes] = await Promise.allSettled([
      equiposApi.list(),
      mantenimientosApi.list(),
      movimientosApi.list(),
      clientesApi.list()
    ]);

    const equiposData = equiposRes.status === "fulfilled" ? equiposRes.value : [];
    const mantData = mantRes.status === "fulfilled" ? mantRes.value : [];
    const movData = movRes.status === "fulfilled" ? movRes.value : [];
    const clientesData = clientesRes.status === "fulfilled" ? clientesRes.value : [];

    const equipos = (equiposData || []).filter((e) => (e.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
    const countByEstado = (estado) =>
      equipos.filter((e) => (e.estado || e.status || e.statusequipo || e.statusEquipo || "").toUpperCase() === estado).length;
    const fechaHace24h = new Date(Date.now() - 24 * 60 * 60 * 1000);

    const mantActivos = (mantData || []).filter(
      (m) =>
        (m.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL &&
        (m.estado || "").toUpperCase() !== INACTIVO_INTERNAL
    );
    const mov24h = (movData || []).filter((m) => {
      const f = m.fecha ? new Date(m.fecha) : null;
      return f && f >= fechaHace24h;
    });
    const clientesActivos = (clientesData || []).filter((c) => (c.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
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

    bindKpiLinks();
  } catch (err) {
  }
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
document.addEventListener("DOMContentLoaded", loadKpis);
