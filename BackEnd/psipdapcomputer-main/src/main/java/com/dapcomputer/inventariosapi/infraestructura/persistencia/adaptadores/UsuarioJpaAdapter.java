package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.RolJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.UsuarioJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.UsuarioMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.RolSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.UsuarioSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioJpaAdapter implements UsuarioRepositorio {
    private final UsuarioSpringRepository repository;
    private final RolSpringRepository rolRepository;
    private final UsuarioMapper mapper;

    public UsuarioJpaAdapter(UsuarioSpringRepository repository, RolSpringRepository rolRepository, UsuarioMapper mapper) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        Long rolId = usuario.rol() != null ? usuario.rol().id() : null;
        String rolCodigo = usuario.rol() != null ? usuario.rol().codigo() : null;
        if (rolId == null && rolCodigo != null && !rolCodigo.isBlank()) {
            String codigoUpper = rolCodigo.toUpperCase().trim();
            RolJpa rol = rolRepository.findByCodigo(codigoUpper)
                    .orElseGet(() -> rolRepository.save(new RolJpa(null, codigoUpper, "PERSONAL INTERNO")));
            rolId = rol.getId();
        }

        UsuarioJpa entidad = mapper.toJpa(usuario);
        entidad.setRolId(rolId);
        entidad.setCedula(normalizar(entidad.getCedula()));
        entidad.setApellidos(normalizar(entidad.getApellidos()));
        entidad.setNombres(normalizar(entidad.getNombres()));
        entidad.setCorreo(normalizar(entidad.getCorreo()));
        entidad.setTelefono(normalizar(entidad.getTelefono()));
        entidad.setSolfrnrf(normalizar(entidad.getSolfrnrf()));
        entidad.setEstatus(normalizar(entidad.getEstatus() != null ? entidad.getEstatus() : "ACTIVO"));
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
    public List<Usuario> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(mapper::toDomain).toList();
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

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim().toUpperCase();
    }
}
