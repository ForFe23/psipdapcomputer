package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import com.dapcomputer.inventariosapi.dominio.entidades.CargoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "CEDULAUSUARIO", length = 15)
    private String cedula;

    @Column(name = "APELLIDOSUSUARIO", length = 50)
    private String apellidos;

    @Column(name = "NOMBRESUSUARIO", length = 50)
    private String nombres;

    @Enumerated(EnumType.STRING)
    @Column(name = "CARGOUSUARIO", length = 20)
    private CargoUsuario cargo;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id;
}
