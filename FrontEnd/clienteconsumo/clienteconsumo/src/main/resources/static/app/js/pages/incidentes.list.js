import { loadLayout } from "../ui/render.js";
import { incidentesApi } from "../api/incidentes.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";

function getParam(name) {
  const url = new URL(window.location.href);
  return url.searchParams.get(name);
}

function renderTable(data) {
  const tbody = document.querySelector("#incidentes-tbody");
  if (!tbody) return;
  if (!data.length) {
    tbody.innerHTML = `<tr><td colspan="11" class="text-center text-muted">Sin datos</td></tr>`;
    return;
  }
  tbody.innerHTML = data
    .map(
      (i) => `
      <tr>
        <td>
          <div class="btn-group btn-group-sm" role="group">
            <a class="btn btn-outline-primary" href="../incidentes/form.html?id=${i.id}">Editar</a>
            <button class="btn btn-outline-danger btn-del" data-id="${i.id}">Eliminar</button>
          </div>
        </td>
        <td>${i.id ?? ""}</td>
        <td>${i.serieEquipo ?? ""}</td>
        <td>${i.idUsuario ?? ""}</td>
        <td>${i.fechaIncidente ? String(i.fechaIncidente).replace("T", " ").slice(0, 19) : ""}</td>
        <td>${i.detalle ?? ""}</td>
        <td>${i.costo ?? ""}</td>
        <td>${i.tecnico ?? ""}</td>
        <td>${i.responsable ?? ""}</td>
      </tr>
    `
    )
    .join("");
}

async function main() {
  await loadLayout("incidentes");
  const serieParam = getParam("serie") || "";
  let idClienteParam = getParam("idCliente") || "";
  const inputSerie = document.querySelector("#serieFilter");
  const btnNuevo = document.querySelector("#btn-nuevo-inc");
  if (inputSerie) inputSerie.value = serieParam;
  const buildNuevoHref = () => {
    if (!btnNuevo) return;
    const qs = [];
    const serieVal = (inputSerie?.value || serieParam || "").trim();
    if (serieVal) qs.push(`serie=${encodeURIComponent(serieVal)}`);
    if (idClienteParam) qs.push(`idCliente=${encodeURIComponent(idClienteParam)}`);
    btnNuevo.href = qs.length ? `../incidentes/form.html?${qs.join("&")}` : "../incidentes/form.html";
  };
  buildNuevoHref();

  if (!idClienteParam && serieParam) {
    try {
      const equipo = await equiposApi.getBySerie(serieParam);
      if (equipo?.idCliente) {
        idClienteParam = String(equipo.idCliente);
        buildNuevoHref();
      }
    } catch (err) {
      
    }
  }

  let incidentes = [];
  const load = async () => {
    const serieVal = (inputSerie?.value || "").trim();
    try {
      const data = serieVal ? await incidentesApi.listBySerie(serieVal) : await incidentesApi.list();
      incidentes = (data || []).filter((i) => (i.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
      renderTable(incidentes);
    } catch (err) {
      showError("No se pudo cargar incidentes");
    }
  };

  const form = document.querySelector("#filter-form");
  if (form) {
    form.addEventListener("submit", (ev) => {
      ev.preventDefault();
      buildNuevoHref();
      load();
    });
  }

  
  document.addEventListener("click", async (ev) => {
    const btn = ev.target.closest(".btn-del");
    if (!btn) return;
    const id = btn.getAttribute("data-id");
    if (!id) return;
    if (!confirm("¿Eliminar incidente?")) return;
    try {
      await incidentesApi.remove(id);
      showSuccess("Incidente eliminado");
      load();
    } catch (err) {
      showError("No se pudo eliminar");
    }
  });

  await load();
}

document.addEventListener("DOMContentLoaded", main);
