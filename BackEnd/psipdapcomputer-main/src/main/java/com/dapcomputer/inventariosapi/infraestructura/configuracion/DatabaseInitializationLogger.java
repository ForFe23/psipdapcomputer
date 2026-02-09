package com.dapcomputer.inventariosapi.infraestructura.configuracion;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializationLogger {
    private final DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void logDatabaseStatus() {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            boolean valid = connection.isValid(2);
            if (valid) {
                log.info("Base de datos recreada y disponible: {}", url);
            } else {
                log.error("La base de datos no respondió al chequeo de disponibilidad inicial: {}", url);
            }
        } catch (SQLException ex) {
            log.error("Error verificando el estado de la base de datos recreada", ex);
        }
    }
}

