BEGIN;
TRUNCATE TABLE
    acta_adjunto,
    acta_item,
    movimiento_equipo,
    acta,
    mantenimiento,
    perifericos,
    incidente,
    equipo,
    usuarios,
    rol,
    ubicacion,
    empresa,
    cliente
RESTART IDENTITY CASCADE;
COMMIT;
