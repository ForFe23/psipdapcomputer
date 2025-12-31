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

@Repository
public class MovimientoJpaAdapter implements MovimientoRepositorio {
    private final MovimientoSpringRepository repository;
    private final ActaSpringRepository actaRepository;
    private final MovimientoMapper mapper = new MovimientoMapper();

    public MovimientoJpaAdapter(MovimientoSpringRepository repository, ActaSpringRepository actaRepository) {
        this.repository = repository;
        this.actaRepository = actaRepository;
    }

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        ActaJpa acta = movimiento.idActa() != null ? actaRepository.findById(movimiento.idActa()).orElse(null) : null;
        MovimientoJpa entidad = mapper.toJpa(movimiento, acta);
        MovimientoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public List<Movimiento> listarPorEquipo(String serieEquipo) {
        return repository.findBySerieEquipo(serieEquipo).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Movimiento> listarPorUsuario(Integer idUsuario) {
        return repository.findByIdUsuarioOrigenOrIdUsuarioDestino(idUsuario, idUsuario).stream().map(mapper::toDomain).toList();
    }
}
