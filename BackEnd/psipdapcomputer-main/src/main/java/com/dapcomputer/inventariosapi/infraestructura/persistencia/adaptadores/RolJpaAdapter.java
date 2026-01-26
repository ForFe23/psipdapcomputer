package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Rol;
import com.dapcomputer.inventariosapi.dominio.repositorios.RolRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.RolMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.RolSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RolJpaAdapter implements RolRepositorio {
    private final RolSpringRepository repository;
    private final RolMapper mapper;

    public RolJpaAdapter(RolSpringRepository repository, RolMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Rol guardar(Rol rol) {
        var entidad = mapper.toJpa(rol);
        var guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Rol> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Rol> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo).map(mapper::toDomain);
    }

    @Override
    public List<Rol> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
