package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.MovimientoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.MovimientoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.EquipoSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.MovimientoSpringRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MovimientoJpaAdapter implements MovimientoRepositorio {
    private final MovimientoSpringRepository repository;
    private final ActaSpringRepository actaRepository;
    private final EquipoSpringRepository equipoRepository;
    private final MovimientoMapper mapper;

    public MovimientoJpaAdapter(MovimientoSpringRepository repository, ActaSpringRepository actaRepository, EquipoSpringRepository equipoRepository, MovimientoMapper mapper) {
        this.repository = repository;
        this.actaRepository = actaRepository;
        this.equipoRepository = equipoRepository;
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
        if (guardado.getIdEquipo() != null && guardado.getUbicacionDestinoId() != null) {
            equipoRepository.actualizarUbicacion(guardado.getIdEquipo(), guardado.getUbicacionDestinoId(), guardado.getUbicacionDestino());
        }
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
    public java.util.Optional<Movimiento> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> buscarPorActa(Integer idActa) {
        if (idActa == null) {
            return Optional.empty();
        }
        return repository.findFirstByActa_IdOrderByIdDesc(idActa).map(mapper::toDomain);
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

    @Override
    @Transactional
    public void actualizarEstadoInterno(Integer id, String estadoInterno) {
        repository.actualizarEstadoInterno(id, estadoInterno);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorCliente(Long idCliente, String estadoInterno) {
        repository.actualizarEstadoInternoPorCliente(idCliente, estadoInterno);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorActa(Integer idActa, String estadoInterno) {
        repository.actualizarEstadoInternoPorActa(idActa, estadoInterno);
    }
}
