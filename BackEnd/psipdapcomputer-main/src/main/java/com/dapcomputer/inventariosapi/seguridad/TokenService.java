package com.dapcomputer.inventariosapi.seguridad;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private static final String HMAC_ALG = "HmacSHA256";
    private static final String SECRET = "psipdap-secret-2026-change-me";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String generar(TokenPayload payload) {
        try {
            Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
            String h = base64Url(MAPPER.writeValueAsBytes(header));

            Map<String, Object> body = new HashMap<>();
            body.put("sub", payload.correo());
            body.put("uid", payload.userId());
            body.put("rol", payload.rol());
            body.put("clienteId", payload.clienteId());
            body.put("empresaId", payload.empresaId());
            body.put("exp", payload.exp() != null ? payload.exp().getEpochSecond() : Instant.now().plus(12, ChronoUnit.HOURS).getEpochSecond());
            body.put("auth", payload.authorities() == null ? List.of() : payload.authorities());
            String b = base64Url(MAPPER.writeValueAsBytes(body));

            String content = h + "." + b;
            String sig = firmar(content);
            return content + "." + sig;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar token", e);
        }
    }

    public TokenPayload validar(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException("Token inválido");
            String content = parts[0] + "." + parts[1];
            String firma = parts[2];
            if (!firma.equals(firmar(content))) throw new IllegalArgumentException("Firma inválida");
            Map<String, Object> payload = MAPPER.readValue(
                    Base64.getUrlDecoder().decode(parts[1]),
                    new TypeReference<Map<String, Object>>() {});
            Instant exp = Instant.ofEpochSecond(((Number) payload.get("exp")).longValue());
            if (Instant.now().isAfter(exp)) throw new IllegalArgumentException("Token expirado");

            Integer uid = payload.get("uid") == null ? null : Integer.valueOf(payload.get("uid").toString());
            Long clienteId = payload.get("clienteId") == null ? null : Long.valueOf(payload.get("clienteId").toString());
            Long empresaId = payload.get("empresaId") == null ? null : Long.valueOf(payload.get("empresaId").toString());
            String rol = payload.getOrDefault("rol", "").toString();
            String correo = payload.getOrDefault("sub", "").toString();
            Object authObj = payload.get("auth");
            List<String> auth = authObj instanceof List<?> list
                    ? list.stream().map(Object::toString).toList()
                    : List.of();
            return new TokenPayload(uid, correo, rol, clienteId, empresaId, exp, auth);
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o expirado", e);
        }
    }

    private String firmar(String content) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALG);
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
        byte[] sig = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return base64Url(sig);
    }

    private String base64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}

