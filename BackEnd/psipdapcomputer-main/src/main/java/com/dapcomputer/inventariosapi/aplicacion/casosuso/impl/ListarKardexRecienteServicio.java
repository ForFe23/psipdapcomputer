package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarKardexRecienteCasoUso;
import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.RegistrarKardexMovimientoCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import com.dapcomputer.inventariosapi.dominio.entidades.Movimiento;
import com.dapcomputer.inventariosapi.dominio.repositorios.KardexRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MovimientoRepositorio;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ListarKardexRecienteServicio implements ListarKardexRecienteCasoUso {
    private final KardexRepositorio kardexRepositorio;
    private final MovimientoRepositorio movimientoRepositorio;
    private final RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso;

    public ListarKardexRecienteServicio(KardexRepositorio kardexRepositorio, MovimientoRepositorio movimientoRepositorio, RegistrarKardexMovimientoCasoUso registrarKardexMovimientoCasoUso) {
        this.kardexRepositorio = kardexRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
        this.registrarKardexMovimientoCasoUso = registrarKardexMovimientoCasoUso;
    }

    @Override
    public List<KardexEntrada> ejecutar(int limite) {
        int cantidad = limite > 0 ? limite : 10;
        List<KardexEntrada> existentes = kardexRepositorio.listarRecientes(cantidad);
        if (existentes.isEmpty()) {
            movimientoRepositorio.listar().stream()
                    .sorted(Comparator.comparing((Movimiento m) -> Optional.ofNullable(m.fecha()).orElse(LocalDateTime.MIN)).reversed())
                    .limit(cantidad)
                    .forEach(mov -> registrarKardexMovimientoCasoUso.ejecutar(mov, "HISTORICO"));
            existentes = kardexRepositorio.listarRecientes(cantidad);
        }
        return existentes.stream().limit(cantidad).toList();
    }
}
