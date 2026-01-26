package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.IncidenteJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.IncidenteMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.IncidenteSpringRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class IncidenteJpaAdapter implements IncidenteRepositorio {
    private final IncidenteSpringRepository repository;
    private final IncidenteMapper mapper;

    public IncidenteJpaAdapter(IncidenteSpringRepository repository, IncidenteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Incidente guardar(Incidente incidente) {
        IncidenteJpa entidad = mapper.toJpa(incidente);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        IncidenteJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidente> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidente> listarPorCliente(Long idCliente) {
        return repository.findByIdCliente(idCliente).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidente> listarPorEquipo(Integer equipoId) {
        return repository.findByEquipoId(equipoId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidente> listarPorUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Incidente> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorEquipo(Integer equipoId, String estadoInterno) {
        if (equipoId != null) {
            repository.actualizarEstadoInternoPorEquipo(equipoId, estadoInterno);
        }
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
