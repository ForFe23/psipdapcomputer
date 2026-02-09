import { getSession } from "../session.js";
import "../auth.js";

function computeBases() {
  const path = window.location.pathname;
  const idx = path.lastIndexOf("/app/");
  const afterApp = idx >= 0 ? path.substring(idx + 5) : "";
  const segments = afterApp.split("/").filter(Boolean);
  const ups = Math.max(segments.length - 1, 0);
  const appBase = "../".repeat(ups);
  const assetsBase = `${appBase}assets`.replace(/\/{2,}/g, "/");
  return { appBase, assetsBase };
}

const base = computeBases();

async function inject(selector, path) {
  const host = document.querySelector(selector);
  if (!host) return;
  const res = await fetch(`${base.appBase}${path}?v=${Date.now()}`);
  const html = await res.text();
  host.innerHTML = html;
}

function setActive(activeMenu) {
  if (!activeMenu) return;
  document.querySelectorAll("[data-menu]").forEach((link) => {
    if (link.dataset.menu === activeMenu) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });
}

function patchAssets() {
  document.querySelectorAll("[data-asset-src]").forEach((el) => {
    const raw = el.dataset.assetSrc || "";
    const normalized = raw.replace(/^\/?assets\/?/, "");
    el.src = `${base.assetsBase}/${normalized}`.replace(/\/{2,}/g, "/").replace(":/", "://");
  });
}

function patchNav() {
  document.querySelectorAll("[data-path]").forEach((link) => {
    const target = link.dataset.path || "";
    link.href = `${base.appBase}${target}`;
  });
}

function ensureSidebarOverlay() {
  let overlay = document.querySelector(".app-drawer-overlay");
  if (!overlay) {
    overlay = document.createElement("button");
    overlay.type = "button";
    overlay.className = "app-drawer-overlay";
    overlay.setAttribute("aria-label", "Cerrar menú");
    overlay.dataset.sidebarOverlay = "true";
    document.body.appendChild(overlay);
  }
  return overlay;
}

function bindSidebarToggle() {
  const sidebar = document.querySelector(".side-nav");
  if (!sidebar) return;
  const overlay = ensureSidebarOverlay();
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
  document.addEventListener("keydown", (ev) => {
    if (ev.key === "Escape") closeSidebar();
  });
}

function hideMenus(menus = []) {
  if (!menus.length) return;
  menus.forEach((menu) => {
    document.querySelectorAll(`[data-menu='${menu}']`).forEach((link) => {
      link.classList.add("d-none");
    });
  });
}

function applyRoleUi() {
  const session = getSession();
  const role = (session?.rol || "").toUpperCase();
  document.body.dataset.role = role || "";
  if (!role) return;

  if (role === "ADMIN_GLOBAL") {
    document.body.classList.add("role-admin");
    return;
  }

  if (role === "TECNICO_GLOBAL" || role === "TECNICO_CLIENTE") {
    document.body.classList.add("role-tecnico");
    hideMenus([
      "inicio",
      "global",
      "clientes",
      "empresas",
      "usuarios",
      "roles",
      "personas",
      "ubicaciones",
      "kardex",
      "reporteria",
      "perifericos",
      "adjuntos"
    ]);
    return;
  }

  if (role === "CLIENTE_ADMIN" || role === "CLIENTE_VISOR") {
    document.body.classList.add("role-cliente");
    const hideList = ["clientes", "usuarios", "roles", "global"];
    if (role === "CLIENTE_VISOR") {
      hideList.push(
        "perifericos",
        "actas",
        "movimientos",
        "mantenimientos",
        "adjuntos",
        "empresas",
        "personas",
        "ubicaciones",
        "kardex",
        "reporteria",
        "plantilla",
        "inicio"
      );
    }
    hideMenus(hideList);
  }
}

export async function loadLayout(activeMenu) {
  await Promise.all([
    inject("[data-include='header']", "layout/header.html"),
    inject("[data-include='sidebar']", "layout/sidebar.html"),
    inject("[data-include='footer']", "layout/footer.html")
  ]);
  patchAssets();
  patchNav();
  setActive(activeMenu);
  bindSidebarToggle();
  applyRoleUi();
}


