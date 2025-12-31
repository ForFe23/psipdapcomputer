# Inventarios TI - Actas y Trazabilidad

## Objetivo
Prototipo web para gestionar el inventario TI y las actas de entrega de un cliente corporativo, asegurando trazabilidad completa por equipo y usuario.

## Qué cubre hoy
- Registro y actualización de equipos TI (serie, tipo, marca/modelo, estado, ubicación, usuario asignado).
- Gestión de actas con estado (registrada, emitida, cerrada, anulada) e ítems asociados.
- Adjuntos por acta (metadatos de archivos/firma).
- Movimientos de equipo (entrega, traslado, retiro, anulación) con ubicaciones origen/destino y usuarios involucrados.
- Incidentes, clientes y usuarios.

## Esquema de datos clave
- `acta`, `acta_item`, `acta_adjunto`
- `equipo`, `perifericos`
- `movimiento_equipo` (historial y trazabilidad)
- `usuarios`, `cliente`, `incidente`, `acta_seq`

## Configuración
- Base de datos: PostgreSQL remoto `186.71.192.250:5432/psipdapcomputer`, esquema `public`.
- DDL: `spring.jpa.hibernate.ddl-auto=update` (actualiza tablas sin dropear el esquema).

## Próximos pasos sugeridos
- Endpoints/servicios para crear y listar movimientos y adjuntos.
- Índices para reportes por fecha/estado/ubicación/usuario/serie.
- Normalizar catálogos de estado/tipo/ubicación si se requiere mayor consistencia.
