package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.AuthRequest;
import com.dapcomputer.inventariosapi.presentacion.dto.AuthResponse;
import com.dapcomputer.inventariosapi.seguridad.RoleMapper;
import com.dapcomputer.inventariosapi.seguridad.TokenPayload;
import com.dapcomputer.inventariosapi.seguridad.TokenService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {
    private final UsuarioRepositorio usuarios;
    private final TokenService tokenService;

    public AuthControlador(UsuarioRepositorio usuarios, TokenService tokenService) {
        this.usuarios = usuarios;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest solicitud) {
        Usuario user = usuarios.buscarPorCorreo(solicitud.correo().trim().toUpperCase())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        String passHash = hash(solicitud.password());
        String stored = user.solfrnrf() == null ? "" : user.solfrnrf().trim().toUpperCase();
        if (!stored.equals(passHash) && !stored.equals(solicitud.password().trim().toUpperCase())) {
            return ResponseEntity.status(401).build();
        }

        String rolCanon = RoleMapper.canonical(user.rol() != null ? user.rol().codigo() : "");

        TokenPayload payload = new TokenPayload(
                user.id(),
                user.correo(),
                rolCanon,
                user.idCliente(),
                user.empresaId(),
                null,
                List.of("ROLE_" + rolCanon));
        String token = tokenService.generar(payload);

        return ResponseEntity.ok(new AuthResponse(
                token,
                rolCanon,
                user.idCliente(),
                user.empresaId(),
                user.id(),
                user.correo()));
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest).toUpperCase();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo calcular hash", e);
        }
    }
}

