package com.dapcomputer.inventariosapi.dominio.repositorios;

public interface ActaItemRepositorio {
    void actualizarEstadoInternoPorActa(Integer idActa, String estadoInterno);
    void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno);
    void actualizarEstadoInternoPorCliente(Integer idCliente, String estadoInterno);
}
