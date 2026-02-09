import { loadLayout } from "../ui/render.js";
import { equiposApi } from "../api/equipos.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { clientesApi } from "../api/clientes.api.js";
import { showError, showSuccess } from "../ui/alerts.js";
import { enforceRole, getAccessScope } from "../auth.js";

const INACTIVO_INTERNAL = "INACTIVO_INTERNAL";
const KPI_IDS = { activos: "kpi-activos", mantenimiento: "kpi-mantenimiento", traslado: "kpi-traslado", baja: "kpi-baja" };
const qrCache = new Map();
const QR_SCALE = 4;
const allowedRoles = ["ADMIN_GLOBAL", "TECNICO_GLOBAL", "TECNICO_CLIENTE", "CLIENTE_ADMIN", "CLIENTE_VISOR"];
const session = enforceRole(allowedRoles);
const IS_VIEWER = (session?.rol || session?.rolCodigo || "").toUpperCase() === "CLIENTE_VISOR";
const scope = getAccessScope();
const scopeClienteId = scope?.clienteId ? String(scope.clienteId) : null;
const scopeEmpresaId = scope?.empresaId ? String(scope.empresaId) : null;

const FIELD_MAP = {
  id: ["id"],
  activo: ["activo", "codigoInterno", "codigo", "activoEquipo"],
  alias: ["alias"],
  nombre: ["nombre", "nombreEquipo"],
  serie: ["serie", "serieEquipo"],
  tipo: ["tipo", "tipoEquipo"],
  marca: ["marca", "marcaEquipo"],
  modelo: ["modelo", "modeloEquipo"],
  so: ["sistemaOperativo", "so"],
  procesador: ["procesador"],
  memoria: ["memoria"],
  hdd: ["disco"],
  estado: ["estado", "status", "statusequipo", "statusEquipo"],
  cliente: ["empresa", "nombreEmpresa", "cliente", "nombreCliente", "razonSocial", "empresaId"],
  empresa: ["empresa", "nombreEmpresa", "cliente", "nombreCliente", "razonSocial", "empresaId"],
  usuario: ["nombreUsuario", "usuario"],
  departamento: ["departamentoUsuario"],
  ubicacion: ["ubicacionUsuario"],
  ciudad: ["ciudad"],
  ip: ["ip"],
  office: ["office"],
  costo: ["costo"]
};

const ADVANCED_FILTERS = [
  { label: "ID", key: "id" },
  { label: "Activo", key: "activo" },
  { label: "Alias", key: "alias" },
  { label: "Nombre", key: "nombre" },
  { label: "Serie", key: "serie" },
  { label: "Tipo", key: "tipo" },
  { label: "Estado", key: "estado" },
  { label: "Marca", key: "marca" },
  { label: "Modelo", key: "modelo" },
  { label: "SO", key: "so" },
  { label: "Procesador", key: "procesador" },
  { label: "Memoria", key: "memoria" },
  { label: "HDD", key: "hdd" },
  { label: "Cliente", key: "cliente" },
  { label: "Empresa", key: "empresa" },
  { label: "Usuario", key: "usuario" },
  { label: "Departamento", key: "departamento" },
  { label: "Ubicacion", key: "ubicacion" },
  { label: "Ciudad", key: "ciudad" },
  { label: "Office", key: "office" },
  { label: "IP", key: "ip" },
  { label: "Costo", key: "costo" }
];

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
      if (IS_VIEWER) {
        return `
        <div class="d-flex align-items-center flex-wrap">
          <button class="btn btn-sm btn-outline-dark mr-1 mb-1" data-detalle-id="${e.id}" aria-label="Ver detalle"><i class="las la-eye mr-1"></i>Vista general</button>
          <a class="btn btn-sm btn-outline-primary mr-1 mb-1" href="./form.html?id=${e.id}" aria-label="Editar"><i class="las la-pen"></i>Editar</a>
          <a class="btn btn-sm btn-outline-secondary mr-1 mb-1" href="../incidentes/list.html?serie=${encodeURIComponent(serie)}" title="Ver incidentes" aria-label="Ver incidentes">
            <i class="las la-list mr-1"></i>Incidentes
          </a>
          <a class="btn btn-sm btn-outline-secondary mr-1 mb-1" href="../mantenimientos/list.html?serie=${encodeURIComponent(serie)}" title="Ver mantenimientos" aria-label="Ver mantenimientos">
            <i class="las la-tools mr-1"></i>Mantenimientos
          </a>
        </div>`;
      }
      return `
        <div class="d-flex align-items-center">
          <button class="btn btn-sm btn-outline-dark mr-1" data-detalle-id="${e.id}" aria-label="Ver detalle"><i class="las la-eye"></i></button>
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
let clientes = [];
let filtrosAvanzados = {};
let visibleColumns = new Set(COLUMNAS.map((c) => c.id));
let columnOrder = COLUMNAS.map((c) => c.id);
let ultimoFiltrado = [];
let currentPage = 1;
let pageSize = 25;
let paginationBar = null;
const ADV_KEY = "equipos_filtros_avanzados";
const ORDER_KEY = "equipos_order";
const sortState = { col: null, dir: "asc" };

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

function nombreCliente(id) {
  const c = clientes.find((cl) => String(cl.id) === String(id));
  return c ? (c.nombre || c.nombreCliente || c.razonSocial || c.id) : "";
}

function matchesScope(item = {}) {
  const clienteVal = item.idCliente ?? item.clienteId ?? item.idcliente;
  const empresaVal = item.empresaId ?? item.idEmpresa ?? item.idempresa;
  const role = (session?.rol || "").toUpperCase();
  if (role === "TECNICO_GLOBAL" || role === "ADMIN_GLOBAL") return true;
  if (scopeClienteId && clienteVal && String(clienteVal) !== scopeClienteId) return false;
  if (scopeEmpresaId && empresaVal && String(empresaVal) !== scopeEmpresaId) return false;
  return true;
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

  ADVANCED_FILTERS.forEach((f) => {
    const values = data
      .map((item) => displayValue(item, f.key))
      .filter(Boolean)
      .map((v) => v.toUpperCase());
    if (values.length) {
      grid.appendChild(buildFilterOption(f.label, Array.from(new Set(values)), f.key));
    }
  });
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
  columnOrder.forEach((colId) => {
    const col = COLUMNAS.find((c) => c.id === colId);
    if (!col) return;
    const id = `col-${col.id}`;
    const item = document.createElement("div");
    item.className = "custom-control custom-checkbox d-flex align-items-center draggable-col";
    item.setAttribute("draggable", "true");
    item.dataset.colId = col.id;
    item.innerHTML = `
      <span class="mr-2 text-muted drag-handle" style="cursor:grab">&#8942;</span>
      <input type="checkbox" class="custom-control-input" id="${id}" ${visibleColumns.has(col.id) ? "checked" : ""}>
      <label class="custom-control-label" for="${id}">${col.label || "Acciones"}</label>
    `;
    item.querySelector("input").addEventListener("change", (ev) => {
      if (ev.target.checked) visibleColumns.add(col.id);
      else visibleColumns.delete(col.id);
      saveColumnState();
      renderTable(ultimoFiltrado);
    });
    item.addEventListener("dragstart", (ev) => {
      ev.dataTransfer.setData("text/plain", col.id);
      ev.dropEffect = "move";
    });
    item.addEventListener("dragover", (ev) => ev.preventDefault());
    item.addEventListener("drop", (ev) => {
      ev.preventDefault();
      const fromId = ev.dataTransfer.getData("text/plain");
      const toId = ev.currentTarget.dataset.colId;
      reorderColumns(fromId, toId);
    });
    menu.appendChild(item);
  });
}

function saveColumnState() {
  localStorage.setItem("equipos_cols", JSON.stringify(Array.from(visibleColumns)));
}

function saveColumnOrder() {
  localStorage.setItem(ORDER_KEY, JSON.stringify(Array.from(columnOrder)));
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

function restoreColumnOrder() {
  const raw = localStorage.getItem(ORDER_KEY);
  if (!raw) return;
  try {
    const arr = JSON.parse(raw);
    if (Array.isArray(arr) && arr.length) {
      const valid = arr.filter((id) => COLUMNAS.some((c) => c.id === id));
      const missing = COLUMNAS.map((c) => c.id).filter((id) => !valid.includes(id));
      columnOrder = [...valid, ...missing];
    }
  } catch (e) {
    columnOrder = COLUMNAS.map((c) => c.id);
  }
}

function reorderColumns(fromId, toId) {
  if (fromId === toId) return;
  const current = [...columnOrder];
  const fromIdx = current.indexOf(fromId);
  const toIdx = current.indexOf(toId);
  if (fromIdx === -1 || toIdx === -1) return;
  current.splice(fromIdx, 1);
  current.splice(toIdx, 0, fromId);
  columnOrder = current;
  saveColumnOrder();
  renderColumnToggles();
  renderTable(ultimoFiltrado);
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

  const cols = columnOrder
    .map((id) => COLUMNAS.find((c) => c.id === id))
    .filter((c) => c && visibleColumns.has(c.id));
  thead.innerHTML = `<tr>${cols
    .map((c) => {
      const sortable = c.id !== "acciones";
      const indicator = sortState.col === c.id ? (sortState.dir === "asc" ? " ^" : " v") : "";
      return `<th ${sortable ? `data-col="${c.id}" class="sortable"` : ""}>${c.label}${indicator}</th>`;
    })
    .join("")}</tr>`;

  thead.querySelectorAll("th[data-col]").forEach((th) => {
    th.addEventListener("click", () => toggleSort(th.dataset.col));
  });

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
    if (IS_VIEWER) return;
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

  document.querySelectorAll("[data-detalle-id]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.dataset.detalleId);
      const eq = equipos.find((x) => Number(x.id) === id);
      if (eq) openDetalle(eq);
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
      const current = displayValue(e, key)?.toUpperCase();
      return current && values.includes(current);
    });

    return matchesSerie && matchesTipo && matchesEstado && matchesEmpresa && hayTexto && advancedOk;
  });

  renderTable(sortData(filtered));
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

function getFieldsFor(key) {
  return FIELD_MAP[key] || [key];
}

function displayValue(item, key) {
  if (key === "empresa") {
    const name =
      getVal(item, ["empresa", "nombreEmpresa", "razonSocial"]) ||
      nombreEmpresa(item.empresaId ?? item.idEmpresa ?? item.idcliente);
    if (name) return name;
  }
  if (key === "cliente") {
    const name =
      getVal(item, ["cliente", "nombreCliente"]) ||
      nombreCliente(item.idCliente ?? item.clienteId ?? item.idcliente);
    if (name) return name;
  }
  return getVal(item, getFieldsFor(key));
}

function getSortValue(item, colId) {
  const val = displayValue(item, colId);
  const num = Number(val);
  if (!Number.isNaN(num) && String(val).trim() !== "") return num;
  return String(val || "").toUpperCase();
}

function sortData(list) {
  if (!sortState.col) return list;
  const dir = sortState.dir === "desc" ? -1 : 1;
  return [...list].sort((a, b) => {
    const av = getSortValue(a, sortState.col);
    const bv = getSortValue(b, sortState.col);
    if (typeof av === "number" && typeof bv === "number") return av === bv ? 0 : av > bv ? dir : -dir;
    return String(av).localeCompare(String(bv)) * dir;
  });
}

function toggleSort(colId) {
  if (sortState.col === colId) {
    sortState.dir = sortState.dir === "asc" ? "desc" : "asc";
  } else {
    sortState.col = colId;
    sortState.dir = "asc";
  }
  applyFilters();
}

async function loadEmpresas() {
  try {
    empresas = scopeClienteId ? await empresasApi.listByCliente(scopeClienteId) : await empresasApi.list();
    if (scopeEmpresaId) {
      empresas = empresas.filter((e) => String(e.id) === scopeEmpresaId);
    }
    if (!empresas.length && (scopeClienteId || scopeEmpresaId)) {
      showError("No hay empresas disponibles para tu rol.");
    }
    const sel = document.querySelector("#fEmpresa");
    if (sel) {
      const placeholder = scopeClienteId ? "Empresas del cliente" : "Todas las empresas";
      sel.innerHTML = `<option value="">${placeholder}</option>` + empresas.map((c) => `<option value="${c.id}">${c.nombre ?? c.razonSocial ?? c.id}</option>`).join("");
      if (scopeEmpresaId) {
        sel.value = scopeEmpresaId;
        sel.disabled = true;
      }
    }
  } catch (err) {
    showError("No se pudo cargar empresas: " + err.message);
  }
}

async function loadClientes() {
  try {
    const data = await clientesApi.list();
    clientes = Array.isArray(data) ? data : [];
  } catch (err) {
    clientes = [];
  }
}

async function loadEquipos() {
  try {
    const data = await equiposApi.list();
    const scoped = (data || []).filter(matchesScope);
    equipos = scoped.filter((e) => (e.estadoInterno || "").toUpperCase() !== INACTIVO_INTERNAL);
    const totalEl = document.querySelector("#total-equipos");
    if (totalEl) totalEl.textContent = equipos.length;
    updateKpis(equipos);
    buildAdvancedFilters(equipos);
    restoreColumnState();
    restoreColumnOrder();
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

async function copyToClipboard(text) {
  if (!text && text !== "0") return;
  try {
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(text);
      return;
    }
    } catch (err) {
  }
  const ta = document.createElement("textarea");
  ta.value = text;
  ta.style.position = "fixed";
  ta.style.top = "-1000px";
  ta.style.left = "-1000px";
  document.body.appendChild(ta);
  ta.focus();
  ta.select();
  try {
    document.execCommand("copy");
  } catch (err) {
    console.warn("copy fallback failed", err);
  }
  document.body.removeChild(ta);
}

function openDetalle(e) {
  const detalleBody = document.getElementById("detalle-body");
  if (!detalleBody) return;

  const rows = [
    { label: "Cliente", val: getVal(e, ["empresa", "nombreEmpresa", "cliente", "nombreCliente", "razonSocial"]) },
    { label: "Activo N°", val: getVal(e, ["activo", "codigoInterno", "codigo", "activoEquipo"]) },
    { label: "Nombre Equipo", val: getVal(e, ["nombre", "nombreEquipo"]) },
    { label: "Serie", val: getVal(e, ["serie", "serieEquipo"]) },
    { label: "Marca", val: getVal(e, ["marca", "marcaEquipo"]) },
    { label: "Modelo", val: getVal(e, ["modelo", "modeloEquipo"]) },
    { label: "Tipo", val: getVal(e, ["tipo", "tipoEquipo"]) },
    { label: "Estado", val: getVal(e, ["estado", "status", "statusequipo", "statusEquipo"]) },
    { label: "Ciudad", val: getVal(e, ["ciudad"]) },
    { label: "Usuario", val: getVal(e, ["nombreUsuario", "usuario"]) },
    { label: "Departamento", val: getVal(e, ["departamentoUsuario"]) },
    { label: "Ubicación", val: getVal(e, ["ubicacionUsuario"]) },
    { label: "SO", val: getVal(e, ["sistemaOperativo", "so"]) },
    { label: "Procesador", val: getVal(e, ["procesador"]) },
    { label: "Memoria", val: getVal(e, ["memoria"]) },
    { label: "HDD", val: getVal(e, ["disco"]) },
    { label: "Fecha de compra", val: getVal(e, ["fechaCompra", "fechaAdquisicion"]) },
    { label: "Office", val: getVal(e, ["office"]) },
    { label: "IP", val: getVal(e, ["ip"]) },
    { label: "Costo", val: getVal(e, ["costo"]) },
    { label: "N° Factura", val: getVal(e, ["numeroFactura", "factura"]) },
    { label: "Nota", val: getVal(e, ["nota", "observaciones"]) }
  ];

  const copyIcon = (val) =>
    `<button class="btn btn-sm btn-link p-0 ml-1 text-secondary copy-one" data-copy="${(val || "").replace(/"/g, "&quot;")}"><i class="las la-clipboard"></i></button>`;

  const col1 = rows.slice(0, Math.ceil(rows.length / 2));
  const col2 = rows.slice(Math.ceil(rows.length / 2));

  const buildCol = (data) =>
    data
      .map(
        (r) => `
        <p class="mb-2 d-flex align-items-center">
          <strong class="mr-1">${r.label}:</strong>
          <span>${r.val || "-"}</span>
          ${copyIcon(r.val)}
        </p>`
      )
      .join("");

  detalleBody.innerHTML = `
    <div class="row">
      <div class="col-md-6">${buildCol(col1)}</div>
      <div class="col-md-6">${buildCol(col2)}</div>
    </div>`;

  detalleBody.querySelectorAll(".copy-one").forEach((btn) => {
    btn.addEventListener("click", () => copyToClipboard(btn.dataset.copy || ""));
  });

  const btnCopiar = document.getElementById("btn-copiar-detalle");
  if (btnCopiar) {
    btnCopiar.onclick = () => {
      const text = rows.map((r) => `${r.label}: ${r.val || "-"}`).join("\n");
      copyToClipboard(text);
    };
  }

  $("#detalleModal").modal("show");
}

async function main() {
  if (!session) return;
  await loadLayout("equipos");

  bindFilters();
  await loadClientes();
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


