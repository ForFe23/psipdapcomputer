package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UbicacionJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.UbicacionMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.UbicacionSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UbicacionJpaAdapter implements IUbicacionRepositorio {
    private final UbicacionSpringRepository repository;
    private final UbicacionMapper mapper;

    public UbicacionJpaAdapter(UbicacionSpringRepository repository, UbicacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        UbicacionJpa entidad = mapper.toJpa(ubicacion);
        if (entidad.getClienteId() == null && entidad.getEmpresaId() == null) {
            throw new IllegalArgumentException("La ubicación debe pertenecer a un cliente o a una empresa.");
        }
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        entidad.setNombre(normalizar(entidad.getNombre()));
        entidad.setDireccion(normalizar(entidad.getDireccion()));
        entidad.setEstado(normalizar(entidad.getEstado()));
        return mapper.toDomain(repository.save(entidad));
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion) {
        return guardar(ubicacion);
    }

    @Override
    public Optional<Ubicacion> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Ubicacion> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Ubicacion> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Ubicacion> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void actualizarEstadoInternoPorEmpresa(Long empresaId, String estadoInterno) {
        repository.actualizarEstadoInternoPorEmpresa(empresaId, estadoInterno);
    }

    @Override
    public void actualizarEstadoInternoPorCliente(Long clienteId, String estadoInterno) {
        repository.actualizarEstadoInternoPorCliente(clienteId, estadoInterno);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim().toUpperCase();
    }
}

