package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.infraestructura.servicios.SoftDeleteMode;
import com.dapcomputer.inventariosapi.infraestructura.servicios.SoftDeletePolicy;
import com.dapcomputer.inventariosapi.infraestructura.servicios.SoftDeleteResult;
import com.dapcomputer.inventariosapi.infraestructura.servicios.SoftDeleteService;
import com.dapcomputer.inventariosapi.infraestructura.servicios.UbicacionChildPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/soft-delete")
public class SoftDeleteControlador {
    private final SoftDeleteService service;

    public SoftDeleteControlador(SoftDeleteService service) {
        this.service = service;
    }

    @PostMapping("/cliente/{id}")
    public ResponseEntity<SoftDeleteResult> cliente(@PathVariable Long id,
                                                    @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                    @RequestParam(required = false) String reason,
                                                    @RequestParam(required = false) String deletedBy) {
        SoftDeleteResult r = service.softDeleteCliente(id, reason, deletedBy, mode, SoftDeletePolicy.builder().build());
        return ResponseEntity.ok(r);
    }

    @PostMapping("/empresa/{id}")
    public ResponseEntity<SoftDeleteResult> empresa(@PathVariable Long id,
                                                    @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                    @RequestParam(required = false) String reason,
                                                    @RequestParam(required = false) String deletedBy) {
        SoftDeleteResult r = service.softDeleteEmpresa(id, reason, deletedBy, mode, SoftDeletePolicy.builder().build());
        return ResponseEntity.ok(r);
    }

    @PostMapping("/ubicacion/{id}")
    public ResponseEntity<SoftDeleteResult> ubicacion(@PathVariable Long id,
                                                      @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                      @RequestParam(defaultValue = "INACTIVATE_CHILDREN") UbicacionChildPolicy policy,
                                                      @RequestParam(required = false) Long defaultUbicacionId,
                                                      @RequestParam(required = false) String reason,
                                                      @RequestParam(required = false) String deletedBy) {
        SoftDeletePolicy pol = SoftDeletePolicy.builder().ubicacionPolicy(policy).defaultUbicacionId(defaultUbicacionId).build();
        SoftDeleteResult r = service.softDeleteUbicacion(id, reason, deletedBy, mode, pol);
        return ResponseEntity.ok(r);
    }

    @PostMapping("/equipo/{id}")
    public ResponseEntity<SoftDeleteResult> equipo(@PathVariable Integer id,
                                                   @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                   @RequestParam(required = false) String reason,
                                                   @RequestParam(required = false) String deletedBy) {
        SoftDeleteResult r = service.softDeleteEquipo(id, reason, deletedBy, mode);
        return ResponseEntity.ok(r);
    }

    @PostMapping("/acta/{id}")
    public ResponseEntity<SoftDeleteResult> acta(@PathVariable Integer id,
                                                 @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                 @RequestParam(required = false) String reason,
                                                 @RequestParam(required = false) String deletedBy) {
        SoftDeleteResult r = service.softDeleteActa(id, reason, deletedBy, mode);
        return ResponseEntity.ok(r);
    }

    @PostMapping("/usuario/{id}")
    public ResponseEntity<SoftDeleteResult> usuario(@PathVariable Integer id,
                                                    @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                    @RequestParam(required = false) String reason,
                                                    @RequestParam(required = false) String deletedBy) {
        SoftDeleteResult r = service.softDeleteUsuario(id, reason, deletedBy, mode);
        return ResponseEntity.ok(r);
    }

    @PostMapping("/rol/{id}")
    public ResponseEntity<SoftDeleteResult> rol(@PathVariable Long id,
                                                @RequestParam(defaultValue = "EXECUTE") SoftDeleteMode mode,
                                                @RequestParam(defaultValue = "false") boolean cascadeUsers,
                                                @RequestParam(required = false) String reason,
                                                @RequestParam(required = false) String deletedBy) {
        SoftDeletePolicy pol = SoftDeletePolicy.builder().cascadeUsersOnRole(cascadeUsers).build();
        SoftDeleteResult r = service.softDeleteRol(id, reason, deletedBy, mode, pol);
        return ResponseEntity.ok(r);
    }
}
