package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import java.util.List;

public interface ListarUsuariosPorClienteCasoUso {
    List<Usuario> ejecutar(Long idCliente);
}
