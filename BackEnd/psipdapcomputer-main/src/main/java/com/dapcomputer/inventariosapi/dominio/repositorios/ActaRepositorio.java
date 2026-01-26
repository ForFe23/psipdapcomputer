package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActaRepositorio {
    Acta guardar(Acta acta);

    Optional<Acta> buscarPorId(Integer id);

    List<Acta> listar();

    List<Acta> listarPorEstado(EstadoActa estado);

    List<Acta> listarPorRangoFecha(LocalDate inicio, LocalDate fin);

    List<Acta> listarPorCliente(Integer idCliente);

    List<Acta> listarPorUsuario(String nombre);

    void actualizarEstadoInternoPorEquipo(Integer idEquipo, String estadoInterno);
}
