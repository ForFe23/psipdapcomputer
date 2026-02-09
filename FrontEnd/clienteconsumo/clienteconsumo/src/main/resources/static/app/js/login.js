import { setSession } from "./session.js";
import { authApi } from "./api/auth.api.js";

const form = document.getElementById("login-form");
const errorBox = document.getElementById("login-error");
const btn = document.getElementById("login-btn");

const mapRol = (codigo = "") => {
  const r = codigo.toUpperCase();
  if (r === "ADMIN_GLOBAL") return "ADMIN_GLOBAL";
  if (r === "TECNICO_GLOBAL") return "TECNICO_GLOBAL";
  if (r === "CLIENTE_ADMIN") return "CLIENTE_ADMIN";
  if (r === "CLIENTE_VISOR") return "CLIENTE_VISOR";
  if (r === "TECNICO_CLIENTE") return "TECNICO_CLIENTE";
  if (r === "TRGRTNRS") return "CLIENTE_ADMIN";
  if (r === "CMPRSGRS") return "CLIENTE_VISOR";
  if (r === "BLTRCPLS") return "TECNICO_CLIENTE";
  return null;
};

const showError = (msg) => {
  errorBox.textContent = msg;
  errorBox.classList.remove("d-none");
};

const hideError = () => errorBox.classList.add("d-none");

form.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  hideError();
  const email = form.usuario.value.trim().toLowerCase();
  const pass = form.solfrnrf.value.trim();
  if (!email || !pass) return;
  btn.disabled = true;
  btn.textContent = "Verificando...";
  try {
    const auth = await authApi.login(email, pass);
    const rol = mapRol(auth.rol || "");
    if (!rol) throw new Error("Rol no soportado");
    const payload = {
      rol,
      clienteId: auth.clienteId ?? null,
      empresaId: auth.empresaId ?? null,
      usuario: auth.correo || email,
      idUsuario: auth.idUsuario ?? null,
      token: auth.token
    };
    setSession(payload);
    window.location.href = "./index.html";
  } catch (err) {
    showError(err?.message || "No se pudo validar. Revisa conexión.");
  } finally {
    btn.disabled = false;
    btn.textContent = "Entrar";
  }
});


