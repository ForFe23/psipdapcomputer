package com.dapcomputer.inventariosapi.infraestructura.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SoftDeleteService {
    private static final String ACTIVE = "ACTIVO_INTERNAL";
    private static final String INACTIVE = "INACTIVO_INTERNAL";

    @PersistenceContext
    private EntityManager em;

    private final NamedParameterJdbcTemplate jdbc;
    private final ObjectMapper mapper;

    public SoftDeleteService(NamedParameterJdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public SoftDeleteResult softDeleteCliente(Long clienteId, String reason, String deletedBy, SoftDeleteMode mode, SoftDeletePolicy policy) {
        Objects.requireNonNull(clienteId, "clienteId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("cliente").id(clienteId).mode(mode).reason(reason).deletedBy(deletedBy);

        var empresas = jdbc.queryForList("select id from empresa where cliente_id = :clienteId", new MapSqlParameterSource("clienteId", clienteId), Long.class);
        for (Long empresaId : empresas) {
            SoftDeleteResult parcial = softDeleteEmpresa(empresaId, reason, deletedBy, mode, policy);
            parcial.getCounts().forEach((k, v) -> result.addCount(k, v));
        }

        result.addCount("persona", runUpdate("update persona set estado_interno = :inactive where cliente_id = :clienteId and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("ubicacion", runUpdate("update ubicacion set estado_interno = :inactive where cliente_id = :clienteId and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("usuarios", runUpdate("update usuarios set estadoInterno = :inactive, estatus = 'INACTIVO' where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("equipo", runUpdate("update equipo set estadoInterno = :inactive where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("mantenimiento", runUpdate("update mantenimiento set estadoInterno = :inactive where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("incidente", runUpdate("update incidente set estadoInterno = :inactive where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));
        result.addCount("perifericos", runUpdate("update perifericos set estadoInterno = :inactive where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId.intValue()), mode));

        var actas = jdbc.queryForList("select id from acta where id_cliente = :clienteId", Map.of("clienteId", clienteId), Integer.class);
        for (Integer actaId : actas) {
            SoftDeleteResult parcial = softDeleteActa(actaId, reason, deletedBy, mode);
            parcial.getCounts().forEach((k, v) -> result.addCount(k, v));
        }
        result.addCount("movimiento_equipo", runUpdate("update movimiento_equipo set estado_interno = :inactive where equipo_id in (select id from equipo where idcliente = :clienteId) or acta_id in (select id from acta where id_cliente = :clienteId)",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));

        result.addCount("cliente", runUpdate("update cliente set estadoInterno = :inactive where idcliente = :clienteId and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "clienteId", clienteId), mode));

        audit("cliente", clienteId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteEmpresa(Long empresaId, String reason, String deletedBy, SoftDeleteMode mode, SoftDeletePolicy policy) {
        Objects.requireNonNull(empresaId, "empresaId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("empresa").id(empresaId).mode(mode).reason(reason).deletedBy(deletedBy);

        var actas = jdbc.queryForList("select id from acta where empresa_id = :id", Map.of("id", empresaId), Integer.class);
        for (Integer actaId : actas) {
            SoftDeleteResult parcial = softDeleteActa(actaId, reason, deletedBy, mode);
            parcial.getCounts().forEach((k, v) -> result.addCount(k, v));
        }

        result.addCount("perifericos", runUpdate("update perifericos set estadoInterno = :inactive where equipo_id in (select id from equipo where empresa_id = :id) and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("acta_item", runUpdate("update acta_item set estadoInterno = :inactive where acta_id in (select id from acta where empresa_id = :id) and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("movimiento_equipo", runUpdate("update movimiento_equipo set estado_interno = :inactive where empresa_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("incidente", runUpdate("update incidente set estadoInterno = :inactive where equipo_id in (select id from equipo where empresa_id = :id) and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("mantenimiento", runUpdate("update mantenimiento set estadoInterno = :inactive where equipo_id in (select id from equipo where empresa_id = :id) and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("equipo", runUpdate("update equipo set estadoInterno = :inactive where empresa_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));

        result.addCount("ubicacion", runUpdate("update ubicacion set estado_interno = :inactive where empresa_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("persona", runUpdate("update persona set estado_interno = :inactive where empresa_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));
        result.addCount("usuarios", runUpdate("update usuarios set estadoInterno = :inactive, estatus = 'INACTIVO' where empresa_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));

        result.addCount("empresa", runUpdate("update empresa set estado_interno = :inactive where id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", empresaId), mode));

        audit("empresa", empresaId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteUbicacion(Long ubicacionId, String reason, String deletedBy, SoftDeleteMode mode, SoftDeletePolicy policy) {
        Objects.requireNonNull(ubicacionId, "ubicacionId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("ubicacion").id(ubicacionId).mode(mode).reason(reason).deletedBy(deletedBy);

        long equiposActivos = jdbc.queryForObject("select count(1) from equipo where ubicacion_actual_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("id", ubicacionId, "inactive", INACTIVE), Long.class);
        if (equiposActivos > 0 && policy.getUbicacionPolicy() == UbicacionChildPolicy.BLOCK && mode == SoftDeleteMode.EXECUTE) {
            throw new IllegalStateException("Ubicacion con equipos activos, politica BLOCK");
        }
        if (equiposActivos > 0 && policy.getUbicacionPolicy() == UbicacionChildPolicy.REASSIGN) {
            Long destino = Optional.ofNullable(policy.getDefaultUbicacionId())
                    .orElseThrow(() -> new IllegalArgumentException("defaultUbicacionId requerido para REASSIGN"));
            result.addCount("equipo_reassign", runUpdate("update equipo set ubicacion_actual_id = :destino where ubicacion_actual_id = :id",
                    Map.of("destino", destino, "id", ubicacionId), mode));
        } else if (equiposActivos > 0 && policy.getUbicacionPolicy() == UbicacionChildPolicy.INACTIVATE_CHILDREN) {
            SoftDeleteResult equipos = softDeleteEquiposByUbicacion(ubicacionId, reason, deletedBy, mode);
            equipos.getCounts().forEach((k, v) -> result.addCount(k, v));
        }

        result.addCount("movimiento_equipo", runUpdate("update movimiento_equipo set estado_interno = :inactive where (ubicacion_origen_id = :id or ubicacion_destino_id = :id) and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", ubicacionId), mode));
        result.addCount("acta", runUpdate("update acta set estado_interno = :inactive where ubicacion_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", ubicacionId), mode));

        result.addCount("ubicacion", runUpdate("update ubicacion set estado_interno = :inactive where id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", ubicacionId), mode));

        audit("ubicacion", ubicacionId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteEquipo(Integer equipoId, String reason, String deletedBy, SoftDeleteMode mode) {
        Objects.requireNonNull(equipoId, "equipoId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("equipo").id(equipoId).mode(mode).reason(reason).deletedBy(deletedBy);

        result.addCount("perifericos", runUpdate("update perifericos set estadoInterno = :inactive where equipo_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));
        result.addCount("acta_item", runUpdate("update acta_item set estadoInterno = :inactive where equipo_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));
        result.addCount("movimiento_equipo", runUpdate("update movimiento_equipo set estado_interno = :inactive where equipo_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));
        result.addCount("incidente", runUpdate("update incidente set estadoInterno = :inactive where equipo_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));
        result.addCount("mantenimiento", runUpdate("update mantenimiento set estadoInterno = :inactive where equipo_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));
        result.addCount("equipo", runUpdate("update equipo set estadoInterno = :inactive where id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", equipoId), mode));

        audit("equipo", equipoId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteActa(Integer actaId, String reason, String deletedBy, SoftDeleteMode mode) {
        Objects.requireNonNull(actaId, "actaId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("acta").id(actaId).mode(mode).reason(reason).deletedBy(deletedBy);

        result.addCount("acta_adjunto", runUpdate("update acta_adjunto set estadoInterno = :inactive where acta_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", actaId), mode));
        result.addCount("acta_item", runUpdate("update acta_item set estadoInterno = :inactive where acta_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", actaId), mode));
        result.addCount("movimiento_equipo", runUpdate("update movimiento_equipo set estado_interno = :inactive where acta_id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", actaId), mode));
        result.addCount("acta", runUpdate("update acta set estado_interno = :inactive where id = :id and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", actaId), mode));

        audit("acta", actaId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteUsuario(Integer usuarioId, String reason, String deletedBy, SoftDeleteMode mode) {
        Objects.requireNonNull(usuarioId, "usuarioId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("usuario").id(usuarioId).mode(mode).reason(reason).deletedBy(deletedBy);

        result.addCount("equipo_unassign", runUpdate("update equipo set asignado_a_id = null where asignado_a_id = :id",
                Map.of("id", usuarioId), mode));
        result.addCount("usuarios", runUpdate("update usuarios set estadoInterno = :inactive, estatus = 'INACTIVO' where idusuario = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "id", usuarioId), mode));
        audit("usuario", usuarioId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    public SoftDeleteResult softDeleteRol(Long rolId, String reason, String deletedBy, SoftDeleteMode mode, SoftDeletePolicy policy) {
        Objects.requireNonNull(rolId, "rolId requerido");
        SoftDeleteResult.Builder result = SoftDeleteResult.builder().entity("rol").id(rolId).mode(mode).reason(reason).deletedBy(deletedBy);

        if (policy != null && policy.isCascadeUsersOnRole()) {
            result.addCount("usuarios", runUpdate("update usuarios set estadoInterno = :inactive, estatus = 'INACTIVO' where rol_id = :rolId and coalesce(upper(estadoInterno),'') <> :inactive",
                    Map.of("inactive", INACTIVE, "rolId", rolId), mode));
        }
        result.addCount("rol", runUpdate("update rol set estado_interno = :inactive where id = :rolId and coalesce(upper(estado_interno),'') <> :inactive",
                Map.of("inactive", INACTIVE, "rolId", rolId), mode));
        audit("rol", rolId, reason, deletedBy, mode, result.getCounts());
        return result.executed(mode == SoftDeleteMode.EXECUTE).build();
    }

    private SoftDeleteResult softDeleteEquiposByUbicacion(Long ubicacionId, String reason, String deletedBy, SoftDeleteMode mode) {
        List<Integer> equipos = jdbc.queryForList("select id from equipo where ubicacion_actual_id = :id and coalesce(upper(estadoInterno),'') <> :inactive",
                Map.of("id", ubicacionId, "inactive", INACTIVE), Integer.class);
        SoftDeleteResult.Builder aggregated = SoftDeleteResult.builder().entity("equipo_by_ubicacion").id(ubicacionId).mode(mode).reason(reason).deletedBy(deletedBy);
        for (Integer eqId : equipos) {
            SoftDeleteResult parcial = softDeleteEquipo(eqId, reason, deletedBy, mode);
            parcial.getCounts().forEach((k, v) -> aggregated.addCount(k, v));
        }
        return aggregated.build();
    }

    private long runUpdate(String sql, Map<String, ?> params, SoftDeleteMode mode) {
        if (mode == SoftDeleteMode.DRY_RUN) {
            int whereIdx = sql.toLowerCase().indexOf("where");
            int setIdx = sql.toLowerCase().indexOf("set");
            if (whereIdx < 0 || setIdx < 0) {
                return 0;
            }
            String tablePart = sql.substring("update".length(), setIdx).trim();
            String wherePart = sql.substring(whereIdx);
            String countSql = "select count(1) from " + tablePart + " " + wherePart;
            return jdbc.queryForObject(countSql, params, Long.class);
        }
        return jdbc.update(sql, params);
    }

    private void audit(String target, Object id, String reason, String deletedBy, SoftDeleteMode mode, Map<String, Long> counts) {
        try {
            Map<String, Object> row = new HashMap<>();
            row.put("target_type", target);
            row.put("target_id", id == null ? null : id.toString());
            row.put("mode", mode.name());
            row.put("deleted_by", deletedBy);
            row.put("delete_reason", reason);
            row.put("counts_json", mapper.writeValueAsString(counts));
            row.put("created_at", Instant.now());
            jdbc.update("insert into audit_soft_delete(target_type, target_id, mode, deleted_by, delete_reason, counts_json, created_at) values(:target_type,:target_id,:mode,:deleted_by,:delete_reason,:counts_json,:created_at)",
                    new MapSqlParameterSource(row));
        } catch (JsonProcessingException e) {
        }
    }
}
