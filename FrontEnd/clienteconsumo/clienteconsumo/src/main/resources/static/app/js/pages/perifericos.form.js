import { loadLayout } from "../ui/render.js";
import { perifericosApi } from "../api/perifericos.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const getParam = (name) => new URL(window.location.href).searchParams.get(name) || "";
const toUpper = (val) => (val ? val.trim().toUpperCase() : "");
const clean = (val) => (val ? val.trim() : null);

document.addEventListener("DOMContentLoaded", main);

async function main() {
  await loadLayout("perifericos");
  const form = document.querySelector("#periferico-form");
  if (!form) return;

  const serieInput = form.serieEquipo;
  const seriesList = document.querySelector("#seriesList");
  const clienteSelect = document.querySelector("#clienteSelect");
  const equipoHidden = document.querySelector("#idEquipo");
  const title = document.querySelector("#form-title");

  const idParam = getParam("id");
  const serieParam = getParam("serie");
  const clienteParam = getParam("idCliente");

  let equipos = [];
  let clientes = [];

  const setCliente = (clienteId) => {
    if (clienteSelect) clienteSelect.value = clienteId ? String(clienteId) : "";
  };

  const renderSeries = () => {
    if (!seriesList) return;
    const clienteSel = clienteSelect?.value || "";
    const opciones = equipos
      .filter((e) => !clienteSel || String(e.idCliente) === String(clienteSel))
      .map((e) => {
        const serie = toUpper(e.serie || e.serieEquipo || "");
        return serie ? `<option value="${serie}">${serie}</option>` : "";
      })
      .filter(Boolean);
    seriesList.innerHTML = `<option value="⬇SUGERENCIAS⬇"></option>${opciones.join("")}`;
  };

  const hydrateFromSerie = (serieVal) => {
    const serieUpper = toUpper(serieVal);
    const eq = equipos.find((e) => toUpper(e.serie || e.serieEquipo) === serieUpper);
    if (eq) {
      if (equipoHidden) equipoHidden.value = eq.id ?? "";
      setCliente(eq.idCliente);
      renderSeries();
    }
  };

  const loadClientes = async () => {
    try {
      clientes = await clientesApi.list();
      if (clienteSelect) {
        clienteSelect.innerHTML =
          `<option value="">Seleccione cliente</option>` +
          clientes.map((c) => `<option value="${c.id}">${c.nombre || c.razonSocial || c.id}</option>`).join("");
      }
    } catch (err) {
      showError("No se pudieron cargar clientes");
    }
  };

  const loadEquipos = async () => {
    try {
      equipos = await equiposApi.list();
    } catch (err) {
      showError("No se pudieron cargar equipos");
    }
  };

  if (serieParam && serieInput) {
    serieInput.value = toUpper(serieParam);
  }

  await Promise.all([loadClientes(), loadEquipos()]);
  if (clienteParam && clienteSelect) {
    clienteSelect.value = clienteParam;
  }
  renderSeries();
  hydrateFromSerie(serieInput?.value);

  if (clienteSelect) {
    clienteSelect.addEventListener("change", () => {
      renderSeries();
      const currentSerie = serieInput?.value || "";
      if (currentSerie) {
        const pertenece = equipos.some(
          (e) =>
            String(e.idCliente) === String(clienteSelect.value || "") &&
            toUpper(e.serie || e.serieEquipo) === toUpper(currentSerie)
        );
        if (!pertenece && serieInput) {
          serieInput.value = "";
          if (equipoHidden) equipoHidden.value = "";
        }
      }
    });
  }

  if (idParam && serieParam) {
    if (title) title.textContent = "Editar periférico";
    try {
      const data = await perifericosApi.get(idParam, serieParam);
      if (serieInput) serieInput.value = toUpper(data.serieEquipo || serieParam);
      hydrateFromSerie(serieInput?.value);
      if (equipoHidden) equipoHidden.value = data.id ?? idParam;
      setCliente(data.idCliente);
      if (serieInput) serieInput.readOnly = true;
      if (clienteSelect) clienteSelect.disabled = true;
      form.serieMonitor.value = data.serieMonitor ?? "";
      form.activoMonitor.value = data.activoMonitor ?? "";
      form.marcaMonitor.value = data.marcaMonitor ?? "";
      form.modeloMonitor.value = data.modeloMonitor ?? "";
      form.observacionMonitor.value = data.observacionMonitor ?? "";
      form.serieTeclado.value = data.serieTeclado ?? "";
      form.activoTeclado.value = data.activoTeclado ?? "";
      form.marcaTeclado.value = data.marcaTeclado ?? "";
      form.modeloTeclado.value = data.modeloTeclado ?? "";
      form.observacionTeclado.value = data.observacionTeclado ?? "";
      form.serieMouse.value = data.serieMouse ?? "";
      form.activoMouse.value = data.activoMouse ?? "";
      form.marcaMouse.value = data.marcaMouse ?? "";
      form.modeloMouse.value = data.modeloMouse ?? "";
      form.observacionMouse.value = data.observacionMouse ?? "";
      form.serieTelefono.value = data.serieTelefono ?? "";
      form.activoTelefono.value = data.activoTelefono ?? "";
      form.marcaTelefono.value = data.marcaTelefono ?? "";
      form.modeloTelefono.value = data.modeloTelefono ?? "";
      form.observacionTelefono.value = data.observacionTelefono ?? "";
    } catch (err) {
      showError("No se pudo cargar el periférico");
    }
  }

  serieInput?.addEventListener("input", () => {
    const upper = toUpper(serieInput.value);
    serieInput.value = upper;
    hydrateFromSerie(upper);
  });

  form.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const serieVal = toUpper(form.serieEquipo.value);
    hydrateFromSerie(serieVal);
    if (!serieVal) {
      showError("La serie del equipo es obligatoria");
      return;
    }
    if (!clienteSelect?.value) {
      showError("Selecciona un equipo válido para asociar cliente.");
      return;
    }
    const payload = {
      id: equipoHidden?.value ? Number(equipoHidden.value) : (idParam ? Number(idParam) : null),
      serieEquipo: serieVal,
      serieMonitor: clean(form.serieMonitor.value),
      activoMonitor: toUpper(form.activoMonitor.value) || null,
      marcaMonitor: clean(form.marcaMonitor.value),
      modeloMonitor: clean(form.modeloMonitor.value),
      observacionMonitor: clean(form.observacionMonitor.value),
      serieTeclado: clean(form.serieTeclado.value),
      activoTeclado: toUpper(form.activoTeclado.value) || null,
      marcaTeclado: clean(form.marcaTeclado.value),
      modeloTeclado: clean(form.modeloTeclado.value),
      observacionTeclado: clean(form.observacionTeclado.value),
      serieMouse: clean(form.serieMouse.value),
      activoMouse: toUpper(form.activoMouse.value) || null,
      marcaMouse: clean(form.marcaMouse.value),
      modeloMouse: clean(form.modeloMouse.value),
      observacionMouse: clean(form.observacionMouse.value),
      serieTelefono: clean(form.serieTelefono.value),
      activoTelefono: toUpper(form.activoTelefono.value) || null,
      marcaTelefono: clean(form.marcaTelefono.value),
      modeloTelefono: clean(form.modeloTelefono.value),
      observacionTelefono: clean(form.observacionTelefono.value),
      clientePerifericos: clienteSelect?.selectedOptions?.[0]?.textContent || null,
      idCliente: clienteSelect?.value ? Number(clienteSelect.value) : null
    };
    try {
      if (idParam) {
        await perifericosApi.update(idParam, serieVal, payload);
        showSuccess("Periférico actualizado");
      } else {
        await perifericosApi.create(payload);
        showSuccess("Periférico registrado");
      }
      setTimeout(() => {
        window.location.href = `./list.html?serie=${encodeURIComponent(serieVal)}`;
      }, 400);
    } catch (err) {
      showError(err.message || "Error al guardar periférico");
    }
  });
}
