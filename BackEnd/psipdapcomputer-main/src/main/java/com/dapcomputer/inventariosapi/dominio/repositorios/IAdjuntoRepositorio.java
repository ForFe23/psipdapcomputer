package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.modelo.ActaAdjunto;
import java.util.List;

public interface IAdjuntoRepositorio {
    ActaAdjunto guardar(ActaAdjunto adjunto);
    List<ActaAdjunto> listarPorActa(Integer actaId);
}

