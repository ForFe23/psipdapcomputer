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
    private final UsuarioMapper mapper;

    public UsuarioJpaAdapter(UsuarioSpringRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioJpa entidad = mapper.toJpa(usuario);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
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

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void actualizarEstadoInterno(Integer id, String estadoInterno, String estatus) {
        repository.actualizarEstadoInterno(id, estadoInterno, estatus);
    }

    @Override
    public void actualizarEstadoInternoPorCliente(Long idCliente, String estadoInterno, String estatus) {
        repository.actualizarEstadoInternoPorCliente(idCliente, estadoInterno, estatus);
    }

    @Override
    public void eliminar(Integer id) {
        repository.actualizarEstadoInterno(id, "INACTIVO_INTERNAL", "INACTIVO");
    }
}
