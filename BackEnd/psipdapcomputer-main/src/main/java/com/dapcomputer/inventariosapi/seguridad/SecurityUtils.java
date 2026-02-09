package com.dapcomputer.inventariosapi.seguridad;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static Optional<TokenPayload> getPayload() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();
        Object details = auth.getDetails();
        if (details instanceof TokenPayload payload) {
            return Optional.of(payload);
        }
        return Optional.empty();
    }
}

