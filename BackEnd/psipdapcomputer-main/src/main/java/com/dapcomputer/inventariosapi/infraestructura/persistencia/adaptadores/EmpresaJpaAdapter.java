package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import com.dapcomputer.inventariosapi.dominio.repositorios.IEmpresaRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.EmpresaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.EmpresaMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.EmpresaSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EmpresaJpaAdapter implements IEmpresaRepositorio {
    private final EmpresaSpringRepository repository;
    private final EmpresaMapper mapper;

    public EmpresaJpaAdapter(EmpresaSpringRepository repository, EmpresaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Empresa guardar(Empresa empresa) {
        EmpresaJpa jpa = mapper.toJpa(empresa);
        if (jpa.getEstadoInterno() == null || jpa.getEstadoInterno().isBlank()) {
            jpa.setEstadoInterno("ACTIVO_INTERNAL");
        }
        return mapper.toDomain(repository.save(jpa));
    }

    @Override
    @Transactional
    public Empresa actualizar(Empresa empresa) {
        return guardar(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void actualizarEstadoInterno(Long id, String estadoInterno) {
        repository.actualizarEstadoInterno(id, estadoInterno);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorCliente(Long clienteId, String estadoInterno) {
        repository.actualizarEstadoInternoPorCliente(clienteId, estadoInterno);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
