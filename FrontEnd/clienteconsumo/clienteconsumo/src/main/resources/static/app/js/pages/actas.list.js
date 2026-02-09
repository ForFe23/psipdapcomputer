import { loadLayout } from "../ui/render.js";
import { actasApi } from "../api/actas.api.js";
import { actaItemsApi } from "../api/acta-items.api.js";
import { empresasApi } from "../api/empresas.api.js";
import { equiposApi } from "../api/equipos.api.js";
import { showError, showSuccess } from "../ui/alerts.js";

let empresas = [];
let dataCache = [];
let dt = null;
let pendingEstado = null;

const estadoBadge = (estado) => {
  const e = (estado || "").toUpperCase();
  if (e.includes("EMIT")) return "badge-success";
  if (e.includes("REG")) return "badge-warning";
  if (e.includes("ANU")) return "badge-danger";
  return "badge-secondary";
};

const empresaNombre = (id) => {
  const e = empresas.find((em) => String(em.id) === String(id));
  return e ? e.nombre || e.razonSocial || e.id : id ?? "";
};

  const actionCell = (a) => `
    <div class="actions-stack">
    <a class="btn btn-sm btn-outline-primary action-btn" href="./form.html?id=${a.id}"><i class="las la-pen"></i><span>Editar</span></a>
    <a class="btn btn-sm btn-outline-secondary action-btn" href="../adjuntos/list.html?actaId=${a.id}"><i class="las la-paperclip"></i><span>Adjuntos</span></a>
    <a class="btn btn-sm btn-outline-info action-btn" href="../acta-items/list.html?actaId=${a.id}"><i class="las la-list"></i><span>Items</span></a>
    <div class="dropdown actions-menu">
      <button class="btn btn-sm btn-dark actions-toggle dropdown-toggle" type="button" data-toggle="dropdown" data-display="static" aria-haspopup="true" aria-expanded="false">
        <i class="las la-ellipsis-v"></i>
      </button>
      <div class="dropdown-menu dropdown-menu-right actions-panel">
        <a class="dropdown-item" href="#" data-estado="EMITIDA" data-id="${a.id}" data-codigo="${a.codigo || ""}">
          <span class="indicator emitida"></span> Marcar EMITIDA
          <span class="badge badge-light text-success">EMITIDA</span>
        </a>
        <a class="dropdown-item" href="#" data-estado="CERRADA" data-id="${a.id}" data-codigo="${a.codigo || ""}">
          <span class="indicator cerrada"></span> Cerrar acta
          <span class="badge badge-light text-warning">CERRADA</span>
        </a>
        <a class="dropdown-item text-danger" href="#" data-estado="ANULADA" data-id="${a.id}" data-codigo="${a.codigo || ""}">
          <span class="indicator anulada"></span> Anular acta
          <span class="badge badge-light text-danger">ANULADA</span>
        </a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="#" data-print-acta="${a.id}">
          <span class="indicator neutro"></span> Imprimir acta
        </a>
        <a class="dropdown-item" href="#" data-print-acta="${a.id}" data-reprint="1">
          <span class="indicator neutro"></span> Reimprimir acta
        </a>
        <a class="dropdown-item" href="../acta-items/list.html?actaId=${a.id}">
          <span class="indicator neutro"></span> Ver ítems / periféricos
        </a>
        <div class="dropdown-divider"></div>
        <button class="dropdown-item text-danger" data-delete="${a.id}">
          <span class="indicator anulada"></span> Eliminar
        </button>
      </div>
    </div>
  </div>
`;

const leerFiltros = () => {
  const f = document.querySelector("#filtros-actas");
  return {
    codigo: f.fCodigo.value.trim() || undefined,
    empresaId: f.fEmpresa.value.trim() || undefined,
    equipoId: f.fEquipo.value.trim() || undefined,
    estado: f.fEstado.value || undefined,
  };
};

const aplicarFiltros = (data, filtros) =>
  data.filter((a) => {
    if (filtros.codigo && !String(a.codigo ?? "").toUpperCase().includes(filtros.codigo.toUpperCase())) return false;
    if (filtros.empresaId && String(a.empresaId ?? a.idCliente ?? a.clienteId) !== filtros.empresaId) return false;
    if (filtros.equipoId && String(a.idEquipo ?? "").toUpperCase() !== filtros.equipoId.toUpperCase()) return false;
    if (filtros.estado && String(a.estado ?? "").toUpperCase() !== filtros.estado.toUpperCase()) return false;
    return true;
  });

const actualizarKpis = (data = []) => {
  const emitidas = data.filter((a) => (a.estado || "").toUpperCase().includes("EMIT")).length;
  const registradas = data.filter((a) => (a.estado || "").toUpperCase().includes("REG")).length;
  const anuladas = data.filter((a) => (a.estado || "").toUpperCase().includes("ANU")).length;
  const set = (id, val) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  };
  set("kpi-emitidas", emitidas);
  set("kpi-registradas", registradas);
  set("kpi-anuladas", anuladas);
};

const renderTable = (rows) => {
  const tableEl = $("#actasTable");
  if (!dt) {
    dt = tableEl.DataTable({
      data: rows,
      scrollX: true,
      stateSave: true,
      dom: "Bfrtip",
      buttons: [{ extend: "colvis", text: "Mostrar / ocultar columnas" }],
      language: { url: "//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json" },
      columns: [
        {
          data: null,
          orderable: false,
          className: "actions-cell",
          render: actionCell,
        },
        { data: "id" },
        { data: "codigo" },
        {
          data: "estado",
          render: (d) => `<span class="badge ${estadoBadge(d)}">${d || ""}</span>`,
        },
        {
          data: "empresaId",
          render: (d, _t, row) => empresaNombre(d ?? row.idCliente ?? row.clienteId),
        },
        { data: "idEquipo" },
        { data: "fechaActa", render: (d, _t, row) => d || row.fecha || "" },
        { data: "tema" },
      ],
      order: [[1, "desc"]],
      createdRow: (row) => {
        row.classList.add("actions-first");
      },
      drawCallback: () => actualizarKpis(dt.rows({ filter: "applied" }).data().toArray()),
    });
  } else {
    dt.clear().rows.add(rows).draw(false);
  }
  actualizarKpis(rows);
};

const loadEmpresas = async () => {
  empresas = await empresasApi.list();
  const empresaSelect = document.getElementById("fEmpresa");
  if (empresaSelect) {
    empresaSelect.innerHTML =
      `<option value=\"\">Todas</option>` +
      empresas.map((e) => `<option value="${e.id}">${e.nombre || e.razonSocial || e.id}</option>`).join("");
  }
};

const loadData = async () => {
  const filtros = leerFiltros();
  dataCache = await actasApi.list();
  renderTable(aplicarFiltros(dataCache, filtros));
};

const changeEstado = async (id, nuevoEstado) => {
  try {
    const actual = await actasApi.getById(id);
    const payload = { ...actual, estado: nuevoEstado };
    await actasApi.update(id, payload);
    showSuccess(`Acta ${id} → ${nuevoEstado}`);
    await loadData();
  } catch (err) {
    showError(err.message);
  }
};

const deleteActa = async (id) => {
  if (!window.confirm("¿Eliminar acta?")) return;
  try {
    await actasApi.remove(id);
    showSuccess("Acta eliminada");
    await loadData();
  } catch (err) {
    showError(err.message);
  }
};

const handlePrint = async (id, reprint) => {
  if (typeof window.actasPrintHandler === "function") {
    window.actasPrintHandler({ id, reprint });
    return;
  }
  try {
    const acta = await actasApi.getById(id);

    let equipo = null;
    let equipoError = null;
    try {
      if (acta.idEquipo) {
        equipo = await equiposApi.getById(acta.idEquipo);
      } else if (acta.equipoSerie) {
        equipo = await equiposApi.getBySerie(acta.equipoSerie);
      }
    } catch (err) {
      equipoError = err?.message || "No se pudo obtener el equipo";
    }

    let items = [];
    let itemsError = null;
    try {
      items = await actaItemsApi.list(id);
    } catch (err) {
      itemsError = err?.message || "No se pudieron cargar los items.";
      items = [];
    }
    const equipoPrincipal = {
      itemNumero: 1,
      tipo: equipo?.tipo || equipo?.tipoEquipo || acta.equipoTipo || acta.tipo || "EQUIPO PRINCIPAL",
      serie:
        equipo?.serie ||
        equipo?.serieEquipo ||
        acta.equipoSerie ||
        acta.idEquipo ||
        acta.serie ||
        "",
      modelo: equipo?.modelo || equipo?.modeloEquipo || acta.equipoModelo || acta.modelo || "",
      observacion: acta.tema || acta.descripcion || "",
    };
    const extraItems = (items || []).map((i, idx) => ({
      ...i,
      itemNumero: i.itemNumero ?? idx + 2,
    }));
    const fullItems = [equipoPrincipal, ...extraItems];

    const fmtFecha = (val) => {
      if (!val) return "";
      const d = new Date(val);
      if (Number.isNaN(d.getTime())) return val;
      return d.toLocaleDateString("es-ES", { year: "numeric", month: "2-digit", day: "2-digit" });
    };
    const fechaStr = fmtFecha(acta.fechaActa || acta.fecha);
    const estado = acta.estado || "";
    const empresa = empresaNombre(acta.empresaId ?? acta.idCliente ?? acta.clienteId);
    const tema = acta.tema ?? "";
    const equipoIdLabel = acta.idEquipo ?? "";
    const equipoSerie = equipoPrincipal.serie || "—";
    const equipoModelo = equipoPrincipal.modelo || "—";
    const equipoTipo = equipoPrincipal.tipo || "—";
    const equipoNombre =
      equipo?.nombre || equipo?.nombreEquipo || equipo?.alias || equipo?.activo || "—";
    const equipoMarca = equipo?.marca || equipo?.marcaEquipo || "—";
    const equipoUsuario = equipo?.nombreUsuario || equipo?.usuario || acta.recibidoPor || "—";
    const equipoUbicacion = equipo?.ubicacionUsuario || acta.ubicacionUsuario || "—";
    const equipoDepartamento = equipo?.departamentoUsuario || acta.departamentoUsuario || "—";
    const equipoCiudad = equipo?.ciudad || acta.ciudadEquipo || "—";
    const observaciones = acta.observacionesGenerales || (equipo?.observaciones ?? "");
    const codigo = acta.codigo || id;

    const win = window.open("", "_blank");
    if (!win) {
      showError("Permite popups para imprimir el acta.");
      return;
    }
    const rows = fullItems
      .map(
        (i) =>
          `<tr>
            <td>${i.itemNumero ?? ""}</td>
            <td>${i.tipo ?? ""}</td>
            <td>${i.serie ?? i.equipoSerie ?? ""}</td>
            <td>${i.modelo ?? ""}</td>
            <td>${i.observacion ?? ""}</td>
          </tr>`
      )
      .join("");

    const estadoColor =
      (estado || "").toUpperCase().includes("ANU") ? "#dc2626" :
      (estado || "").toUpperCase().includes("EMIT") ? "#16a34a" :
      (estado || "").toUpperCase().includes("CER") ? "#f59e0b" : "#475569";

    win.document.write(`<!doctype html><html><head><title>Acta ${codigo}</title>
      <style>
        :root {
          --ink:#0f172a;
          --muted:#475569;
          --card:#ffffff;
          --soft:#f8fafc;
          --accent1:#0ea5e9;
          --accent2:#6366f1;
          --border:#e2e8f0;
        }
        *{box-sizing:border-box;}
        body{
          margin:0;
          font-family:'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
          color:var(--ink);
          background:linear-gradient(135deg,#f6fafe 0%,#eef2ff 50%,#f8fafc 100%);
          padding:32px;
        }
        .sheet{
          max-width:1100px;
          margin:0 auto;
          background:var(--card);
          border:1px solid var(--border);
          box-shadow:0 18px 40px rgba(15,23,42,.08);
          border-radius:14px;
          overflow:hidden;
        }
        header{
          padding:28px 28px 18px;
          background:linear-gradient(120deg,rgba(14,165,233,.12),rgba(99,102,241,.10));
          border-bottom:1px solid var(--border);
        }
        .headline{
          font-size:32px;
          margin:0 0 6px;
          letter-spacing:-0.5px;
        }
        .meta{
          display:flex;
          flex-wrap:wrap;
          gap:12px;
          color:var(--muted);
          font-weight:600;
          font-size:14px;
        }
        .pill{
          display:inline-flex;
          align-items:center;
          gap:6px;
          padding:6px 12px;
          border-radius:999px;
          background:rgba(99,102,241,.1);
          color:var(--accent2);
          font-weight:700;
          text-transform:uppercase;
          letter-spacing:.4px;
          font-size:12px;
        }
        .pill.estado{background:${estadoColor}1a;color:${estadoColor}}
        .body{
          padding:24px 28px 28px;
        }
        .grid{
          display:grid;
          grid-template-columns:repeat(auto-fit,minmax(220px,1fr));
          gap:14px;
          margin-top:12px;
          margin-bottom:18px;
        }
        .card{
          background:var(--soft);
          border:1px solid var(--border);
          border-radius:10px;
          padding:14px;
        }
        .label{font-size:12px;color:var(--muted);text-transform:uppercase;letter-spacing:.3px;margin-bottom:6px;}
        .value{font-weight:700;font-size:15px;}
        .alert{
          background:#fff7ed;
          border:1px solid #fb923c;
          color:#9a3412;
          padding:10px 12px;
          border-radius:8px;
          margin:6px 0 14px;
          font-weight:600;
        }
        table{
          width:100%;
          border-collapse:separate;
          border-spacing:0;
          border:1px solid var(--border);
          border-radius:12px;
          overflow:hidden;
        }
        th,td{
          padding:10px 12px;
          font-size:13px;
          border-bottom:1px solid var(--border);
        }
        th{
          background:var(--soft);
          text-align:left;
          font-weight:700;
          color:var(--muted);
        }
        tr:last-child td{border-bottom:none;}
        tr:nth-child(even) td{background:#fdfefe;}
        .footer{
          margin-top:18px;
          font-size:11px;
          color:var(--muted);
          text-align:right;
        }
        .signatures{
          display:grid;
          grid-template-columns:repeat(auto-fit,minmax(240px,1fr));
          gap:20px;
          margin-top:22px;
        }
        .sign-box{
          border:1px dashed var(--border);
          border-radius:10px;
          padding:14px 16px 18px;
          background:var(--soft);
        }
        .sign-line{
          margin-top:28px;
          border-bottom:1px solid #cbd5e1;
          height:1px;
        }
        .sign-label{
          margin-top:6px;
          font-size:12px;
          color:var(--muted);
        }
        .info-section{
          display:grid;
          grid-template-columns:repeat(auto-fit,minmax(200px,1fr));
          gap:12px;
          margin-top:10px;
          margin-bottom:10px;
        }
      </style>
    </head><body>
      <div class="sheet">
        <header>
          <div class="headline">Acta ${codigo}</div>
          <div class="meta">
            <span class="pill estado">${estado || "SIN ESTADO"}</span>
            <span>Empresa: ${empresa || "—"}</span>
            <span>Fecha: ${fechaStr || "—"}</span>
            ${equipoError ? `<span style="color:#dc2626;font-weight:700;">Equipo: ${equipoError}</span>` : ""}
          </div>
        </header>
        <div class="body">
          <div class="grid">
            <div class="card"><div class="label">Equipo</div><div class="value">${equipoIdLabel || "—"}</div></div>
            <div class="card"><div class="label">Tema</div><div class="value">${tema || "—"}</div></div>
            <div class="card"><div class="label">Código</div><div class="value">${codigo}</div></div>
          </div>
          <div class="info-section">
            <div class="card"><div class="label">Serie</div><div class="value">${equipoSerie}</div></div>
            <div class="card"><div class="label">Modelo</div><div class="value">${equipoModelo}</div></div>
            <div class="card"><div class="label">Tipo</div><div class="value">${equipoTipo}</div></div>
            <div class="card"><div class="label">Marca</div><div class="value">${equipoMarca}</div></div>
            <div class="card"><div class="label">Nombre/Alias</div><div class="value">${equipoNombre}</div></div>
            <div class="card"><div class="label">Usuario</div><div class="value">${equipoUsuario}</div></div>
            <div class="card"><div class="label">Ubicación</div><div class="value">${equipoUbicacion}</div></div>
            <div class="card"><div class="label">Departamento</div><div class="value">${equipoDepartamento}</div></div>
            <div class="card"><div class="label">Ciudad</div><div class="value">${equipoCiudad}</div></div>
          </div>
          <div class="card" style="margin-bottom:10px;">
            <div class="label">Observaciones</div>
            <div class="value" style="white-space:pre-line;">${observaciones || "—"}</div>
          </div>
          ${itemsError ? `<div class="alert">Items no disponibles: ${itemsError}</div>` : ""}
          <table>
            <thead><tr><th>#</th><th>Tipo</th><th>Serie</th><th>Modelo</th><th>Observación</th></tr></thead>
            <tbody>${rows || "<tr><td colspan='5'>Sin items</td></tr>"}</tbody>
          </table>
          <div class="signatures">
            <div class="sign-box">
              <div class="label">Entrega</div>
              <div class="value" style="font-weight:700;">${acta.entregadoPor || "________________"}</div>
              <div class="sign-label">${acta.cargoEntrega || "Nombre y firma"}</div>
              <div class="label" style="margin-top:10px">Fecha</div>
              <div class="value">${fechaStr || "____/____/____"}</div>
            </div>
            <div class="sign-box">
              <div class="label">Recepción</div>
              <div class="value" style="font-weight:700;">${acta.recibidoPor || "________________"}</div>
              <div class="sign-label">${acta.cargoRecibe || "Nombre y firma"}</div>
              <div class="label" style="margin-top:10px">Fecha</div>
              <div class="value">____/____/____</div>
            </div>
          </div>
          <div class="footer">Generado automáticamente · ${new Date().toLocaleString("es-ES")}</div>
        </div>
      </div>
      <script>window.print();<\/script>
    </body></html>`);
    win.document.close();
  } catch (err) {
    showError("No se pudo generar la impresión del acta");
  }
};

const promptEstadoChange = ({ id, estado, codigo }) => {
  pendingEstado = { id, estado, codigo };
  const info = document.getElementById("estadoModalInfo");
  if (info) info.textContent = `Acta ${id}: cambiar a ${estado}`;
  const select = document.getElementById("estadoSelect");
  if (select) select.value = estado;
  $("#estadoModal").modal("show");
  $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
};

const bindEstadoModal = () => {
  const confirmBtn = document.getElementById("confirmEstadoBtn");
  if (confirmBtn) {
    confirmBtn.onclick = async () => {
      if (!pendingEstado) return;
      const { id, estado } = pendingEstado;
      pendingEstado = null;
      $("#estadoModal").modal("hide");
      await changeEstado(id, estado);
    };
  }
};

const bindEvents = () => {
  const formFiltros = document.querySelector("#filtros-actas");
  formFiltros?.addEventListener("submit", async (ev) => {
    ev.preventDefault();
    renderTable(aplicarFiltros(dataCache, leerFiltros()));
  });
  document.getElementById("btnClearFilters")?.addEventListener("click", (ev) => {
    ev.preventDefault();
    formFiltros?.reset();
    renderTable(dataCache);
  });

  document.addEventListener("click", async (ev) => {
    const actionLink = ev.target.closest(".dropdown-item, button.dropdown-item");
    if (actionLink) ev.preventDefault();
    const estadoBtn = ev.target.closest("[data-estado]");
    const delBtn = ev.target.closest("[data-delete]");
    const printBtn = ev.target.closest("[data-print-acta]");

    if (estadoBtn) {
      const { id, estado } = estadoBtn.dataset;
      promptEstadoChange({ id, estado, codigo: estadoBtn.dataset.codigo });
    }
    if (delBtn) {
      await deleteActa(delBtn.dataset.delete);
      $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
    }
    if (printBtn) {
      const id = printBtn.dataset.printActa;
      handlePrint(id, !!printBtn.dataset.reprint);
      $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
    }
  });

  
  $(document).on("show.bs.dropdown", ".actions-menu", function () {
    
    $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
    
    $(".dropdown-menu.floating-actions-panel").each(function () {
      const $m = $(this);
      const origin = $m.data("origin-parent");
      $m.removeClass("floating-actions-panel").removeAttr("style");
      if (origin && origin.length) $m.appendTo(origin);
      const closeFn = $m.data("close-fn");
      if (closeFn) {
        $(window).off("scroll.dropdownfix", closeFn).off("resize.dropdownfix", closeFn);
        $(".dataTables_scrollBody").off("scroll.dropdownfix", closeFn);
      }
      $m.data("origin-parent", null).data("close-fn", null);
    });

    const $menu = $(this).find(".dropdown-menu");
    const $toggle = $(this).find(".dropdown-toggle");
    if (!$menu.length || !$toggle.length) return;

    
    if (!$menu.data("origin-parent")) {
      $menu.data("origin-parent", $menu.parent());
    }
    $menu.appendTo(document.body);
    const rect = $toggle[0].getBoundingClientRect();
    const top = rect.bottom + window.scrollY + 8; 
    const left = rect.left + window.scrollX;
    const minWidth = Math.max(rect.width * 6, 260);
    $menu.css({
      position: "absolute",
      top: `${top}px`,
      left: `${left}px`,
      minWidth: `${minWidth}px`,
      width: `${minWidth}px`,
      zIndex: 1000000,
    });
    $menu.addClass("floating-actions-panel");
    
    const closeFn = () => {
      if ($toggle.data("bs.dropdown")) {
        $toggle.dropdown("toggle");
      }
    };
    $(window).one("scroll.dropdownfix resize.dropdownfix", closeFn);
    $(".dataTables_scrollBody").one("scroll.dropdownfix", closeFn);
    $menu.data("close-fn", closeFn);

    
    const handleMenuClick = async (ev) => {
      ev.preventDefault();
      ev.stopPropagation();
      const t = ev.currentTarget;
      if (t.dataset.estado) {
        promptEstadoChange({
          id: t.dataset.id,
          estado: t.dataset.estado,
          codigo: t.dataset.codigo,
        });
      } else if (t.dataset.delete) {
        await deleteActa(t.dataset.delete);
      } else if (t.dataset.printActa) {
        handlePrint(t.dataset.printActa, !!t.dataset.reprint);
      }
      $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
    };
    $menu.off("click.menuactions");
    $menu.on("click.menuactions", "[data-estado]", handleMenuClick);
    $menu.on("click.menuactions", "[data-delete]", handleMenuClick);
    $menu.on("click.menuactions", "[data-print-acta]", handleMenuClick);
    $menu.data("menu-handler", handleMenuClick);
  });

  $(document).on("hide.bs.dropdown", ".actions-menu", function () {
    const $menu = $(this).find(".dropdown-menu.floating-actions-panel");
    if (!$menu.length) return;
    const origin = $menu.data("origin-parent");
    $menu.removeClass("floating-actions-panel");
    $menu.removeAttr("style");
    if (origin && origin.length) {
      $menu.appendTo(origin);
    }
    $menu.removeData("origin-parent");
    const closeFn = $menu.data("close-fn");
    if (closeFn) {
      $(window).off("scroll.dropdownfix", closeFn).off("resize.dropdownfix", closeFn);
      $(".dataTables_scrollBody").off("scroll.dropdownfix", closeFn);
      $menu.removeData("close-fn");
    }
    $menu.off("click.menuactions");
  });

  
  $("#actasTable").on("draw.dt", () => {
    $(".actions-menu.show .dropdown-toggle").dropdown("toggle");
  });
};

document.addEventListener("DOMContentLoaded", async () => {
  await loadLayout("actas");
  try {
    await loadEmpresas();
    await loadData();
  } catch (err) {
    showError(err.message);
  }
  bindEstadoModal();
  bindEvents();
});


