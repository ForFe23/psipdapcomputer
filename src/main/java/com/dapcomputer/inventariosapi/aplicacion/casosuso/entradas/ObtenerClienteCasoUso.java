package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;

public interface ObtenerClienteCasoUso {
    Cliente ejecutar(Long id);
}
