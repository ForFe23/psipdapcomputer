package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaSecuencia;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaSecuenciaRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpa;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.ActaSecuenciaJpaId;
import com.dapcomputer.inventariosapi.infraestructura.persistencia.mapeadores.ActaSecuenciaMapper;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSecuenciaSpringRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ActaSecuenciaJpaAdapter implements ActaSecuenciaRepositorio {
    private final ActaSecuenciaSpringRepository repository;
    private final ActaSecuenciaMapper mapper;

    public ActaSecuenciaJpaAdapter(ActaSecuenciaSpringRepository repository, ActaSecuenciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ActaSecuencia guardar(ActaSecuencia secuencia) {
        ActaSecuenciaJpa entidad = mapper.toJpa(secuencia);
        ActaSecuenciaJpa guardado = repository.save(entidad);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<ActaSecuencia> buscarPorClienteYAnio(Integer idCliente, Integer anio) {
        return repository.findById(new ActaSecuenciaJpaId(idCliente, anio)).map(mapper::toDomain);
    }
}

