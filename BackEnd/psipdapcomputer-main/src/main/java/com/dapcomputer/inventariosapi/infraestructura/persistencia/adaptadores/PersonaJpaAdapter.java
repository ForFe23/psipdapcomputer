package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Persona;
import com.dapcomputer.inventariosapi.dominio.repositorios.IPersonaRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.PersonaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.PersonaMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.PersonaSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PersonaJpaAdapter implements IPersonaRepositorio {
    private final PersonaSpringRepository repository;
    private final PersonaMapper mapper;

    public PersonaJpaAdapter(PersonaSpringRepository repository, PersonaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Persona guardar(Persona persona) {
        PersonaJpa entidad = mapper.toJpa(persona);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        entidad.setCedula(normalizar(entidad.getCedula()));
        entidad.setApellidos(normalizar(entidad.getApellidos()));
        entidad.setNombres(normalizar(entidad.getNombres()));
        entidad.setCorreo(normalizar(entidad.getCorreo()));
        entidad.setTelefono(normalizar(entidad.getTelefono()));
        return mapper.toDomain(repository.save(entidad));
    }

    @Override
    public Persona actualizar(Persona persona) {
        return guardar(persona);
    }

    @Override
    public Optional<Persona> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Persona> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Persona> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void actualizarEstadoInternoPorEmpresa(Long empresaId, String estadoInterno) {
        repository.actualizarEstadoInternoPorEmpresa(empresaId, estadoInterno);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim().toUpperCase();
    }
}
