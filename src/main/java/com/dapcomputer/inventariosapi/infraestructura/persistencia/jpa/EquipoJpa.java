package com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipo", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoJpa {
    @EmbeddedId
    private EquipoJpaId identificador;

    @Column(name = "IDCLIENTE")
    private Long idCliente;

    @Column(name = "MARCAEQUIPO", length = 50)
    private String marca;

    @Column(name = "MODELOEQUIPO", length = 50)
    private String modelo;

    @Column(name = "TIPOEQUIPO", length = 50)
    private String tipo;

    @Column(name = "SOEQUIPO", length = 50)
    private String sistemaOperativo;

    @Column(name = "PROCESADOREQUIPO", length = 255)
    private String procesador;

    @Column(name = "MEMORIAEQUIPO", length = 50)
    private String memoria;

    @Column(name = "HDDEQUIPO", length = 50)
    private String disco;

    @Column(name = "FCOMPRAEQUIPO")
    private LocalDateTime fechaCompra;

    @Column(name = "STATUSEQUIPO", length = 50)
    private String estado;

    @Column(name = "IPEQUIPO", length = 50)
    private String ip;

    @Column(name = "UBICACIONUSUARIO", length = 50)
    private String ubicacionUsuario;

    @Column(name = "DEPUSUARIO", length = 50)
    private String departamentoUsuario;

    @Column(name = "NOMBREUSUARIO", length = 50)
    private String nombreUsuario;

    @Column(name = "NOMBREPROVEEDOR", length = 50)
    private String nombreProveedor;

    @Column(name = "DIRECCIONPROVEEDOR", length = 50)
    private String direccionProveedor;

    @Column(name = "TELEFONOPROVEEDOR", length = 50)
    private String telefonoProveedor;

    @Column(name = "CONTACTOPROVEEDOR", length = 50)
    private String contactoProveedor;

    @Column(name = "CLIENTE", length = 50)
    private String cliente;

    @Column(name = "ACTIVOEQUIPO", length = 50)
    private String activo;

    @Column(name = "OFFICEEQUIPO", length = 50)
    private String office;

    @Column(name = "COSTOEQUIPO", length = 50)
    private String costo;

    @Column(name = "FACTURAEQUIPO", length = 50)
    private String factura;

    @Column(name = "NOTASEQUIPO", length = 500)
    private String notas;

    @Column(name = "CIUDADEQUIPO", length = 50)
    private String ciudad;

    @Column(name = "NOMBREEQUIPO", length = 50)
    private String nombre;

    @Column(name = "ALIAS", length = 255)
    private String alias;
}
