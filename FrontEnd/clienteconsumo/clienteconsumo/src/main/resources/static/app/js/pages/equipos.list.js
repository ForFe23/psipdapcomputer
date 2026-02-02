import { loadLayout } from "../ui/render.js";
import { equiposApi } from "../api/equipos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";
const KPI_IDS = { activos: "kpi-activos", mantenimiento: "kpi-mantenimiento", traslado: "kpi-traslado", baja: "kpi-baja" };
const qrCache = new Map();
const QR_SCALE = 4;

const COLUMNAS = [
  {
    id: "qr",
    label: "QR",
    render: (e) => {
      const payload = qrPayload(e);
      return `
        <button class="btn btn-link p-0 qr-cell" data-label-id="${e.id}" title="Imprimir etiqueta">
          <img class="lazy-qr" data-qr="${payload}" alt="QR" width="64" height="64" loading="lazy" decoding="async">
        </button>`;
    }
  },
  { id: "id", label: "ID", render: (e) => e.id ?? "" },
  { id: "activo", label: "Activo", render: (e) => getVal(e, ["activo", "codigoInterno"]) },
  { id: "alias", label: "Alias", render: (e) => getVal(e, ["alias"]) },
  { id: "nombre", label: "Nombre", render: (e) => getVal(e, ["nombre", "nombreEquipo"]) },
  { id: "serie", label: "Serie", render: (e) => getVal(e, ["serie", "serieEquipo"]) },
  { id: "tipo", label: "Tipo", render: (e) => getVal(e, ["tipo", "tipoEquipo"]) },
  { id: "marca", label: "Marca", render: (e) => getVal(e, ["marca", "marcaEquipo"]) },
  { id: "modelo", label: "Modelo", render: (e) => getVal(e, ["modelo", "modeloEquipo"]) },
  { id: "so", label: "SO", render: (e) => getVal(e, ["sistemaOperativo", "so"]) },
  { id: "procesador", label: "Procesador", render: (e) => getVal(e, ["procesador"]) },
  { id: "memoria", label: "Memoria", render: (e) => getVal(e, ["memoria"]) },
  { id: "hdd", label: "HDD", render: (e) => getVal(e, ["disco"]) },
  { id: "estado", label: "Estado", render: (e) => getVal(e, ["estado", "status", "statusequipo", "statusEquipo"]) },
  { id: "cliente", label: "Empresa", render: (e) => getVal(e, ["empresa", "nombreEmpresa", "cliente", "nombreCliente", "razonSocial"]) || nombreEmpresa(e.empresaId ?? e.idCliente) },
  { id: "usuario", label: "Usuario", render: (e) => getVal(e, ["nombreUsuario", "usuario"]) },
  { id: "departamento", label: "Departamento", render: (e) => getVal(e, ["departamentoUsuario"]) },
  { id: "ubicacion", label: "Ubicación", render: (e) => getVal(e, ["ubicacionUsuario"]) },
  { id: "ip", label: "IP", render: (e) => getVal(e, ["ip"]) },
  { id: "office", label: "Office", render: (e) => getVal(e, ["office"]) },
  { id: "costo", label: "Costo", render: (e) => getVal(e, ["costo"]) },
  {
    id: "acciones",
    label: "",
    render: (e) => {
      const serie = getVal(e, ["serie", "serieEquipo"]);
      return `
        <div class="d-flex align-items-center">
          <a class="btn btn-sm btn-outline-primary mr-1" href="./form.html?id=${e.id}" aria-label="Editar"><i class="las la-pen"></i></a>
          <a class="btn btn-sm btn-outline-secondary mr-1" href="../incidentes/list.html?serie=${encodeURIComponent(serie)}" title="Ver incidentes" aria-label="Ver incidentes">
            <i class="las la-list mr-1"></i>Ver incidentes
          </a>
          <button class="btn btn-sm btn-outline-danger" data-delete-id="${e.id}" data-delete-serie="${serie}" aria-label="Eliminar"><i class="las la-trash"></i></button>
        </div>
      `;
    }
  }
];

let equipos = [];
let empresas = [];
let filtrosAvanzados = {};
let visibleColumns = new Set(COLUMNAS.map((c) => c.id));
let ultimoFiltrado = [];
let currentPage = 1;
let pageSize = 25;
let paginationBar = null;
const ADV_KEY = "equipos_filtros_avanzados";

const getVal = (obj, keys) => {
  for (const k of keys) {
    const v = obj[k];
    if (v !== undefined && v !== null && String(v).trim() !== "") return String(v);
  }
  return "";
};

const qrPayload = (e) => getVal(e, ["codigoInterno", "activo", "codigo", "activoEquipo", "serie", "serieEquipo", "id"]) || String(e.id || "");

const qrUrl = (e) => qrDataUrl(qrPayload(e));

function qrDataUrl(data) {
  if (!data) return "";
  if (qrCache.has(data)) return qrCache.get(data);
  let url = "";
  try {
    if (typeof window.qrcode === "function") {
      const qr = window.qrcode(0, "L");
      qr.addData(data);
      qr.make();
      url = qr.createDataURL(QR_SCALE);
    }
  } catch (err) {
    console.warn("QR local gen fallo, usando remoto", err);
  }
  if (!url) {
    url = `https://api.qrserver.com/v1/create-qr-code/?size=80x80&data=${encodeURIComponent(data)}`;
  }
  qrCache.set(data, url);
  return url;
}

function nombreEmpresa(id) {
  const c = empresas.find((cl) => String(cl.id) === String(id));
  return c ? (c.nombre || c.razonSocial || c.id) : "";
}

function updateKpis(source) {
  const setVal = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val ?? 0;
  };
  const countByEstado = (estado) =>
    source.filter((e) => (getVal(e, ["estado", "status", "statusequipo", "statusEquipo"]) || "").toUpperCase() === estado).length;

  setVal(KPI_IDS.activos, countByEstado("ACTIVO"));
  setVal(KPI_IDS.mantenimiento, countByEstado("MANTENIMIENTO"));
  setVal(KPI_IDS.traslado, countByEstado("TRASLADO"));
  setVal(KPI_IDS.baja, countByEstado("BAJA"));
}

function buildAdvancedFilters(data) {
  restoreAdvancedFilters();
  const grid = document.getElementById("filter-grid");
  if (!grid) return;
  grid.innerHTML = "";
  const collect = (keyPaths) => {
    const vals = new Set();
    data.forEach((e) => {
      keyPaths.forEach((k) => {
        const val = getVal(e, [k]);
        if (val) vals.add(val.toUpperCase());
      });
    });
    return Array.from(vals);
  };

  const filters = [
    { label: "Activo", key: "activo", values: collect(["activo", "codigoInterno"]) },
    { label: "Alias", key: "alias", values: collect(["alias"]) },
    { label: "Nombre", key: "nombre", values: collect(["nombre", "nombreEquipo"]) },
    { label: "Serie", key: "serie", values: collect(["serie", "serieEquipo"]) },
    { label: "Tipo", key: "tipo", values: collect(["tipo", "tipoEquipo"]) },
    { label: "Estado", key: "estado", values: collect(["estado", "status", "statusequipo", "statusEquipo"]) },
    { label: "Marca", key: "marca", values: collect(["marca", "marcaEquipo"]) },
    { label: "Modelo", key: "modelo", values: collect(["modelo", "modeloEquipo"]) },
    { label: "SO", key: "sistemaOperativo", values: collect(["sistemaOperativo", "so"]) },
    { label: "Procesador", key: "procesador", values: collect(["procesador"]) },
    { label: "Departamento", key: "departamentoUsuario", values: collect(["departamentoUsuario"]) },
    { label: "Ubicación", key: "ubicacionUsuario", values: collect(["ubicacionUsuario"]) },
    { label: "Ciudad", key: "ciudad", values: collect(["ciudad"]) },
    { label: "Office", key: "office", values: collect(["office"]) },
    { label: "Usuario", key: "nombreUsuario", values: collect(["nombreUsuario", "usuario"]) }
  ];

  filters.forEach((f) => grid.appendChild(buildFilterOption(f.label, f.values, f.key)));
}

function buildFilterOption(label, values, key) {
  const container = document.createElement("div");
  container.className = "filter-block";
  const id = `fadv-${key}`;
  container.innerHTML = `
    <label class="small text-uppercase text-muted mb-1" for="${id}">${label}</label>
    <select id="${id}" class="form-control form-control-sm" multiple></select>
  `;
  const select = container.querySelector("select");
  values.sort().forEach((v) => {
    const opt = document.createElement("option");
    opt.value = v;
    opt.textContent = v;
    if (filtrosAvanzados[key] && filtrosAvanzados[key].includes(v)) {
      opt.selected = true;
    }
    select.appendChild(opt);
  });
  select.addEventListener("change", () => {
    filtrosAvanzados[key] = Array.from(select.selectedOptions).map((o) => o.value);
    saveAdvancedFilters();
  });
  return container;
}

function renderColumnToggles() {
  const menu = document.getElementById("columnas-menu");
  if (!menu) return;
  menu.innerHTML = "";
  COLUMNAS.forEach((col) => {
    const id = `col-${col.id}`;
    const item = document.createElement("div");
    item.className = "custom-control custom-checkbox";
    item.innerHTML = `
      <input type="checkbox" class="custom-control-input" id="${id}" ${visibleColumns.has(col.id) ? "checked" : ""}>
      <label class="custom-control-label" for="${id}">${col.label || "Acciones"}</label>
    `;
    item.querySelector("input").addEventListener("change", (ev) => {
      if (ev.target.checked) visibleColumns.add(col.id);
      else visibleColumns.delete(col.id);
      saveColumnState();
      renderTable(ultimoFiltrado);
    });
    menu.appendChild(item);
  });
}

function saveColumnState() {
  localStorage.setItem("equipos_cols", JSON.stringify(Array.from(visibleColumns)));
}

function saveAdvancedFilters() {
  localStorage.setItem(ADV_KEY, JSON.stringify(filtrosAvanzados));
}

function restoreAdvancedFilters() {
  const raw = localStorage.getItem(ADV_KEY);
  if (!raw) {
    filtrosAvanzados = {};
    return;
  }
  try {
    const obj = JSON.parse(raw);
    filtrosAvanzados = obj && typeof obj === "object" ? obj : {};
  } catch (e) {
    filtrosAvanzados = {};
  }
}

function restoreColumnState() {
  const raw = localStorage.getItem("equipos_cols");
  if (!raw) return;
  try {
    const arr = JSON.parse(raw);
    if (Array.isArray(arr) && arr.length) {
      visibleColumns = new Set(arr);
    }
  } catch (e) {
    
  }
}

function renderTable(data) {
  ultimoFiltrado = data;
  const tbody = document.querySelector("#equipos-tbody");
  const thead = document.querySelector("#equipos-thead");
  if (!tbody || !thead) return;

  
  const total = data.length;
  const paginable = total > 10;
  if (paginationBar) {
    paginationBar.classList.toggle("d-none", !paginable);
  }
  const effectivePageSize = paginable ? pageSize : total || 10;
  const pageTotal = Math.max(1, Math.ceil(total / effectivePageSize));
  if (currentPage > pageTotal) currentPage = pageTotal;
  const start = (currentPage - 1) * effectivePageSize;
  const pageData = data.slice(start, start + effectivePageSize);

  const cols = COLUMNAS.filter((c) => visibleColumns.has(c.id));
  thead.innerHTML = `<tr>${cols.map((c) => `<th>${c.label}</th>`).join("")}</tr>`;

  if (!pageData.length) {
    tbody.innerHTML = `<tr><td colspan="${cols.length}" class="text-center text-muted">Sin datos</td></tr>`;
  } else {
    tbody.innerHTML = pageData
      .map((e) => `<tr>${cols.map((c) => `<td>${c.render(e) ?? ""}</td>`).join("")}</tr>`)
      .join("");
  }

  const info = document.getElementById("page-info");
  if (info) info.textContent = `${total === 0 ? 0 : start + 1}-${Math.min(start + effectivePageSize, total)} de ${total}`;

  document.querySelectorAll("[data-delete-id]").forEach((btn) => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.deleteId;
      if (!confirm("¿Eliminar equipo " + id + "?")) return;
      try {
        await equiposApi.remove(id);
        showSuccess("Equipo eliminado");
        equipos = equipos.filter((eq) => String(eq.id) !== String(id));
        applyFilters();
      } catch (err) {
        showError(err.message);
      }
    });
  });

  document.querySelectorAll("[data-label-id]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.dataset.labelId);
      const eq = equipos.find((x) => Number(x.id) === id);
      if (eq) openLabel(eq);
    });
  });

  initLazyQrRender();
}

function applyFilters() {
  const serie = (document.querySelector("#fSerie")?.value || "").trim().toLowerCase();
  const tipo = (document.querySelector("#fTipo")?.value || "").trim().toLowerCase();
  const estado = (document.querySelector("#fEstado")?.value || "").trim().toLowerCase();
  const empresaId = (document.querySelector("#fEmpresa")?.value || "").trim();
  const text = (document.querySelector("#fTexto")?.value || "").trim().toLowerCase();

  const filtered = equipos.filter((e) => {
    const estadoInterno = (e.estadoInterno || "").toUpperCase();
    if (estadoInterno === INACTIVO_INTERNAL) return false;
    const matchesSerie = !serie || getVal(e, ["serie", "serieEquipo"]).toLowerCase().includes(serie);
    const matchesTipo = !tipo || getVal(e, ["tipo", "tipoEquipo"]).toLowerCase().includes(tipo);
    const matchesEstado = !estado || getVal(e, ["estado", "status", "statusequipo", "statusEquipo"]).toLowerCase() === estado;
    const matchesEmpresa = !empresaId || String(e.empresaId ?? e.idCliente) === empresaId;
    const hayTexto =
      !text ||
      getVal(e, ["codigoInterno", "codigo", "activo", "activoEquipo"]).toLowerCase().includes(text) ||
      getVal(e, ["modelo", "modeloEquipo"]).toLowerCase().includes(text) ||
      getVal(e, ["marca", "marcaEquipo"]).toLowerCase().includes(text);

    const advancedOk = Object.entries(filtrosAvanzados).every(([key, values]) => {
      if (!values || !values.length) return true;
      const current = getVal(e, [key])?.toUpperCase();
      return current && values.includes(current);
    });

    return matchesSerie && matchesTipo && matchesEstado && matchesEmpresa && hayTexto && advancedOk;
  });

  renderTable(filtered);
  const totalEl = document.querySelector("#total-equipos");
  if (totalEl) totalEl.textContent = filtered.length;
  updateKpis(equipos);
}

function initLazyQrRender() {
  const images = Array.from(document.querySelectorAll("img[data-qr]"));
  if (!images.length) return;

  const renderImg = (img) => {
    const data = img.dataset.qr;
    if (!data) return;
    img.src = qrDataUrl(data);
    img.removeAttribute("data-qr");
  };

  if (!("IntersectionObserver" in window)) {
    images.forEach(renderImg);
    return;
  }

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          renderImg(entry.target);
          observer.unobserve(entry.target);
        }
      });
    },
    { rootMargin: "120px 0px", threshold: 0.1 }
  );

  images.forEach((img, idx) => {
    img.style.setProperty("--reveal-delay", `${0.01 * idx}s`);
    observer.observe(img);
  });
}

function bindFilters() {
  const formFiltros = document.querySelector("#filtros-equipos");
  if (formFiltros) {
    formFiltros.addEventListener("submit", (ev) => {
      ev.preventDefault();
      applyFilters();
    });
  }
  ["fSerie", "fTipo", "fEstado", "fEmpresa", "fTexto"].forEach((id) => {
    const el = document.querySelector("#" + id);
    if (el) el.addEventListener("input", applyFilters);
  });
}

async function loadEmpresas() {
  try {
    empresas = await empresasApi.list();
    const sel = document.querySelector("#fEmpresa");
    if (sel) sel.innerHTML = `<option value="">Todas las empresas</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre ?? c.razonSocial ?? c.id}</option>`).join("");
  } catch (err) {
    showError("No se pudo cargar empresas: " + err.message);
  }
}

async function loadEquipos() {
  try {
    const data = await equiposApi.list();
    equipos = (data || []).filter((e) => (e.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
    const totalEl = document.querySelector("#total-equipos");
    if (totalEl) totalEl.textContent = equipos.length;
    updateKpis(equipos);
    buildAdvancedFilters(equipos);
    restoreColumnState();
    renderColumnToggles();
    const savedSize = localStorage.getItem("equipos_page_size");
    if (savedSize && !Number.isNaN(Number(savedSize))) pageSize = Number(savedSize);
    if (equipos.length <= 10) {
      currentPage = 1;
      pageSize = equipos.length || 10;
    }
    applyFilters();
  } catch (err) {
    showError(err.message);
  }
}

function openLabel(e) {
  const payload = qrPayload(e);
  const qrImg = qrDataUrl(payload);
  const html = `
    <html>
    <head>
      <style>
        body { font-family: Arial, sans-serif; margin: 12px; }
        .label { display: flex; align-items: center; border: 1px dashed #999; padding: 12px; width: 320px; }
        .qr img { width: 100px; height: 100px; }
        .meta { margin-left: 12px; }
        .title { font-weight: 700; font-size: 16px; margin: 0 0 4px 0; }
        .small { font-size: 12px; margin: 0; }
      </style>
    </head>
    <body onload="window.print()">
      <div class="label">
        <div class="qr"><img src="${qrImg}" /></div>
        <div class="meta">
          <p class="title">${getVal(e, ["codigoInterno", "serie", "serieEquipo"]) || "Equipo"}</p>
          <p class="small">Serie: ${getVal(e, ["serie", "serieEquipo"]) || "-"}</p>
          <p class="small">Activo N°: ${getVal(e, ["codigoInterno", "activo", "codigo", "activoEquipo"]) || "-"}</p>
          <p class="small">Marca/Modelo: ${getVal(e, ["marca", "marcaEquipo"]) || "-"} ${getVal(e, ["modelo", "modeloEquipo"]) || ""}</p>
          <p class="small">Estado: ${getVal(e, ["estado", "status", "statusequipo", "statusEquipo"]) || "-"}</p>
        </div>
      </div>
    </body>
    </html>`;
  const w = window.open("", "_blank", "width=400,height=260");
  w.document.write(html);
  w.document.close();
}

async function main() {
  await loadLayout("equipos");
  bindFilters();
  await loadEmpresas();
  await loadEquipos();

  paginationBar = document.getElementById("pagination-bar");
  const btnOpen = document.getElementById("btn-filtros-avanzados");
  const btnClose = document.getElementById("btn-cerrar-filtros");
  const btnReset = document.getElementById("btn-reset-filtros");
  const btnAplicar = document.getElementById("btn-aplicar-filtros");
  const panel = document.getElementById("filter-panel");
  const backdrop = document.getElementById("filter-backdrop");
  const pagePrev = document.getElementById("page-prev");
  const pageNext = document.getElementById("page-next");
  const pageSizeSel = document.getElementById("page-size");

  const togglePanel = (show) => {
    if (!panel || !backdrop) return;
    panel.classList.toggle("open", show);
    backdrop.classList.toggle("open", show);
  };

  btnOpen?.addEventListener("click", () => togglePanel(true));
  btnClose?.addEventListener("click", () => togglePanel(false));
  backdrop?.addEventListener("click", () => togglePanel(false));
  btnReset?.addEventListener("click", () => {
    filtrosAvanzados = {};
    document.querySelectorAll("#filter-grid select").forEach((s) => (s.selectedIndex = -1));
    saveAdvancedFilters();
    applyFilters();
  });
  btnAplicar?.addEventListener("click", () => {
    togglePanel(false);
    saveAdvancedFilters();
    applyFilters();
  });

  pagePrev?.addEventListener("click", () => {
    if (currentPage > 1) {
      currentPage -= 1;
      renderTable(ultimoFiltrado);
    }
  });
  pageNext?.addEventListener("click", () => {
    const total = ultimoFiltrado.length;
    const pageTotal = Math.max(1, Math.ceil(total / pageSize));
    if (currentPage < pageTotal) {
      currentPage += 1;
      renderTable(ultimoFiltrado);
    }
  });
  pageSizeSel?.addEventListener("change", () => {
    const val = Number(pageSizeSel.value);
    pageSize = !Number.isNaN(val) ? val : 25;
    localStorage.setItem("equipos_page_size", pageSize);
    currentPage = 1;
    renderTable(ultimoFiltrado);
  });
}

document.addEventListener("DOMContentLoaded", main);
