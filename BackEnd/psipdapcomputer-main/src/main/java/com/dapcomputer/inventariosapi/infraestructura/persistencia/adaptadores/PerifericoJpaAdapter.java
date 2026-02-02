package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.PerifericoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.PerifericoSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PerifericoJpaAdapter implements PerifericoRepositorio {
    private final PerifericoSpringRepository repository;
    private final PerifericoMapper mapper;

    public PerifericoJpaAdapter(PerifericoSpringRepository repository, PerifericoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Periferico guardar(Periferico periferico) {
        PerifericoJpa entidad = mapper.toJpa(periferico);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        PerifericoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Periferico> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Periferico> listarPorEquipo(Integer equipoId) {
        if (equipoId == null) {
            return List.of();
        }
        return repository.findByEquipoId(equipoId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Periferico> listarPorSerie(String serieEquipo) {
        if (serieEquipo == null) {
            return List.of();
        }
        return repository.findBySerieEquipo(serieEquipo).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Periferico> listarPorCliente(Integer idCliente) {
        if (idCliente == null) {
            return List.of();
        }
        return repository.findByIdCliente(idCliente).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Periferico> listarTodos() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
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
    public void actualizarEstadoInternoPorCliente(Integer idCliente, String estadoInterno) {
        if (idCliente != null) {
            repository.actualizarEstadoInternoPorCliente(idCliente, estadoInterno);
        }
    }

    @Override
    public void eliminar(Integer id) {
        if (id != null) {
            repository.deleteById(id);
        }
    }
}
