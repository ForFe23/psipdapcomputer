package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.UsuarioMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.UsuarioSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioJpaAdapter implements UsuarioRepositorio {
    private final UsuarioSpringRepository repository;
    private final UsuarioMapper mapper = new UsuarioMapper();

    public UsuarioJpaAdapter(UsuarioSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioJpa entidad = mapper.toJpa(usuario);
        UsuarioJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> listarPorCliente(Long idCliente) {
        return repository.findByIdCliente(idCliente).stream().map(mapper::toDomain).toList();
    }
}
