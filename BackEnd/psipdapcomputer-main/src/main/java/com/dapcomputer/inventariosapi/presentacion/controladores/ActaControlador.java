package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ActualizarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.EliminarActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorEstadoCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorRangoFechaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarActasPorUsuarioCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerActaCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarActaCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaDto;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ActaDtoMapper;
import jakarta.validation.Valid;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/actas")
@CrossOrigin(
        origins = {
                "http://localhost:8090",
                "http://127.0.0.1:8090",
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "http://192.168.1.7:8090",
                "https://192.168.1.7:8443"
        },
        allowCredentials = "true")
public class ActaControlador {
    private final RegistrarActaCasoUso registrarActa;
    private final ListarActasCasoUso listarActas;
    private final ObtenerActaCasoUso obtenerActa;
    private final ListarActasPorEstadoCasoUso listarPorEstado;
    private final ListarActasPorRangoFechaCasoUso listarPorRango;
    private final ListarActasPorClienteCasoUso listarPorCliente;
    private final ListarActasPorUsuarioCasoUso listarPorUsuario;
    private final ActualizarActaCasoUso actualizarActa;
    private final EliminarActaCasoUso eliminarActa;
    private final ActaDtoMapper mapper;

    private static final PDFont FONT_REGULAR = PDType1Font.HELVETICA;
    private static final PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final PDFont FONT_BOLD_OBLIQUE = PDType1Font.HELVETICA_BOLD_OBLIQUE;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color COLOR_BORDER = Color.decode("#4b5563");
    private static final Color COLOR_BORDER_LIGHT = Color.decode("#9ca3af");
    private static final Color COLOR_HEADER_TEXT = Color.BLACK;
    private static final Color COLOR_FILL_HEADER = Color.WHITE;
    private static final Color COLOR_FILL_HEADER_ALT = Color.decode("#f3f4f6");
    private static final Color COLOR_FOOTER = Color.decode("#4b5563");

    public ActaControlador(
            RegistrarActaCasoUso registrarActa,
            ListarActasCasoUso listarActas,
            ObtenerActaCasoUso obtenerActa,
            ListarActasPorEstadoCasoUso listarPorEstado,
            ListarActasPorRangoFechaCasoUso listarPorRango,
            ListarActasPorClienteCasoUso listarPorCliente,
            ListarActasPorUsuarioCasoUso listarPorUsuario,
            ActualizarActaCasoUso actualizarActa,
            EliminarActaCasoUso eliminarActa,
            ActaDtoMapper mapper) {
        this.registrarActa = registrarActa;
        this.listarActas = listarActas;
        this.obtenerActa = obtenerActa;
        this.listarPorEstado = listarPorEstado;
        this.listarPorRango = listarPorRango;
        this.listarPorCliente = listarPorCliente;
        this.listarPorUsuario = listarPorUsuario;
        this.actualizarActa = actualizarActa;
        this.eliminarActa = eliminarActa;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ActaDto> crear(@Valid @RequestBody ActaDto solicitud) {
        validarClienteEmpresa(solicitud);
        var creada = registrarActa.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creada));
    }

    @GetMapping
    public List<ActaDto> listar() {
        return listarActas.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActaDto> obtener(@PathVariable Integer id) {
        var encontrada = obtenerActa.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrada));
    }

    @GetMapping("/estado")
    public List<ActaDto> listarPorEstado(@RequestParam EstadoActa estado) {
        return listarPorEstado.ejecutar(estado).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/rango-fecha")
    public List<ActaDto> listarPorRango(
            @RequestParam("inicio") LocalDate inicio,
            @RequestParam("fin") LocalDate fin) {
        return listarPorRango.ejecutar(inicio, fin).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<ActaDto> listarPorCliente(@PathVariable Integer idCliente) {
        return listarPorCliente.ejecutar(idCliente).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/usuario")
    public List<ActaDto> listarPorUsuario(@RequestParam String nombre) {
        return listarPorUsuario.ejecutar(nombre).stream().map(mapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActaDto> actualizar(@PathVariable Integer id, @Valid @RequestBody ActaDto solicitud) {
        validarClienteEmpresa(solicitud);
        var entrada = new ActaDto(
                id,
                solicitud.codigo(),
                solicitud.estado(),
                solicitud.estadoInterno(),
                solicitud.idCliente(),
                solicitud.idEquipo(),
                solicitud.empresaId(),
                solicitud.ubicacionId(),
                solicitud.fechaActa(),
                solicitud.tema(),
                solicitud.entregadoPor(),
                solicitud.recibidoPor(),
                solicitud.cargoEntrega(),
                solicitud.cargoRecibe(),
                solicitud.departamentoUsuario(),
                solicitud.ciudadEquipo(),
                solicitud.ubicacionUsuario(),
                solicitud.observacionesGenerales(),
                solicitud.equipoTipo(),
                solicitud.equipoSerie(),
                solicitud.equipoModelo(),
                solicitud.creadoEn(),
                solicitud.creadoPor(),
                solicitud.items());
        var actualizado = actualizarActa.ejecutar(mapper.toDomain(entrada));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarActa.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarClienteEmpresa(ActaDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Acta vacía");
        }
        if (dto.idCliente() == null) {
            throw new IllegalArgumentException("Debe especificar clienteId");
        }
        if (dto.empresaId() != null && dto.empresaId() <= 0) {
            throw new IllegalArgumentException("empresaId inválido");
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Integer id) throws IOException {
        var acta = obtenerActa.ejecutar(id);
        var dto = mapper.toDto(acta);
        byte[] pdf = generarPdf(dto);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=acta-" + id + ".pdf")
                .body(pdf);
    }

    private byte[] generarPdf(ActaDto acta) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            document.setVersion(1.4f);
            PDPage page = new PDPage(new PDRectangle(612, 792));
            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float margin = 28f;
            float contentWidth = pageWidth - (2 * margin);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float y = pageHeight - margin;

                y = drawTopBar(content, acta, margin, y, contentWidth);
                y = drawTwoColRow(content, "Tema", nv(acta.tema()), "Fecha", formatFecha(acta.fechaActa()), margin, y - 10f, contentWidth);
                y = drawTwoColRow(content, "Empresa", nv(acta.empresaId()), "Acta N°", nv(acta.codigo() != null ? acta.codigo() : acta.id()), margin, y - 2f, contentWidth);
                y = drawUbicacion(content, acta, margin, y - 12f, contentWidth);
                y = drawItemsTable(content, acta, margin, y - 14f, contentWidth);
                y = drawObservacion(content, acta, margin, y - 10f, contentWidth);
                y = drawFirmas(content, acta, margin, y - 30f, contentWidth);
                drawFooter(content, pageWidth);
            }

            document.save(out);
            return out.toByteArray();
        }
    }

    private float drawTopBar(PDPageContentStream content, ActaDto acta, float x, float yTop, float width)
            throws IOException {
        float height = 72f;
        float logoWidth = 150f;
        float rightWidth = 90f;
        float centerWidth = width - logoWidth - rightWidth;
        float y = yTop - height;

        content.setStrokingColor(COLOR_BORDER);
        content.setLineWidth(1f);
        content.addRect(x, y, width, height);
        content.stroke();

        float xCenter = x + logoWidth;
        float xRight = x + logoWidth + centerWidth;

        content.moveTo(xCenter, y);
        content.lineTo(xCenter, yTop);
        content.stroke();
        content.moveTo(xRight, y);
        content.lineTo(xRight, yTop);
        content.stroke();

        writeText(content, FONT_BOLD_OBLIQUE, 22f, COLOR_HEADER_TEXT, x + 12f, y + height / 2f + 6f, "LOGO EMPRESA");

        String title = "Acta entrega recepcion";
        float titleWidth = FONT_BOLD.getStringWidth(title) / 1000 * 16f;
        float titleX = xCenter + (centerWidth - titleWidth) / 2f;
        writeText(content, FONT_BOLD, 16f, COLOR_HEADER_TEXT, titleX, y + height / 2f + 6f, title);

        String area = "IT";
        float areaWidth = FONT_BOLD.getStringWidth(area) / 1000 * 14f;
        float areaX = xRight + (rightWidth - areaWidth) / 2f;
        writeText(content, FONT_BOLD, 14f, COLOR_HEADER_TEXT, areaX, y + height / 2f + 4f, area);

        return y;
    }

    private float drawTwoColRow(PDPageContentStream content, String leftLabel, String leftValue, String rightLabel,
                                String rightValue, float x, float yTop, float width) throws IOException {
        float height = 32f;
        float leftWidth = width * 0.68f;
        float rightWidth = width - leftWidth;
        float y = yTop - height;

        content.setStrokingColor(COLOR_BORDER_LIGHT);
        content.setLineWidth(1f);
        content.addRect(x, y, width, height);
        content.stroke();
        content.moveTo(x + leftWidth, y);
        content.lineTo(x + leftWidth, yTop);
        content.stroke();

        writeText(content, FONT_BOLD, 11f, COLOR_HEADER_TEXT, x + 8f, y + height / 2f + 3f,
                leftLabel + ": " + leftValue);
        writeText(content, FONT_BOLD, 11f, COLOR_HEADER_TEXT, x + leftWidth + 8f, y + height / 2f + 3f,
                rightLabel + ": " + rightValue);

        return y;
    }

    private float drawUbicacion(PDPageContentStream content, ActaDto acta, float x, float yTop, float width)
            throws IOException {
        float lineHeight = 16f;
        float padding = 10f;
        float titleHeight = 18f;
        float bodyHeight = lineHeight * 3 + 10f;
        float height = padding + titleHeight + 6f + bodyHeight + padding;
        float y = yTop - height;

        content.setStrokingColor(COLOR_BORDER_LIGHT);
        content.setLineWidth(1f);
        content.addRect(x, y, width, height);
        content.stroke();

        float titleY = yTop - padding - titleHeight;
        writeText(content, FONT_BOLD, 12f, COLOR_HEADER_TEXT, x + 6f, titleY, "Ubicacion de asignacion");

        float textY = titleY - 10f;
        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + 6f, textY,
                "Departamento: " + nv(acta.departamentoUsuario()));
        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + 6f, textY - lineHeight,
                "Ciudad: " + nv(acta.ciudadEquipo()));
        String ubicacionValor = acta.ubicacionUsuario() != null && !acta.ubicacionUsuario().isBlank()
                ? acta.ubicacionUsuario()
                : nv(acta.ubicacionId());
        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + 6f, textY - (2 * lineHeight),
                "Ubicacion: " + ubicacionValor);

        return y;
    }

    private float drawItemsTable(PDPageContentStream content, ActaDto acta, float x, float yTop, float width)
            throws IOException {
        float headerHeight = 22f;
        float[] colWidths = new float[]{
                50f, 110f, 90f, 110f, 110f, width - 470f
        };
        float y = yTop - headerHeight;

        content.setNonStrokingColor(COLOR_FILL_HEADER_ALT);
        content.addRect(x, y, width, headerHeight);
        content.fill();
        content.setStrokingColor(COLOR_BORDER);
        content.setLineWidth(1f);
        content.addRect(x, y, width, headerHeight);
        content.stroke();

        String[] headers = {"Item", "Tipo", "Marca", "Serie", "Modelo", "Observacion"};
        float textY = y + headerHeight / 2f - 4f;
        float colX = x;
        for (int i = 0; i < headers.length; i++) {
            writeText(content, FONT_BOLD, 10.5f, COLOR_HEADER_TEXT, colX + 6f, textY, headers[i]);
            colX += colWidths[i];
            if (i < headers.length - 1) {
                content.moveTo(colX, y);
                content.lineTo(colX, y + headerHeight);
                content.stroke();
            }
        }

        float currentY = y;
        List<ItemRow> rows = buildOrderedRows(acta);
        if (rows.isEmpty()) {
            currentY = drawItemRow(content, new String[]{"-", "-", "-", "-", "Sin items"}, x, currentY, width, colWidths, 0);
        } else {
            for (int idx = 0; idx < rows.size(); idx++) {
                ItemRow item = rows.get(idx);
                String[] values = new String[]{
                        String.valueOf(idx + 1),
                        nv(item.tipo()),
                        nv(item.marca()),
                        nv(item.serie()),
                        nv(item.modelo()),
                        nv(item.observacion())
                };
                currentY = drawItemRow(content, values, x, currentY, width, colWidths, idx + 1);
            }
        }
        return currentY;
    }

    private float drawItemRow(PDPageContentStream content, String[] values, float x, float yTop, float tableWidth,
                              float[] colWidths, int rowIndex) throws IOException {
        float interline = 12f;
        int maxLines = 1;
        for (int i = 0; i < values.length; i++) {
            maxLines = Math.max(maxLines, wrapText(values[i], FONT_REGULAR, 10.5f, colWidths[i] - 8f).size());
        }
        float rowHeight = Math.max(22f, 10f + (maxLines * interline));
        float y = yTop - rowHeight;

        content.setNonStrokingColor(Color.WHITE);
        content.addRect(x, y, tableWidth, rowHeight);
        content.fill();
        content.setStrokingColor(COLOR_BORDER_LIGHT);
        content.setLineWidth(1f);
        content.addRect(x, y, tableWidth, rowHeight);
        content.stroke();

        float colX = x;
        for (int i = 0; i < values.length; i++) {
            List<String> lines = wrapText(values[i], FONT_REGULAR, 10.5f, colWidths[i] - 8f);
            float textY = y + rowHeight - 9f;
            for (String line : lines) {
                writeText(content, FONT_REGULAR, 10.5f, COLOR_HEADER_TEXT, colX + 4f, textY, line);
                textY -= interline;
            }
            colX += colWidths[i];
            if (i < values.length - 1) {
                content.moveTo(colX, y);
                content.lineTo(colX, y + rowHeight);
                content.stroke();
            }
        }
        return y;
    }

    private float drawObservacion(PDPageContentStream content, ActaDto acta, float x, float yTop, float width)
            throws IOException {
        float padding = 8f;
        float labelHeight = 18f;
        List<String> lines = wrapText(nv(acta.observacionesGenerales()), FONT_REGULAR, 11f, width - padding * 2);
        float bodyHeight = lines.size() * 12f + 6f;
        float height = padding + labelHeight + bodyHeight + padding;
        float y = yTop - height;

        content.setStrokingColor(COLOR_BORDER_LIGHT);
        content.setLineWidth(1f);
        content.addRect(x, y, width, height);
        content.stroke();

        float labelY = yTop - padding - 4f;
        writeText(content, FONT_BOLD, 12f, COLOR_HEADER_TEXT, x + padding, labelY, "Observacion general");

        float textY = labelY - 14f;
        for (String line : lines) {
            writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + padding, textY, line);
            textY -= 12f;
        }
        return y;
    }

    private float drawFirmas(PDPageContentStream content, ActaDto acta, float x, float yTop, float width)
            throws IOException {
        float lineY = Math.max(140f, yTop - 80f);
        float lineWidth = (width - 80f) / 2f;

        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + 6f, lineY + 26f,
                "Entregado por: " + nv(acta.entregadoPor()));
        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + 6f, lineY + 12f,
                "Cargo: " + nv(acta.cargoEntrega()));

        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + lineWidth + 80f + 6f, lineY + 26f,
                "Recibido por: " + nv(acta.recibidoPor()));
        writeText(content, FONT_REGULAR, 11f, COLOR_HEADER_TEXT, x + lineWidth + 80f + 6f, lineY + 12f,
                "Cargo: " + nv(acta.cargoRecibe()));

        content.setStrokingColor(COLOR_BORDER_LIGHT);
        content.setLineWidth(1f);
        content.moveTo(x, lineY);
        content.lineTo(x + lineWidth, lineY);
        content.stroke();
        content.moveTo(x + lineWidth + 80f, lineY);
        content.lineTo(x + lineWidth + 80f + lineWidth, lineY);
        content.stroke();

        writeText(content, FONT_BOLD, 11f, COLOR_HEADER_TEXT, x + lineWidth / 2f - 30f, lineY - 14f, "ENTREGA");
        writeText(content, FONT_BOLD, 11f, COLOR_HEADER_TEXT, x + lineWidth + 80f + lineWidth / 2f - 38f, lineY - 14f, "RECEPCION");

        return lineY - 20f;
    }

    private void drawFooter(PDPageContentStream content, float pageWidth) throws IOException {
        String footer = "Documento generado por DSAM";
        float fontSize = 10f;
        float textWidth = FONT_REGULAR.getStringWidth(footer) / 1000 * fontSize;
        float x = (pageWidth - textWidth) / 2f;
        float y = 30f;
        writeText(content, FONT_REGULAR, fontSize, COLOR_FOOTER, x, y, footer);
    }

    private List<ItemRow> buildOrderedRows(ActaDto acta) {
        List<ItemRow> rows = new ArrayList<>();
        List<ActaItemDto> raw = acta.items() != null ? new ArrayList<>(acta.items()) : new ArrayList<>();

        if ((acta.equipoSerie() != null && !acta.equipoSerie().isBlank()) || acta.idEquipo() != null) {
            rows.add(new ItemRow(
                    nv(acta.equipoTipo()),
                    "-",
                    nv(acta.equipoSerie()),
                    nv(acta.equipoModelo()),
                    "-"
            ));
        }

        boolean equipoOmitido = false;
        for (ActaItemDto it : raw) {
            boolean esEquipo = (acta.equipoSerie() != null && (safeEquals(it.equipoSerie(), acta.equipoSerie()) || safeEquals(it.serie(), acta.equipoSerie())))
                    || (acta.idEquipo() != null && Objects.equals(it.equipoId(), acta.idEquipo()));
            if (esEquipo && !equipoOmitido && raw.size() > 1) {
                equipoOmitido = true;
                continue;
            }
            rows.add(new ItemRow(it.tipo(), it.marca(), it.serie(), it.modelo(), it.observacion()));
        }
        return rows;
    }

    private boolean safeEquals(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        String safe = (text == null || text.isBlank()) ? "-" : text.replace("\n", " ").trim();
        String[] words = safe.split("\\s+");
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            float candidateWidth = font.getStringWidth(candidate) / 1000 * fontSize;
            if (candidateWidth > maxWidth && !line.isEmpty()) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        if (lines.isEmpty()) {
            lines.add("-");
        }
        return lines;
    }

    private void writeText(PDPageContentStream content, PDFont font, float size, Color color, float x, float y,
                           String value) throws IOException {
        content.beginText();
        content.setNonStrokingColor(color);
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(value);
        content.endText();
    }

    private String formatFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(DATE_FORMAT) : "-";
    }

    private String nv(Object o) {
        return o == null ? "-" : o.toString();
    }

    private String nvOr(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred;
        }
        return nv(fallback);
    }

    private record ItemRow(String tipo, String marca, String serie, String modelo, String observacion) {
    }
}

