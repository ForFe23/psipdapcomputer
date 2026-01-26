package com.dapcomputer.inventariosapi.dominio.repositorios;

import com.dapcomputer.inventariosapi.dominio.entidades.ActaAdjunto;
import java.util.List;

public interface ActaAdjuntoRepositorio {
    ActaAdjunto guardar(ActaAdjunto adjunto);
    List<ActaAdjunto> listarPorActa(Integer idActa);
}
