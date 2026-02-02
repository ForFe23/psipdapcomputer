package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.ActaMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ActaJpaAdapter implements ActaRepositorio {
    private final ActaSpringRepository repository;
    private final ActaMapper mapper;

    public ActaJpaAdapter(ActaSpringRepository repository, ActaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Acta guardar(Acta acta) {
        ActaJpa entidad = mapper.toJpa(acta);
        if (entidad.getEstadoInterno() == null || entidad.getEstadoInterno().isBlank()) {
            entidad.setEstadoInterno("ACTIVO_INTERNAL");
        }
        if (entidad.getItems() != null) {
            entidad.getItems().forEach(item -> {
                if (item.getEstadoInterno() == null || item.getEstadoInterno().isBlank()) {
                    item.setEstadoInterno("ACTIVO_INTERNAL");
                }
            });
        }
        ActaJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Acta> buscarPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acta> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acta> listarPorEstado(EstadoActa estado) {
        return repository.findByEstado(estado).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acta> listarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        return repository.findByFechaActaBetween(inicio, fin).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acta> listarPorCliente(Integer idCliente) {
        return repository.findByIdCliente(idCliente).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acta> listarPorUsuario(String nombre) {
        return repository.findByEntregadoPorIgnoreCaseOrRecibidoPorIgnoreCase(nombre, nombre).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno) {
        if (idEquipo != null) {
            repository.actualizarEstadoInternoPorEquipo(idEquipo, estadoInterno);
        }
    }

    @Override
    @Transactional
    public void actualizarEstadoInterno(Integer id, String estadoInterno) {
        if (id != null) {
            repository.actualizarEstadoInterno(id, estadoInterno);
        }
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorCliente(Integer idCliente, String estadoInterno) {
        if (idCliente != null) {
            repository.actualizarEstadoInternoPorCliente(idCliente, estadoInterno);
        }
    }
}
