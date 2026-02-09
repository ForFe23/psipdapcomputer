package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import com.dapcomputer.inventariosapi.infraestructura.persistencia.jpa.RolJpa;
import com.dapcomputer.inventariosapi.infraestructura.repositorios.RolSpringRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BootstrapRoles {
    private final RolSpringRepository rolRepository;
    private static final String INTERNO_CODE = "PERSONAL_INTERNO";

    public BootstrapRoles(RolSpringRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void ensureInternalRole() {
        rolRepository.findByCodigo(INTERNO_CODE)
                .orElseGet(() -> rolRepository.save(new RolJpa(null, INTERNO_CODE, "PERSONAL INTERNO")));
    }
}

