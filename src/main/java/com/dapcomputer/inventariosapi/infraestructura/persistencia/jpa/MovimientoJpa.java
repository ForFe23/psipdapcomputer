package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import com.dapcomputer.inventariosapi.dominio.entidades.TipoMovimiento;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movimiento_equipo", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "acta_id")
    private ActaJpa acta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "equipo_id", referencedColumnName = "ID", insertable = false, updatable = false),
            @JoinColumn(name = "equipo_serie", referencedColumnName = "SERIEEQUIPO", insertable = false, updatable = false)
    })
    private EquipoJpa equipo;

    @Column(name = "equipo_id")
    private Integer idEquipo;

    @Column(name = "equipo_serie", length = 50)
    private String serieEquipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", length = 20)
    private TipoMovimiento tipo;

    @Column(name = "ubicacion_origen", length = 120)
    private String ubicacionOrigen;

    @Column(name = "ubicacion_destino", length = 120)
    private String ubicacionDestino;

    @Column(name = "usuario_origen_id")
    private Integer idUsuarioOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_origen_id", insertable = false, updatable = false)
    private UsuarioJpa usuarioOrigen;

    @Column(name = "usuario_destino_id")
    private Integer idUsuarioDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destino_id", insertable = false, updatable = false)
    private UsuarioJpa usuarioDestino;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fecha;

    @Column(name = "observacion", length = 255)
    private String observacion;
}
