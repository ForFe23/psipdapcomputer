import { loadLayout } from "../../js/ui/render.js";
import { getDashboardData } from "../../js/viewdashboard/dashboard.datasource.js";
import { actaItemsApi } from "../../js/api/acta-items.api.js";

let dataCache = null;
let lastEquipos = [];
let lastIncidentes = [];
let lastPerifericos = [];

function normalize(str) {
  return (str || "").toString().trim();
}

function optionList(items, labelAll = "Todos") {
  const base = `<option value="">${labelAll}</option>`;
  const body = items
    .map((i) => {
      if (typeof i === "object") return `<option value="${i.value ?? ""}">${i.label ?? i.value ?? ""}</option>`;
      return `<option value="${i}">${i}</option>`;
    })
    .join("");
  return base + body;
}

function setSelect(id, values, labelAll) {
  const el = document.getElementById(id);
  if (!el) return;
  const current = el.value;
  el.innerHTML = optionList(values, labelAll);
  if (current && values.some((v) => String(v.value ?? v) === current)) {
    el.value = current;
  } else {
    el.value = "";
  }
}

function unique(list) {
  return Array.from(new Set(list.filter((v) => v && String(v).trim().length)));
}

function empresaNombre(empresas, id) {
  const e = empresas.find((em) => String(em.id) === String(id) || String(em.idCliente) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
}

function clienteNombre(clientes, id) {
  const c = clientes.find((cl) => String(cl.id) === String(id));
  return c ? c.nombre || c.razonSocial || c.id : id ?? "";
}

function statusEquipo(e) {
  return (e.estado || e.status || e.statusequipo || e.statusEquipo || "").toUpperCase();
}

function ubicacionEquipo(e) {
  return e.ubicacionUsuario || e.ubicacion || e.ubicacionActual || "";
}

function usuarioEquipo(e) {
  return e.nombreUsuario || e.usuario || e.idUsuario || "";
}

function fillFilters() {
  const empresas = dataCache.empresas || [];
  const clientes = dataCache.clientes || [];
  const equipos = dataCache.equipos || [];
  setSelect(
    "fClienteReporte",
    clientes.map((c) => ({ value: c.id, label: c.nombre || c.razonSocial || c.id })),
    "Todos los clientes"
  );
  refreshEmpresasPorCliente();
  setSelect(
    "fClienteInc",
    clientes.map((c) => ({ value: c.id, label: c.nombre || c.razonSocial || c.id })),
    "Todos los clientes"
  );
  setSelect("fMarca", unique(equipos.map((e) => e.marca || e.marcas || e.marcaEquipo)), "Todas las marcas");
  setSelect("fTipo", unique(equipos.map((e) => e.tipo || e.tipoEquipo)), "Todos los tipos");
  setSelect("fStatus", unique(equipos.map((e) => statusEquipo(e))), "Todos los estados");
  setSelect("fUbicacion", unique(equipos.map((e) => ubicacionEquipo(e))), "Todas las ubicaciones");
  setSelect("fUsuario", unique(equipos.map((e) => usuarioEquipo(e))), "Todos los usuarios");
  setSelect(
    "fClienteEquipo",
    clientsFromEquipos(equipos, clientes).map((id) => ({ value: id, label: clienteNombre(clientes, id) })),
    "Todos los clientes"
  );
  setSelect("fDepto", unique(equipos.map((e) => e.departamentoUsuario || e.departamento || e.depto || "")), "Todos los departamentos");
  setSelect("fCiudad", unique(equipos.map((e) => e.ciudad || e.city || "")), "Todas las ciudades");
  setSelect("fStatusInc", unique(dataCache.incidentes.map((i) => (i.estado || i.status || "").toUpperCase())), "Todos los status");
  preselectHighlands();
}

function refreshEmpresasPorCliente() {
  const empresas = dataCache.empresas || [];
  const clienteSel = document.getElementById("fClienteReporte")?.value || "";
  const empresasDestino = clienteSel
    ? empresas.filter((e) => String(e.idCliente || e.clienteId) === String(clienteSel))
    : empresas;
  const empresaOptions = empresasDestino.map((e) => ({ value: e.id, label: e.nombre || e.razonSocial || e.id }));
  setSelect("fEmpresaReporte", empresaOptions, clienteSel ? "Empresas del cliente" : "Todas las empresas");
  const empresaSelect = document.getElementById("fEmpresaReporte");
  if (empresaSelect) {
    empresaSelect.disabled = !clienteSel;
    if (!clienteSel) empresaSelect.value = "";
  }

  const empresasIncSelect = document.getElementById("fEmpresaInc");
  if (empresasIncSelect) {
    setSelect("fEmpresaInc", empresaOptions, clienteSel ? "Empresas del cliente" : "Todas las empresas");
    empresasIncSelect.disabled = !clienteSel;
    if (!clienteSel) empresasIncSelect.value = "";
  }
}

function clientsFromEquipos(equipos, clientes) {
  const ids = unique(equipos.map((e) => e.idCliente || e.clienteId));
  return ids.map((id) => clienteNombre(clientes, id));
}

function preselectHighlands() {
  const empresas = dataCache.empresas || [];
  const target = empresas.find((e) => ((e.nombre || e.razonSocial || "") + "").toUpperCase().includes("HIGHLANDS"));
  if (target) {
    ["fEmpresaReporte", "fEmpresaInc"].forEach((id) => {
      const el = document.getElementById(id);
      if (el) el.value = target.id;
    });
    setBadgeCliente(target.nombre || target.razonSocial || target.id);
  }
}

function filterEquipos() {
  const empresaSel = document.getElementById("fEmpresaReporte").value;
  const clienteSel = document.getElementById("fClienteReporte").value;
  const marcaSel = document.getElementById("fMarca").value;
  const tipoSel = document.getElementById("fTipo").value;
  const statusSel = document.getElementById("fStatus").value;
  const ubicSel = document.getElementById("fUbicacion").value;
  const usuarioSel = document.getElementById("fUsuario").value;
  const clienteEqSel = document.getElementById("fClienteEquipo").value;
  const aliasSel = (document.getElementById("fAlias").value || "").toUpperCase();
  const nombreSel = (document.getElementById("fNombre").value || "").toUpperCase();
  const serieSel = (document.getElementById("fSerie").value || "").toUpperCase();
  const modeloSel = (document.getElementById("fModelo").value || "").toUpperCase();
  const soSel = (document.getElementById("fSO").value || "").toUpperCase();
  const cpuSel = (document.getElementById("fCpu").value || "").toUpperCase();
  const deptoSel = document.getElementById("fDepto").value;
  const ciudadSel = document.getElementById("fCiudad").value;
  const officeSel = (document.getElementById("fOffice").value || "").toUpperCase();

  const empresas = dataCache.empresas || [];
  const clientes = dataCache.clientes || [];

  const filtered = dataCache.equipos.filter((e) => {
    const empresaId = e.empresaId || e.idCliente;
    const clienteId = e.idCliente || e.clienteId;
    const empresaTxt = empresaNombre(empresas, empresaId);
    const clienteTxt = clienteNombre(clientes, clienteId);
    const belongsCliente = !clienteSel || String(clienteId) === clienteSel;
    const belongsEmpresa = !empresaSel || String(empresaId) === empresaSel;
    if (!belongsCliente) return false;
    if (clienteSel && empresaSel && !belongsEmpresa) return false;
    if (!clienteSel && empresaSel) return false;
    if (marcaSel && (e.marca || e.marcas || e.marcaEquipo) !== marcaSel) return false;
    if (tipoSel && (e.tipo || e.tipoEquipo) !== tipoSel) return false;
    if (statusSel && statusEquipo(e) !== statusSel) return false;
    if (ubicSel && ubicacionEquipo(e) !== ubicSel) return false;
    if (usuarioSel && String(usuarioEquipo(e)) !== usuarioSel) return false;
    if (clienteEqSel && String(clienteId) !== clienteEqSel) return false;
    if (aliasSel && !(e.alias || "").toUpperCase().includes(aliasSel)) return false;
    if (nombreSel && !(e.nombre || e.alias || "").toUpperCase().includes(nombreSel)) return false;
    if (serieSel && !(e.serie || e.serieEquipo || "").toUpperCase().includes(serieSel)) return false;
    if (modeloSel && !(e.modelo || e.modeloEquipo || "").toUpperCase().includes(modeloSel)) return false;
    if (soSel && !(e.so || e.sistemaOperativo || "").toUpperCase().includes(soSel)) return false;
    if (cpuSel && !(e.procesador || e.cpu || "").toUpperCase().includes(cpuSel)) return false;
    if (deptoSel && (e.departamentoUsuario || e.departamento || e.depto || "") !== deptoSel) return false;
    if (ciudadSel && (e.ciudad || e.city || "") !== ciudadSel) return false;
    if (officeSel && !(e.office || e.suiteOffice || "").toUpperCase().includes(officeSel)) return false;
    return true;
  });

  renderEquiposTable(filtered, empresas, clientes);
  renderPerifericosAsync(filtered, empresas, clienteSel, empresaSel);
  const badgeTxt =
    empresaSel && empresaNombre(empresas, empresaSel)
      ? empresaNombre(empresas, empresaSel)
      : clienteSel && clienteNombre(clientes, clienteSel)
      ? clienteNombre(clientes, clienteSel)
      : "Todos";
  setBadgeCliente(badgeTxt);
  renderKpi(filtered);
}

function renderEquiposTable(rows, empresas, clientes) {
  const eqTable = document.querySelector("#tabla-equipos tbody");
  if (!eqTable) return;
  lastEquipos = rows;
  const html = rows
    .map(
      (e) => `<tr>
        <td>${e.id ?? ""}</td>
        <td>${empresaNombre(empresas, e.empresaId || e.idCliente)}</td>
        <td>${clienteNombre(clientes, e.idCliente || e.clienteId)}</td>
        <td>${e.serie || e.serieEquipo || ""}</td>
        <td>${e.alias || ""}</td>
        <td>${e.nombre || e.alias || ""}</td>
        <td>${e.marca || e.marcas || e.marcaEquipo || ""}</td>
        <td>${e.modelo || e.modeloEquipo || ""}</td>
        <td>${e.tipo || e.tipoEquipo || ""}</td>
        <td>${statusEquipo(e)}</td>
        <td>${e.so || e.sistemaOperativo || ""}</td>
        <td>${e.procesador || e.cpu || ""}</td>
        <td>${e.departamentoUsuario || e.departamento || e.depto || ""}</td>
        <td>${ubicacionEquipo(e)}</td>
        <td>${e.ciudad || e.city || ""}</td>
        <td>${e.office || e.suiteOffice || ""}</td>
        <td>${usuarioEquipo(e)}</td>
      </tr>`
    )
    .join("");
  eqTable.innerHTML = html || `<tr><td colspan="17" class="text-center text-muted">Sin datos</td></tr>`;
}

function filterIncidentes() {
  const empresaSel = document.getElementById("fEmpresaInc").value;
  const clienteSel = document.getElementById("fClienteInc").value;
  const statusSel = document.getElementById("fStatusInc").value;
  const empresas = dataCache.empresas || [];
  const clientes = dataCache.clientes || [];

  const filtered = dataCache.incidentes.filter((i) => {
    const empresaId = i.empresaId || i.idCliente;
    const clienteId = i.idCliente || i.clienteId;
    const empresaTxt = empresaNombre(empresas, empresaId);
    const clienteTxt = clienteNombre(clientes, clienteId);
    const statusTxt = (i.estado || i.status || "").toUpperCase();
    const belongsCliente = !clienteSel || String(clienteId) === clienteSel;
    const belongsEmpresa = !empresaSel || String(empresaId) === empresaSel;
    if (!belongsCliente) return false;
    if (clienteSel && empresaSel && !belongsEmpresa) return false;
    if (!clienteSel && empresaSel) return false;
    if (statusSel && statusTxt !== statusSel) return false;
    return true;
  });
  renderIncidentesTable(filtered, empresas, clientes);
}

function renderIncidentesTable(rows, empresas, clientes) {
  const incTable = document.querySelector("#tabla-incidentes tbody");
  if (!incTable) return;
  lastIncidentes = rows;
  const html = rows
    .map((i) => {
      return `<tr>
        <td>${i.id ?? ""}</td>
        <td>${empresaNombre(empresas, i.empresaId || i.idCliente)}</td>
        <td>${clienteNombre(clientes, i.idCliente || i.clienteId)}</td>
        <td>${i.serieEquipo || i.equipoSerie || ""}</td>
        <td>${(i.estado || i.status || "").toUpperCase()}</td>
        <td>${i.detalle || ""}</td>
        <td>${i.fechaIncidente || i.fecha || ""}</td>
      </tr>`;
    })
    .join("");
  incTable.innerHTML = html || `<tr><td colspan="7" class="text-center text-muted">Sin incidentes</td></tr>`;
}

function renderPerifericos(rows, empresas) {
  const perTable = document.querySelector("#tabla-perifericos tbody");
  if (!perTable) return;
  lastPerifericos = rows;
  const html = rows
    .map(
      (d) =>
        `<tr>
          <td>${d.empresa}</td>
          <td>${d.equipoSerie}</td>
          <td>${d.marca}</td>
          <td>${d.modelo}</td>
          <td>${d.tipo}</td>
          <td>${d.observacion}</td>
        </tr>`
    )
    .join("");
  perTable.innerHTML = html || `<tr><td colspan="6" class="text-center text-muted">Sin datos</td></tr>`;
}

function perifericosRows() {
  return lastPerifericos || [];
}

async function renderPerifericosAsync(equipRows, empresas, clienteSel, empresaSel) {
  const actaItems = await fetchActaItems(clienteSel, empresaSel);
  renderPerifericos(actaItems, empresas);
}

async function fetchActaItems(clienteSel, empresaSel) {
  const actas = dataCache?.actas || [];
  const targetActas = actas.filter((a) => {
    if (!clienteSel && empresaSel) return false;
    const belongsCliente = !clienteSel || String(a.idCliente) === String(clienteSel);
    const belongsEmpresa = !empresaSel || String(a.empresaId) === String(empresaSel);
    return belongsCliente && belongsEmpresa;
  });
  const items = await Promise.all(
    targetActas.map(async (a) => {
      try {
        const list = await actaItemsApi.list(a.id);
        return (list || []).map((it) => ({
          empresa: empresaNombre(dataCache.empresas || [], a.empresaId || a.idCliente),
          cliente: a.idCliente,
          equipoSerie: it.serie || it.equipoSerie || "",
          marca: it.marca || "",
          modelo: it.modelo || "",
          tipo: it.tipo || "",
          observacion: it.observacion || ""
        }));
      } catch {
        return [];
      }
    })
  );
  return items.flat();
}

function equiposColumns() {
  const empresas = dataCache?.empresas || [];
  const clientes = dataCache?.clientes || [];
  return [
    { label: "ID", get: (e) => e.id },
    { label: "Empresa", get: (e) => empresaNombre(empresas, e.empresaId || e.idCliente) },
    { label: "Cliente", get: (e) => clienteNombre(clientes, e.idCliente || e.clienteId) },
    { label: "Serie", get: (e) => e.serie || e.serieEquipo },
    { label: "Alias", get: (e) => e.alias || "" },
    { label: "Nombre", get: (e) => e.nombre || e.alias || "" },
    { label: "Marca", get: (e) => e.marca || e.marcas || e.marcaEquipo },
    { label: "Modelo", get: (e) => e.modelo || e.modeloEquipo },
    { label: "Tipo", get: (e) => e.tipo || e.tipoEquipo },
    { label: "Estado", get: (e) => statusEquipo(e) },
    { label: "Sistema operativo", get: (e) => e.so || e.sistemaOperativo || "" },
    { label: "Procesador", get: (e) => e.procesador || e.cpu || "" },
    { label: "Departamento", get: (e) => e.departamentoUsuario || e.departamento || e.depto || "" },
    { label: "Ubicacion", get: (e) => ubicacionEquipo(e) },
    { label: "Ciudad", get: (e) => e.ciudad || e.city || "" },
    { label: "Office", get: (e) => e.office || e.suiteOffice || "" },
    { label: "Usuario", get: (e) => usuarioEquipo(e) }
  ];
}

function incidentesColumns() {
  const empresas = dataCache?.empresas || [];
  const clientes = dataCache?.clientes || [];
  return [
    { label: "ID", get: (i) => i.id },
    { label: "Empresa", get: (i) => empresaNombre(empresas, i.empresaId || i.idCliente) },
    { label: "Cliente", get: (i) => clienteNombre(clientes, i.idCliente || i.clienteId) },
    { label: "Equipo", get: (i) => i.serieEquipo || i.equipoSerie || "" },
    { label: "Status", get: (i) => (i.estado || i.status || "").toUpperCase() },
    { label: "Detalle", get: (i) => i.detalle || "" },
    { label: "Fecha", get: (i) => i.fechaIncidente || i.fecha || "" }
  ];
}

function perifericosColumns() {
  return [
    { label: "Empresa", get: (p) => p.empresa },
    { label: "Equipo/Periférico", get: (p) => p.equipoSerie },
    { label: "Marca", get: (p) => p.marca },
    { label: "Modelo", get: (p) => p.modelo },
    { label: "Tipo", get: (p) => p.tipo },
    { label: "Observacion", get: (p) => p.observacion }
  ];
}

function buildFilename(base, includeClienteEmpresa) {
  const clienteSel = document.getElementById("fClienteReporte")?.value;
  const empresaSel = document.getElementById("fEmpresaReporte")?.value;
  const clienteName = clienteSel ? clienteNombre(dataCache?.clientes || [], clienteSel) : "todos";
  const empresaName = includeClienteEmpresa && empresaSel ? empresaNombre(dataCache?.empresas || [], empresaSel) : null;
  const now = new Date();
  const stamp = `${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, "0")}${String(now.getDate()).padStart(
    2,
    "0"
  )}_${String(now.getHours()).padStart(2, "0")}${String(now.getMinutes()).padStart(2, "0")}${String(
    now.getSeconds()
  ).padStart(2, "0")}`;
  const parts = [base];
  if (includeClienteEmpresa) {
    parts.push(`cliente-${clienteName}`);
    if (empresaName) parts.push(`empresa-${empresaName}`);
  }
  parts.push(stamp);
  return parts.join("_") + ".xls";
}

function renderKpi(rows) {
  const kpiResumen = document.querySelector("#kpi-resumen");
  if (!kpiResumen) return;
  const total = rows.length;
  const activos = rows.filter((e) => statusEquipo(e) === "ACTIVO").length;
  const baja = rows.filter((e) => statusEquipo(e) === "BAJA").length;
  const ubicaciones = unique(rows.map((e) => ubicacionEquipo(e))).length;
  const usuarios = unique(rows.map((e) => usuarioEquipo(e))).length;
  const template = [
    { label: "Total equipos", val: total, tone: "primary" },
    { label: "Activos", val: activos, tone: "success" },
    { label: "Baja/Inactive", val: baja, tone: "danger" },
    { label: "Ubicaciones", val: ubicaciones, tone: "info" },
    { label: "Usuarios", val: usuarios, tone: "secondary" }
  ]
    .map(
      (k) =>
        `<div class="card border-0 shadow-sm">
          <div class="card-body">
            <div class="text-muted small">${k.label}</div>
            <div class="h4 mb-0 text-${k.tone}">${k.val}</div>
          </div>
        </div>`
    )
    .join("");
  kpiResumen.innerHTML = template;
}

function setBadgeCliente(text) {
  const badge = document.getElementById("badge-selected-cliente");
  if (badge) badge.textContent = text || "Todos";
}

function bindButtons() {
  document.getElementById("btn-aplicar-equipos")?.addEventListener("click", filterEquipos);
  document.getElementById("btn-limpiar-equipos")?.addEventListener("click", () => {
    [
      "fMarca",
      "fTipo",
      "fStatus",
      "fUbicacion",
      "fUsuario",
      "fClienteEquipo",
      "fAlias",
      "fNombre",
      "fSerie",
      "fModelo",
      "fSO",
      "fCpu",
      "fDepto",
      "fCiudad",
      "fOffice"
    ].forEach((id) => {
      const el = document.getElementById(id);
      if (el) el.value = "";
    });
    filterEquipos();
  });
  document.getElementById("btn-aplicar-inc")?.addEventListener("click", filterIncidentes);
  document.getElementById("btn-limpiar-inc")?.addEventListener("click", () => {
    ["fEmpresaInc", "fClienteInc", "fStatusInc"].forEach((id) => {
      const el = document.getElementById(id);
      if (el) el.value = "";
    });
    filterIncidentes();
  });
  document.getElementById("fClienteReporte")?.addEventListener("change", () => {
    refreshEmpresasPorCliente();
    filterEquipos();
  });
  document.getElementById("fEmpresaReporte")?.addEventListener("change", () => {
    filterEquipos();
  });
  document.getElementById("fClienteInc")?.addEventListener("change", () => {
    refreshEmpresasPorCliente();
    filterIncidentes();
  });
  document.getElementById("fEmpresaInc")?.addEventListener("change", () => {
    filterIncidentes();
  });
  document.getElementById("btn-export-equipos-adv")?.addEventListener("click", () =>
    exportXlsRows(lastEquipos, equiposColumns(), buildFilename("equipos_filtro_avanzado", false))
  );
  document.getElementById("btn-export-equipos-pdf")?.addEventListener("click", () => printSection("tabla-equipos"));
  document.getElementById("btn-export-incidentes-global")?.addEventListener("click", () =>
    exportXlsRows(lastIncidentes, incidentesColumns(), buildFilename("incidentes", true))
  );
  document.getElementById("btn-export-equipos-global")?.addEventListener("click", () =>
    exportXlsRows(lastEquipos, equiposColumns(), buildFilename("equipos", true))
  );
  document.getElementById("btn-export-perifericos")?.addEventListener("click", () =>
    exportXlsRows(perifericosRows(), perifericosColumns(), buildFilename("perifericos_detalle", true))
  );
  document.getElementById("btn-print-global")?.addEventListener("click", () => window.print());
  document.getElementById("btn-export-filtro-excel")?.addEventListener("click", () =>
    exportXlsRows(lastEquipos, equiposColumns(), buildFilename("equipos_filtro_cliente_empresa", true))
  );
  document.getElementById("btn-export-filtro-pdf")?.addEventListener("click", () => printSection("tabla-equipos"));
}

function exportCsvRows(rows, columns, filename) {
  if (!rows || !rows.length) return;
  const header = columns.map((c) => c.label).join("\t");
  const body = rows
    .map((row) =>
      columns
        .map((c) => {
          const raw = c.get(row) ?? "";
          const text = String(raw).replace(/\s+/g, " ").trim();
          return text;
        })
        .join("\t")
    )
    .join("\n");
  const content = header + "\n" + body;
  return { header, content };
}

function exportXlsRows(rows, columns, filename) {
  const data = exportCsvRows(rows, columns, filename);
  if (!data) return;
  const html =
    `<html><head><meta charset="UTF-8"></head><body><table border="1"><thead><tr>` +
    columns.map((c) => `<th>${c.label}</th>`).join("") +
    `</tr></thead><tbody>` +
    rows
      .map(
        (r) =>
          `<tr>` +
          columns
            .map((c) => {
              const val = c.get(r) ?? "";
              return `<td>${String(val).replace(/</g, "&lt;").replace(/>/g, "&gt;")}</td>`;
            })
            .join("") +
          `</tr>`
      )
      .join("") +
    `</tbody></table></body></html>`;
  const blob = new Blob([html], { type: "application/vnd.ms-excel" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function printSection(tableId) {
  const table = document.getElementById(tableId);
  if (!table) {
    window.print();
    return;
  }
  const w = window.open("", "_blank");
  if (!w) return;
  w.document.write("<html><head><title>Export</title>");
  w.document.write(
    '<link rel="stylesheet" href="/assets/css/vendors/bootstrap.min.css"><link rel="stylesheet" href="/assets/css/pages/layout.css">'
  );
  w.document.write("</head><body>");
  w.document.write(table.outerHTML);
  w.document.write("</body></html>");
  w.document.close();
  w.focus();
  w.print();
  w.close();
}

async function main() {
  await loadLayout("reporteria");
  dataCache = await getDashboardData();
  fillFilters();
  filterEquipos();
  filterIncidentes();
  bindButtons();
}

document.addEventListener("DOMContentLoaded", main);


