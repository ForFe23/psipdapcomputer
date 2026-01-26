package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MantenimientoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.MantenimientoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.MantenimientoSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MantenimientoJpaAdapter implements MantenimientoRepositorio {
    private final MantenimientoSpringRepository repository;
    private final MantenimientoMapper mapper;

    public MantenimientoJpaAdapter(MantenimientoSpringRepository repository, MantenimientoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        MantenimientoJpa jpa = mapper.toJpa(mantenimiento);
        if (jpa.getEstadoInterno() == null || jpa.getEstadoInterno().isBlank()) {
            jpa.setEstadoInterno("ACTIVO_INTERNAL");
        }
        MantenimientoJpa guardado = repository.save(jpa);
        return mapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mantenimiento> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> listarPorEquipoId(Long equipoId) {
        return repository.findByEquipoId(equipoId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void actualizarEstadoPorEquipo(Long equipoId, String estado) {
        repository.actualizarEstadoPorEquipo(equipoId, estado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
