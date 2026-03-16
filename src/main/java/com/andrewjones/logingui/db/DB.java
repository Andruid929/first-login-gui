package com.andrewjones.logingui.db;

import static com.andrewjones.logingui.db.Secrets.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public final class DB {

    private static final DataSource DATA_SOURCE;

    static {
        HikariConfig theConfig = new HikariConfig();
        theConfig.setJdbcUrl(PSQL_URL);
        theConfig.setUsername(PSQL_USER);
        theConfig.setPassword(PSQL_PASSWORD);
        theConfig.setMaximumPoolSize(10);
        theConfig.setMinimumIdle(2);
        theConfig.setIdleTimeout(600_000);
        theConfig.setConnectionTimeout(30_000);
        theConfig.setMaxLifetime(1_800_000);

        DATA_SOURCE = new HikariDataSource(theConfig);
    }

    public static Connection getDbConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
