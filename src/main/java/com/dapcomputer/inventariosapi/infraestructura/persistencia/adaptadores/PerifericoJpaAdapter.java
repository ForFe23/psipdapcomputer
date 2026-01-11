package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.EquipoId;
import com.dapcomputer.inventariosapi.dominio.entidades.Periferico;
import com.dapcomputer.inventariosapi.dominio.repositorios.PerifericoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.PerifericoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.PerifericoSpringRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

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
        PerifericoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public List<Periferico> listarPorEquipo(EquipoId id) {
        if (id == null) {
            return List.of();
        }
        return repository.findByIdIdAndIdSerieEquipo(id.id(), id.serie()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Periferico> listarPorSerie(String serieEquipo) {
        if (serieEquipo == null) {
            return List.of();
        }
        return repository.findByIdSerieEquipo(serieEquipo).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Periferico> listarTodos() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void eliminar(EquipoId id) {
        if (id != null) {
            repository.deleteById(new com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PerifericoJpaId(id.id(), id.serie()));
        }
    }
}
