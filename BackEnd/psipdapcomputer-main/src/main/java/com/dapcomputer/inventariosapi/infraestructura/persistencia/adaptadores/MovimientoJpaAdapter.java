package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.MovimientoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.MovimientoSpringRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MovimientoJpaAdapter implements MovimientoRepositorio {
    private final MovimientoSpringRepository repository;
    private final ActaSpringRepository actaRepository;
    private final MovimientoMapper mapper;

    public MovimientoJpaAdapter(MovimientoSpringRepository repository, ActaSpringRepository actaRepository, MovimientoMapper mapper) {
        this.repository = repository;
        this.actaRepository = actaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Movimiento guardar(Movimiento movimiento) {
        ActaJpa acta = movimiento.idActa() != null ? actaRepository.findById(movimiento.idActa()).orElse(null) : null;
        MovimientoJpa entidad = mapper.toJpa(movimiento, acta);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        MovimientoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> listarPorEquipo(Integer equipoId) {
        if (equipoId == null) {
            return List.of();
        }
        return repository.findByIdEquipo(equipoId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> listarPorUsuario(Integer idUsuario) {
        return repository.findByIdUsuarioOrigenOrIdUsuarioDestino(idUsuario, idUsuario).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno) {
        repository.actualizarEstadoInternoPorEquipo(idEquipo, estadoInterno);
    }
}
