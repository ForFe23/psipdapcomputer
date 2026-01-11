package com.dapcomputer.inventariosapi.dominio.repositorios;

import java.util.Optional;
import java.util.List;
import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;


public interface IClienteRepositorio {

	Cliente guardar (Cliente cliente);
	
	Optional<Cliente> buscarPorId(int id);
	
	List<Cliente> ListarTodos();
	
	void eliminar (int id);
	
}
