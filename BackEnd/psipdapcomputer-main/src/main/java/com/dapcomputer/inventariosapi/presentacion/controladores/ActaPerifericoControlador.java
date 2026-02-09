package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.entidades.EstadoActa;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaItemSpringRepository;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.ActaSpringRepository;
import com.dapcomputer.inventariosapi.presentacion.dto.ActaItemPerifericoDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perifericos/acta-items")
@CrossOrigin(
        origins = {
                "http://localhost:8090",
                "http://127.0.0.1:8090",
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "http://192.168.1.7:8090",
                "https://192.168.1.7:8443"
        },
        allowCredentials = "true")
public class ActaPerifericoControlador {

    private final ActaSpringRepository actaRepository;
    private final ActaItemSpringRepository itemRepository;

    public ActaPerifericoControlador(ActaSpringRepository actaRepository, ActaItemSpringRepository itemRepository) {
        this.actaRepository = actaRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<ActaItemPerifericoDto> listar() {
        var actas = actaRepository.findAll();
        List<ActaItemPerifericoDto> resultado = new ArrayList<>();
        actas.stream()
                .filter(a -> a.getEstado() != EstadoActa.ANULADA)
                .forEach(acta -> {
                    var items = itemRepository.findByActa_Id(acta.getId());
                    items.stream()
                            .filter(it -> it.getEstadoInterno() == null || !"INACTIVO_INTERNAL".equalsIgnoreCase(it.getEstadoInterno()))
                            .forEach(it -> resultado.add(new ActaItemPerifericoDto(
                                    "acta-" + acta.getId() + "-item-" + it.getId(),
                                    it.getEquipoId(),
                                    it.getSerie() != null ? it.getSerie() : it.getEquipoSerie(),
                                    acta.getEmpresaId(),
                                    acta.getIdCliente(),
                                    it.getSerie() != null ? it.getSerie() : it.getEquipoSerie(),
                                    it.getTipo(),
                                    it.getModelo(),
                                    it.getObservacion(),
                                    it.getEstadoInterno(),
                                    "ACTA",
                                    acta.getId(),
                                    acta.getCodigo(),
                                    it.getItemNumero())));
                });
        return resultado;
    }
}

