CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public;


CREATE TABLE IF NOT EXISTS cliente (
    idcliente BIGSERIAL PRIMARY KEY,
    nombrecliente VARCHAR(50),
    contrasenacliente VARCHAR(50),
    email VARCHAR(50),
    licenciacliente VARCHAR(50),
    calle VARCHAR(50),
    ciudad VARCHAR(50),
    fechalicencia DATE,
    estado_interno VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS rol (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(idcliente),
    nombre VARCHAR(150) NOT NULL,
    estado VARCHAR(30),
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_empresa_cliente ON empresa(cliente_id);
CREATE INDEX IF NOT EXISTS idx_empresa_nombre ON empresa(nombre);

CREATE TABLE IF NOT EXISTS ubicacion (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    estado VARCHAR(30),
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_ubicacion_empresa ON ubicacion(empresa_id);
CREATE INDEX IF NOT EXISTS idx_ubicacion_estado ON ubicacion(estado);


CREATE TABLE IF NOT EXISTS usuarios (
    idusuario SERIAL PRIMARY KEY,
    idcliente BIGINT NOT NULL REFERENCES cliente(idcliente),
    empresa_id BIGINT REFERENCES empresa(id),
    rol_id BIGINT REFERENCES rol(id),
    cedulausuario VARCHAR(15),
    apellidosusuario VARCHAR(50),
    nombresusuario VARCHAR(50),
    solfrnrf VARCHAR(255),
    correousuario VARCHAR(180),
    telefonousuario VARCHAR(50),
    estatususuario VARCHAR(50),
    fregistrousuario TIMESTAMP,
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_usuarios_cliente ON usuarios(idcliente);
CREATE INDEX IF NOT EXISTS idx_usuarios_empresa ON usuarios(empresa_id);
CREATE INDEX IF NOT EXISTS idx_usuarios_rol ON usuarios(rol_id);


CREATE TABLE IF NOT EXISTS equipo (
    id SERIAL PRIMARY KEY,
    serieequipo VARCHAR(50) NOT NULL UNIQUE,
    idcliente BIGINT NOT NULL REFERENCES cliente(idcliente),
    empresa_id BIGINT REFERENCES empresa(id),
    ubicacion_actual_id BIGINT REFERENCES ubicacion(id),
    asignado_a_id INTEGER REFERENCES usuarios(idusuario),
    marcaequipo VARCHAR(50),
    modeloequipo VARCHAR(50),
    tipoequipo VARCHAR(50),
    soequipo VARCHAR(50),
    procesadorequipo VARCHAR(255),
    memoriaequipo VARCHAR(50),
    hddequipo VARCHAR(50),
    fcompraequipo TIMESTAMP,
    statusequipo VARCHAR(50),
    estado_interno VARCHAR(50),
    ipequipo VARCHAR(50),
    ubicacionusuario VARCHAR(50),
    depusuario VARCHAR(50),
    nombreusuario VARCHAR(50),
    nombreproveedor VARCHAR(50),
    direccionproveedor VARCHAR(50),
    telefonoproveedor VARCHAR(50),
    contactoproveedor VARCHAR(50),
    cliente VARCHAR(50),
    activoequipo VARCHAR(50),
    officeequipo VARCHAR(50),
    costoequipo VARCHAR(50),
    facturaequipo VARCHAR(50),
    notasequipo VARCHAR(500),
    ciudadequipo VARCHAR(50),
    nombreequipo VARCHAR(50),
    alias VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_equipo_serie ON equipo(serieequipo);
CREATE INDEX IF NOT EXISTS idx_equipo_empresa ON equipo(empresa_id);
CREATE INDEX IF NOT EXISTS idx_equipo_ubicacion ON equipo(ubicacion_actual_id);
CREATE INDEX IF NOT EXISTS idx_equipo_asignado ON equipo(asignado_a_id);


CREATE TABLE IF NOT EXISTS perifericos (
    id SERIAL PRIMARY KEY,
    equipo_id INTEGER NOT NULL REFERENCES equipo(id) ON DELETE CASCADE,
    serieequipo VARCHAR(50),
    seriemonitor VARCHAR(50),
    activomonitor VARCHAR(50),
    marcamonitor VARCHAR(50),
    modelomonitor VARCHAR(50),
    observacionmonitor VARCHAR(100),
    serieteclado VARCHAR(50),
    activoteclado VARCHAR(50),
    marcateclado VARCHAR(50),
    modeloteclado VARCHAR(50),
    observacionteclado VARCHAR(100),
    seriemouse VARCHAR(50),
    activomouse VARCHAR(50),
    marcamouse VARCHAR(50),
    modelomouse VARCHAR(50),
    observacionmouse VARCHAR(100),
    serietelefono VARCHAR(50),
    activotelefono VARCHAR(50),
    marcatelefono VARCHAR(50),
    modelotelefono VARCHAR(50),
    observaciontelefono VARCHAR(100),
    clienteperifericos VARCHAR(500),
    idcliente INTEGER REFERENCES cliente(idcliente),
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_perifericos_equipo ON perifericos(equipo_id);


CREATE TABLE IF NOT EXISTS mantenimiento (
    id SERIAL PRIMARY KEY,
    equipo_id BIGINT NOT NULL REFERENCES equipo(id) ON DELETE CASCADE,
    serieequipo VARCHAR(50),
    idcliente BIGINT NOT NULL REFERENCES cliente(idcliente),
    fechaprogramada TIMESTAMP NOT NULL,
    frecuenciadias INT,
    descripcion VARCHAR(255),
    estado VARCHAR(30),
    creadoen TIMESTAMP,
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_mantenimiento_equipo ON mantenimiento(equipo_id);


CREATE TABLE IF NOT EXISTS acta (
    id SERIAL PRIMARY KEY,
    acta_code VARCHAR(40) UNIQUE,
    estado VARCHAR(30) NOT NULL,
    estado_interno VARCHAR(50),
    idcliente INTEGER REFERENCES cliente(idcliente),
    id_equipo INTEGER REFERENCES equipo(id),
    fecha_acta DATE,
    tema VARCHAR(255),
    entregado_por VARCHAR(120),
    recibido_por VARCHAR(120),
    cargo_entrega VARCHAR(120),
    cargo_recibe VARCHAR(120),
    depusuario VARCHAR(120),
    ciudadequipo VARCHAR(120),
    ubicacionusuario VARCHAR(120),
    obs_generales TEXT,
    equipo_tipo VARCHAR(120),
    equipo_serie VARCHAR(120),
    equipo_modelo VARCHAR(120),
    created_at TIMESTAMP,
    created_by VARCHAR(120),
    empresa_id BIGINT REFERENCES empresa(id),
    ubicacion_id BIGINT REFERENCES ubicacion(id),
    tipo VARCHAR(30),
    justificacion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    firmado_entrego_id INTEGER REFERENCES usuarios(idusuario),
    firmado_recibio_id INTEGER REFERENCES usuarios(idusuario),
    movimiento_id INTEGER
);
CREATE INDEX IF NOT EXISTS idx_acta_empresa ON acta(empresa_id);
CREATE INDEX IF NOT EXISTS idx_acta_ubicacion ON acta(ubicacion_id);
CREATE INDEX IF NOT EXISTS idx_acta_equipo ON acta(id_equipo);

CREATE TABLE IF NOT EXISTS acta_item (
    id SERIAL PRIMARY KEY,
    acta_id INTEGER NOT NULL REFERENCES acta(id) ON DELETE CASCADE,
    equipo_id INTEGER REFERENCES equipo(id),
    equipo_serie VARCHAR(120),
    item_num INTEGER,
    tipo VARCHAR(120),
    serie VARCHAR(120),
    modelo VARCHAR(120),
    observacion VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_acta_item_acta ON acta_item(acta_id);
CREATE INDEX IF NOT EXISTS idx_acta_item_equipo ON acta_item(equipo_id);

CREATE TABLE IF NOT EXISTS acta_adjunto (
    id SERIAL PRIMARY KEY,
    acta_id INTEGER NOT NULL REFERENCES acta(id) ON DELETE CASCADE,
    nombre VARCHAR(255),
    url VARCHAR(500),
    tipo VARCHAR(50),
    nombre_archivo VARCHAR(255),
    content_type VARCHAR(150),
    tamano BIGINT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_acta_adjunto_acta ON acta_adjunto(acta_id);


CREATE TABLE IF NOT EXISTS movimiento_equipo (
    id SERIAL PRIMARY KEY,
    acta_id INTEGER REFERENCES acta(id),
    equipo_id INTEGER NOT NULL REFERENCES equipo(id),
    equipo_serie VARCHAR(50),
    empresa_id BIGINT REFERENCES empresa(id),
    tipo_movimiento VARCHAR(30) NOT NULL,
    estado_interno VARCHAR(50),
    ubicacion_origen VARCHAR(120),
    ubicacion_destino VARCHAR(120),
    usuario_origen_id INTEGER,
    usuario_destino_id INTEGER,
    fecha_movimiento TIMESTAMP,
    observacion VARCHAR(255),
    ubicacion_origen_id BIGINT REFERENCES ubicacion(id),
    ubicacion_destino_id BIGINT REFERENCES ubicacion(id),
    persona_origen_id INTEGER REFERENCES usuarios(idusuario),
    persona_destino_id INTEGER REFERENCES usuarios(idusuario),
    ejecutado_por_id INTEGER REFERENCES usuarios(idusuario)
);
CREATE INDEX IF NOT EXISTS idx_movimiento_equipo ON movimiento_equipo(equipo_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_acta ON movimiento_equipo(acta_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_empresa ON movimiento_equipo(empresa_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_ubicacion_origen ON movimiento_equipo(ubicacion_origen_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_ubicacion_destino ON movimiento_equipo(ubicacion_destino_id);


CREATE TABLE IF NOT EXISTS incidente (
    id SERIAL PRIMARY KEY,
    equipo_id INTEGER REFERENCES equipo(id),
    idusuario INTEGER REFERENCES usuarios(idusuario),
    idcliente BIGINT REFERENCES cliente(idcliente),
    fechaincidente TIMESTAMP,
    detalleincidente VARCHAR(255),
    costoincidente VARCHAR(50),
    tecnicoincidente VARCHAR(50),
    responsable VARCHAR(100),
    estado_interno VARCHAR(50)
);
CREATE INDEX IF NOT EXISTS idx_incidente_equipo ON incidente(equipo_id);
CREATE INDEX IF NOT EXISTS idx_incidente_cliente ON incidente(idcliente);
CREATE INDEX IF NOT EXISTS idx_incidente_usuario ON incidente(idusuario);


ALTER TABLE IF EXISTS acta DROP CONSTRAINT IF EXISTS fk_acta_movimiento;
ALTER TABLE IF EXISTS movimiento_equipo DROP CONSTRAINT IF EXISTS fk_movimiento_equipo;
ALTER TABLE IF EXISTS acta_item DROP CONSTRAINT IF EXISTS fk_acta_item_equipo;
