import { getSession, clearSession } from "./session.js";

const session = getSession();

function redirect(target) {
  window.location.href = target;
}

function detectBase(path) {
  const idx = path.lastIndexOf("/app/");
  return idx >= 0 ? path.substring(0, idx + 5) : "/app/";
}

const base = detectBase(window.location.pathname);

if (!session) {
  if (!window.location.pathname.includes("/app/login.html")) {
    redirect(`${base}login.html`);
  }
} else {
  const role = (session.rol || "").toUpperCase();
  const isLogin = window.location.pathname.includes("/app/login.html");
  const isRoot = window.location.pathname.endsWith("/app/") || window.location.pathname.endsWith("/app/index.html");
  if (isLogin || isRoot) {
    routeByRole(role, session);
  }
}

function routeByRole(role, session) {
  const targets = {
    ADMIN_GLOBAL: `${base}index.html`,
    TECNICO_GLOBAL: `${base}pages/dapcom/tecnico/menu.html`,
    CLIENTE_ADMIN: `${base}pages/cliente/admin/index.html`,
    CLIENTE_VISOR: `${base}pages/equipos/list.html`,
    TECNICO_CLIENTE: `${base}pages/tecnico/menu.html`
  };
  const target = targets[role];
  if (!target) return;
  const current = window.location.pathname + window.location.search;
  const targetPath = new URL(target, window.location.origin).pathname;
  if (current === targetPath || window.location.pathname === targetPath) return;
  redirect(target);
}

const logoutBtn = document.getElementById("logout-btn");
if (logoutBtn) {
  logoutBtn.addEventListener("click", (ev) => {
    ev.preventDefault();
    clearSession();
    redirect(`${base}login.html`);
  });
}

document.addEventListener("click", (ev) => {
  const btn = ev.target.closest("[data-logout],#logout-btn");
  if (!btn) return;
  ev.preventDefault();
  clearSession();
  redirect(`${base}login.html`);
});

export function enforceRole(allowedRoles) {
  if (!session) {
    redirect(`${base}login.html`);
    return null;
  }
  const role = (session.rol || "").toUpperCase();
  if (allowedRoles.length && !allowedRoles.includes(role)) {
    clearSession();
    redirect(`${base}login.html`);
    return null;
  }
  return session;
}

export function getCurrentSession() {
  return session;
}

export function getAccessScope() {
  const params = new URLSearchParams(window.location.search);
  const role = (session?.rol || "").toUpperCase();
  const scope = {
    role,
    clienteId: null,
    empresaId: null,
    lockCliente: false,
    lockEmpresa: false
  };
  const paramCliente = params.get("clienteId");
  const paramEmpresa = params.get("empresaId");

  if (role === "ADMIN_GLOBAL") {
    scope.clienteId = paramCliente;
    scope.empresaId = paramEmpresa;
    return scope;
  }

  if (role === "TECNICO_GLOBAL") {
    scope.clienteId = paramCliente || null;
    scope.empresaId = paramEmpresa || null;
    scope.lockCliente = Boolean(paramCliente);
    scope.lockEmpresa = Boolean(paramEmpresa);
    return scope;
  }

  if (role === "TECNICO_CLIENTE") {
    scope.clienteId = session?.clienteId || paramCliente || null;
    scope.empresaId = session?.empresaId || paramEmpresa || null;
    scope.lockCliente = true;
    scope.lockEmpresa = Boolean(scope.empresaId);
    return scope;
  }

  if (role === "CLIENTE_ADMIN" || role === "CLIENTE_VISOR") {
    scope.clienteId = session?.clienteId || paramCliente || null;
    scope.empresaId = paramEmpresa || session?.empresaId || null;
    scope.lockCliente = true;
    scope.lockEmpresa = false;
    return scope;
  }

  scope.clienteId = paramCliente || session?.clienteId || null;
  scope.empresaId = paramEmpresa || session?.empresaId || null;
  return scope;
}


