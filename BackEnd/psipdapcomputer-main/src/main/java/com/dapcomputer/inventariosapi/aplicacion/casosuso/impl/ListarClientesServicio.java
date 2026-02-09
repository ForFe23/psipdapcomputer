package com.dapcomputer.inventariosapi.aplicacion.casosuso.impl;

import com.dapcomputer.inventariosapi.aplicacion.casosuso.entradas.ListarClientesCasoUso;
import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import java.util.List;

public class ListarClientesServicio implements ListarClientesCasoUso {
	private final ClienteRepositorio repositorio;

	public ListarClientesServicio(ClienteRepositorio repositorio) {
		this.repositorio = repositorio;
	}

	@Override
	public List<Cliente> ejecutar() {
		return repositorio.listar();
	}
}

