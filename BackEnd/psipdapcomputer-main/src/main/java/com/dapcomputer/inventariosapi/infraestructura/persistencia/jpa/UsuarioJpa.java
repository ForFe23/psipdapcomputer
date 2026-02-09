package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioJpa {
    @Column(name = "IDCLIENTE")
    private Long idCliente;

    @Column(name = "EMPRESA_ID")
    private Long empresaId;

    @Column(name = "CEDULAUSUARIO", length = 15)
    private String cedula;

    @Column(name = "APELLIDOSUSUARIO", length = 50)
    private String apellidos;

    @Column(name = "NOMBRESUSUARIO", length = 50)
    private String nombres;

    @Column(name = "ROL_ID")
    private Long rolId;

    @ManyToOne
    @JoinColumn(name = "ROL_ID", insertable = false, updatable = false)
    private RolJpa rol;

    @Column(name = "SOLFRNRF", length = 255)
    private String solfrnrf;

    @Column(name = "CORREOUSUARIO", length = 180)
    private String correo;

    @Column(name = "TELEFONOUSUARIO", length = 50)
    private String telefono;

    @Column(name = "ESTATUSUSUARIO", length = 50)
    private String estatus;

    @Column(name = "FREGISTROUSUARIO")
    private LocalDateTime fechaRegistro;

    @Column(name = "ESTADO_INTERNO", length = 50)
    private String estadoInterno;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id;
}

