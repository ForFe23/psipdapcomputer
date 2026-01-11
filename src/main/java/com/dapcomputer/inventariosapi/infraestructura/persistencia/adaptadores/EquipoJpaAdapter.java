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

@Repository
public class EquipoJpaAdapter implements EquipoRepositorio {
    private final EquipoSpringRepository repository;
    private final EquipoMapper mapper = new EquipoMapper();

    public EquipoJpaAdapter(EquipoSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public Equipo guardar(Equipo equipo) {
        EquipoJpa entidad = mapper.toJpa(equipo);
        if (entidad.getIdentificador() == null) {
            entidad.setIdentificador(new EquipoJpaId(null, equipo.identificador() != null ? equipo.identificador().serie() : null));
        }
        EquipoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Equipo> buscarPorId(EquipoId id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(new EquipoJpaId(id.id(), id.serie())).map(mapper::toDomain);
    }

    @Override
    public List<Equipo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Equipo> buscarPorSerie(String serie) {
        if (serie == null) {
            return Optional.empty();
        }
        return repository.findByIdentificador_Serie(serie).map(mapper::toDomain);
    }
}
