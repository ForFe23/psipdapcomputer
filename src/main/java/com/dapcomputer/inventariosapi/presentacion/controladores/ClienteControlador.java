package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.CrearClienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarClientesCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ObtenerClienteCasoUso;
import com.dapcomputer.inventariosapi.presentacion.dto.ClienteDto;
import com.dapcomputer.inventariosapi.presentacion.mapeadores.ClienteDtoMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {
    private final CrearClienteCasoUso crearCliente;
    private final ListarClientesCasoUso listarClientes;
    private final ObtenerClienteCasoUso obtenerCliente;
    private final ClienteDtoMapper mapper = new ClienteDtoMapper();

    public ClienteControlador(CrearClienteCasoUso crearCliente, ListarClientesCasoUso listarClientes, ObtenerClienteCasoUso obtenerCliente) {
        this.crearCliente = crearCliente;
        this.listarClientes = listarClientes;
        this.obtenerCliente = obtenerCliente;
    }

    @PostMapping
    public ResponseEntity<ClienteDto> crear(@Valid @RequestBody ClienteDto solicitud) {
        var creado = crearCliente.ejecutar(mapper.toDomain(solicitud));
        return ResponseEntity.ok(mapper.toDto(creado));
    }

    @GetMapping
    public List<ClienteDto> listar() {
        return listarClientes.ejecutar().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        var encontrado = obtenerCliente.ejecutar(id);
        return ResponseEntity.ok(mapper.toDto(encontrado));
    }
}
