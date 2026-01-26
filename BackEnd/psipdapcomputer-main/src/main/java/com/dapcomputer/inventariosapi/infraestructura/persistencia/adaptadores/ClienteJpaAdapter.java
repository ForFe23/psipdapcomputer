package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ClienteJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.ClienteMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ClienteSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteJpaAdapter implements ClienteRepositorio {
    private final ClienteSpringRepository repository;
    private final ClienteMapper mapper;

    public ClienteJpaAdapter(ClienteSpringRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpa entidad = mapper.toJpa(cliente);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        ClienteJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void actualizarEstadoInterno(Long id, String estadoInterno) {
        repository.actualizarEstadoInterno(id, estadoInterno);
    }

    @Override
    public void eliminar(Long id) {
        repository.actualizarEstadoInterno(id, "INACTIVO_INTERNAL");
    }
}
