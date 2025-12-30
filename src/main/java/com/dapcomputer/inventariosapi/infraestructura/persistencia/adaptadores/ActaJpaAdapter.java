package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.ActaMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ActaJpaAdapter implements ActaRepositorio {
    private final ActaSpringRepository repository;
    private final ActaMapper mapper = new ActaMapper();

    public ActaJpaAdapter(ActaSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public Acta guardar(Acta acta) {
        ActaJpa entidad = mapper.toJpa(acta);
        ActaJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Acta> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Acta> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
