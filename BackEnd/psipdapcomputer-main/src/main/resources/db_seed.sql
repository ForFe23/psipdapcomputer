BEGIN;


INSERT INTO cliente (nombrecliente, contrasenacliente, email, licenciacliente, calle, ciudad, fechalicencia, estado_interno)
VALUES ('Cliente Demo', 'demo123', 'demo@example.com', 'LIC-DEMO', 'Calle 1', 'Quito', CURRENT_DATE, 'ACTIVO_INTERNAL');


INSERT INTO empresa (cliente_id, nombre, estado, estado_interno)
VALUES (1, 'Empresa Demo', 'ACTIVA', 'ACTIVO_INTERNAL');


INSERT INTO ubicacion (empresa_id, nombre, direccion, estado, estado_interno)
VALUES (1, 'Bodega Central', 'Av. Principal 123', 'ACTIVA', 'ACTIVO_INTERNAL');


INSERT INTO rol (codigo, nombre) VALUES ('ADMIN', 'Administrador');
INSERT INTO rol (codigo, nombre) VALUES ('USER', 'Usuario');


INSERT INTO usuarios (idcliente, empresa_id, rol_id, cedulausuario, apellidosusuario, nombresusuario, correousuario, telefonousuario, estatususuario, fregistrousuario, estado_interno)
VALUES (1, 1, 1, '0102030405', 'Admin', 'Sistema', 'admin@example.com', '0990000000', 'ACTIVO', CURRENT_TIMESTAMP, 'ACTIVO_INTERNAL');


INSERT INTO equipo (serieequipo, idcliente, empresa_id, ubicacion_actual_id, asignado_a_id, marcaequipo, modeloequipo, tipoequipo, statusequipo, estado_interno, alias)
VALUES ('SERIE-DEMO-001', 1, 1, 1, 1, 'Lenovo', 'ThinkPad', 'LAPTOP', 'OPERATIVO', 'ACTIVO_INTERNAL', 'Equipo Demo');


INSERT INTO perifericos (equipo_id, serieequipo, seriemonitor, marcamonitor, modelomonitor, estado_interno)
VALUES (1, 'SERIE-DEMO-001', 'MON-001', 'Dell', 'P2419H', 'ACTIVO_INTERNAL');


INSERT INTO mantenimiento (equipo_id, serieequipo, idcliente, fechaprogramada, frecuenciadias, descripcion, estado, creadoen, estado_interno)
VALUES (1, 'SERIE-DEMO-001', 1, CURRENT_TIMESTAMP, 90, 'Mantenimiento inicial', 'PROGRAMADO', CURRENT_TIMESTAMP, 'ACTIVO_INTERNAL');


INSERT INTO acta (acta_code, estado, idcliente, id_equipo, fecha_acta, tema, entregado_por, recibido_por, empresa_id, ubicacion_id, fecha_creacion, estado_interno)
VALUES ('ACT-001', 'BORRADOR', 1, 1, CURRENT_DATE, 'Entrega inicial', 'Admin', 'Usuario', 1, 1, CURRENT_TIMESTAMP, 'ACTIVO_INTERNAL');


INSERT INTO movimiento_equipo (acta_id, equipo_id, equipo_serie, empresa_id, tipo_movimiento, fecha_movimiento, estado_interno, ubicacion_origen, ubicacion_destino)
VALUES (1, 1, 'SERIE-DEMO-001', 1, 'TRASLADO', CURRENT_TIMESTAMP, 'ACTIVO_INTERNAL', 'Bodega Central', 'Escritorio');


INSERT INTO acta_item (acta_id, equipo_id, equipo_serie, item_num, tipo, serie, modelo, observacion)
VALUES (1, 1, 'SERIE-DEMO-001', 1, 'CPU', 'SERIE-DEMO-001', 'ThinkPad', 'Equipo principal');


INSERT INTO acta_adjunto (acta_id, nombre, url, tipo, nombre_archivo, content_type, tamano)
VALUES (1, 'Adjunto demo', 'https://example.com/adjunto.pdf', 'PDF', 'adjunto.pdf', 'application/pdf', 1024);


INSERT INTO incidente (equipo_id, idusuario, idcliente, fechaincidente, detalleincidente, estado_interno)
VALUES (1, 1, 1, CURRENT_TIMESTAMP, 'Sin incidentes iniciales', 'ACTIVO_INTERNAL');

COMMIT;
