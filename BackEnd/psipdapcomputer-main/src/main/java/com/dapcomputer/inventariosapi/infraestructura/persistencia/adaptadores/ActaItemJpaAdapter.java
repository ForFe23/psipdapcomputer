package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.repositorios.ActaItemRepositorio;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaItemSpringRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ActaItemJpaAdapter implements ActaItemRepositorio {
    private final ActaItemSpringRepository repository;

    public ActaItemJpaAdapter(ActaItemSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorActa(Integer idActa, String estadoInterno) {
        repository.actualizarEstadoInternoPorActa(idActa, estadoInterno);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno) {
        repository.actualizarEstadoInternoPorEquipo(idEquipo, estadoInterno);
    }

    @Override
    @Transactional
    public void actualizarEstadoInternoPorCliente(Integer idCliente, String estadoInterno) {
        repository.actualizarEstadoInternoPorCliente(idCliente, estadoInterno);
    }
}

