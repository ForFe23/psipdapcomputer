package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EquipoJpaId;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.EquipoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.EquipoSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EquipoJpaAdapter implements EquipoRepositorio {
    private final EquipoSpringRepository repository;
    private final EquipoMapper mapper;

    public EquipoJpaAdapter(EquipoSpringRepository repository, EquipoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Equipo guardar(Equipo equipo) {
        EquipoJpa entidad = mapper.toJpa(equipo);
        EquipoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> buscarPorId(EquipoId id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(new EquipoJpaId(id.id(), id.serie())).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> buscarPorSerie(String serie) {
        if (serie == null) {
            return Optional.empty();
        }
        return repository.findBySerieEquipo(serie).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listarPorEstado(String estado) {
        return repository.findByEstadoIgnoreCase(estado).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listarPorUbicacion(String ubicacion) {
        return repository.findByUbicacionUsuarioIgnoreCase(ubicacion).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void eliminar(EquipoId id) {
        if (id != null) {
            repository.deleteById(new EquipoJpaId(id.id(), id.serie()));
        }
    }
}
