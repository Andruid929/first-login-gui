package com.andrewjones.logingui.db;

import static com.andrewjones.logingui.db.Secrets.PSQL_PASSWORD;
import static com.andrewjones.logingui.db.Secrets.PSQL_USER;

import org.postgresql.ds.PGSimpleDataSource;

public final class DB {

    public static final PGSimpleDataSource DATA_SOURCE;

    static {
        DATA_SOURCE = new PGSimpleDataSource();

        DATA_SOURCE.setDatabaseName(PSQL_USER);
        DATA_SOURCE.setUser(PSQL_USER);
        DATA_SOURCE.setPortNumbers(new int[]{12345});
        DATA_SOURCE.setServerNames(new String[]{"localhost"});
        DATA_SOURCE.setPassword(PSQL_PASSWORD);
    }

}
