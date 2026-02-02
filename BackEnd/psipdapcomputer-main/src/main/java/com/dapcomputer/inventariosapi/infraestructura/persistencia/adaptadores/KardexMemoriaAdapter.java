package com.dapcomputer.inventariosapi.infraestructura.persistencia.adaptadores;

import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import com.dapcomputer.inventariosapi.dominio.repositorios.KardexRepositorio;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Repository;

@Repository
public class KardexMemoriaAdapter implements KardexRepositorio {
    private static final int MAX_REGISTROS = 500;
    private final Deque<KardexEntrada> historial = new ConcurrentLinkedDeque<>();

    @Override
    public void registrar(KardexEntrada entrada) {
        if (entrada == null) {
            return;
        }
        historial.addFirst(entrada);
        while (historial.size() > MAX_REGISTROS) {
            historial.pollLast();
        }
    }

    @Override
    public List<KardexEntrada> listarRecientes(int limite) {
        int cantidad = limite > 0 ? limite : 10;
        List<KardexEntrada> resultado = new ArrayList<>();
        int contador = 0;
        for (KardexEntrada entrada : historial) {
            if (contador >= cantidad) {
                break;
            }
            resultado.add(entrada);
            contador++;
        }
        return resultado;
    }
}
