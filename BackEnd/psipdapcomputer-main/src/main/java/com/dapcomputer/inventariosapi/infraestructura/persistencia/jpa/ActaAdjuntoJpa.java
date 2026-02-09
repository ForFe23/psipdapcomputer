package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acta_adjunto", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActaAdjuntoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "acta_id", nullable = false)
    private ActaJpa acta;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "content_type", length = 150)
    private String contentType;

    @Column(name = "tamano")
    private Long tamano;

    @Column(name = "fecha_registro")
    private java.time.LocalDateTime fechaRegistro;

    @Column(name = "estado_interno", length = 50)
    private String estadoInterno;
}

