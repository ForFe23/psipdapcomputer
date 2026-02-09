package com.dapcomputer.inventariosapi.seguridad;

public final class RoleMapper {
    private RoleMapper() {}

    public static String canonical(String codigo) {
        if (codigo == null) return "";
        String r = codigo.trim().toUpperCase();
        return switch (r) {
            case "ADMIN_GLOBAL" -> "ADMIN_GLOBAL";
            case "TECNICO_GLOBAL" -> "TECNICO_GLOBAL";
            case "TRGRTNRS" -> "CLIENTE_ADMIN";
            case "CMPRSGRS" -> "CLIENTE_VISOR";
            case "BLTRCPLS" -> "TECNICO_CLIENTE";
            default -> r;
        };
    }
}

