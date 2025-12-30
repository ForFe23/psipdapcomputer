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
    private final PerifericoMapper mapper = new PerifericoMapper();

    public PerifericoJpaAdapter(PerifericoSpringRepository repository) {
        this.repository = repository;
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
}
