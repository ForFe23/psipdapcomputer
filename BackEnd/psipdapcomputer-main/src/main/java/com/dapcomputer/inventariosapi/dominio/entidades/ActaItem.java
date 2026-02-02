package com.dapcomputer.inventariosapi.dominio.entidades;

public record ActaItem(Integer id, Integer actaId, Integer itemNumero, String tipo, String serie, String modelo, String observacion, Integer equipoId, String equipoSerie, String estadoInterno) {
}
