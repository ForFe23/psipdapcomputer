package com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import java.util.List;

public interface ListarUsuariosPorEmpresaCasoUso {
    List<Usuario> ejecutar(Long empresaId);
}
