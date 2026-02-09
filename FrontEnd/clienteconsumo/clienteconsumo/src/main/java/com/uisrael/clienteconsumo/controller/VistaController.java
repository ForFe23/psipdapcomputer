package com.uisrael.clienteconsumo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class VistaController {

    @GetMapping({"/", "/inicio"})
    public String inicio() {
        
        return "redirect:/app/index.html";
    }

    @GetMapping("/clientes")
    public String listarClientes() {
        return "clientes/listar";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoCliente() {
        return "clientes/nuevo";
    }

    @GetMapping("/equipos")
    public String listarEquipos() {
        return "equipos/listar";
    }

    @GetMapping("/equipos/nuevo")
    public String nuevoEquipo() {
        return "equipos/nuevo";
    }

    @GetMapping("/actas")
    public String listarActas() {
        return "actas/listar";
    }

    @GetMapping("/actas/nuevo")
    public String nuevaActa() {
        return "actas/nuevo";
    }

    @GetMapping("/perifericos")
    public String listarPerifericos() {
        return "perifericos/listar";
    }

    @GetMapping("/perifericos/nuevo")
    public String nuevoPeriferico() {
        return "perifericos/nuevo";
    }

    @GetMapping("/movimientos")
    public String listarMovimientos() {
        return "movimientos/listar";
    }

    @GetMapping("/movimientos/nuevo")
    public String nuevoMovimiento() {
        return "movimientos/nuevo";
    }

    @GetMapping("/mantenimientos")
    public String listarMantenimientos() {
        return "mantenimientos/listar";
    }

    @GetMapping("/mantenimientos/nuevo")
    public String nuevoMantenimiento() {
        return "mantenimientos/nuevo";
    }

    @GetMapping("/adjuntos")
    public String listarAdjuntos() {
        return "adjuntos/listar";
    }

    @GetMapping("/adjuntos/nuevo")
    public String nuevoAdjunto() {
        return "adjuntos/nuevo";
    }

    @GetMapping("/incidentes")
    public String listarIncidentes() {
        return "incidentes/listar";
    }

    @GetMapping("/incidentes/nuevo")
    public String nuevoIncidente() {
        return "incidentes/nuevo";
    }
}

