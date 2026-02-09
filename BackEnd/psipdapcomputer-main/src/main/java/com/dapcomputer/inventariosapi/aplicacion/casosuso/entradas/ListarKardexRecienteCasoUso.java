package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.KardexEntrada;
import java.util.List;

public interface ListarKardexRecienteCasoUso {
    List<KardexEntrada> ejecutar(int limite);
}

