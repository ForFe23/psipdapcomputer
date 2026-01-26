package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acta", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "acta_code", length = 40, unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoActa estado;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;

    @Column(name = "empresa_id")
    private Long empresaId;

    @ManyToOne
    @JoinColumn(name = "empresa_id", insertable = false, updatable = false)
    private EmpresaJpa empresa;

    @Column(name = "ubicacion_id")
    private Long ubicacionId;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", insertable = false, updatable = false)
    private UbicacionJpa ubicacion;

    @Column(name = "tipo", length = 30)
    private String tipo;

    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "firmado_entrego_id")
    private Integer firmadoEntregoId;

    @ManyToOne
    @JoinColumn(name = "firmado_entrego_id", insertable = false, updatable = false)
    private UsuarioJpa firmadoEntrego;

    @Column(name = "firmado_recibio_id")
    private Integer firmadoRecibioId;

    @ManyToOne
    @JoinColumn(name = "firmado_recibio_id", insertable = false, updatable = false)
    private UsuarioJpa firmadoRecibio;

    @Column(name = "idCliente")
    private Integer idCliente;

    @Column(name = "id_equipo")
    private Integer idEquipo;

    @Column(name = "movimiento_id")
    private Integer movimientoId;

    @Column(name = "fecha_acta")
    private LocalDate fechaActa;

    @Column(name = "tema", length = 255)
    private String tema;

    @Column(name = "entregado_por", length = 120)
    private String entregadoPor;

    @Column(name = "recibido_por", length = 120)
    private String recibidoPor;

    @Column(name = "cargo_entrega", length = 120)
    private String cargoEntrega;

    @Column(name = "cargo_recibe", length = 120)
    private String cargoRecibe;

    @Column(name = "depUsuario", length = 120)
    private String departamentoUsuario;

    @Column(name = "ciudadEquipo", length = 120)
    private String ciudadEquipo;

    @Column(name = "ubicacionUsuario", length = 120)
    private String ubicacionUsuario;

    @Column(name = "obs_generales")
    private String observacionesGenerales;

    @Column(name = "equipo_tipo", length = 120)
    private String equipoTipo;

    @Column(name = "equipo_serie", length = 120)
    private String equipoSerie;

    @Column(name = "equipo_modelo", length = 120)
    private String equipoModelo;

    @Column(name = "created_at")
    private LocalDateTime creadoEn;

    @Column(name = "created_by", length = 120)
    private String creadoPor;

    @OneToMany(mappedBy = "acta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActaItemJpa> items = new ArrayList<>();
}
