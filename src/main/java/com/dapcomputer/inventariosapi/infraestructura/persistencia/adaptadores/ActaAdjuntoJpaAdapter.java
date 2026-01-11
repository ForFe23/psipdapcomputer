package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaAdjuntoRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaAdjuntoJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.ActaAdjuntoMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaAdjuntoSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class ActaAdjuntoJpaAdapter implements ActaAdjuntoRepositorio {
    private final ActaAdjuntoSpringRepository repository;
    private final ActaSpringRepository actaRepository;
    private final ActaAdjuntoMapper mapper;

    public ActaAdjuntoJpaAdapter(ActaAdjuntoSpringRepository repository, ActaSpringRepository actaRepository, ActaAdjuntoMapper mapper) {
        this.repository = repository;
        this.actaRepository = actaRepository;
        this.mapper = mapper;
    }

    @Override
    public ActaAdjunto guardar(ActaAdjunto adjunto) {
        ActaJpa acta = adjunto.idActa() != null ? actaRepository.findById(adjunto.idActa()).orElse(null) : null;
        if (acta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Acta no encontrada");
        }
        ActaAdjuntoJpa entidad = mapper.toJpa(adjunto, acta);
        ActaAdjuntoJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public List<ActaAdjunto> listarPorActa(Integer idActa) {
        return repository.findByActa_Id(idActa).stream().map(mapper::toDomain).toList();
    }
}
