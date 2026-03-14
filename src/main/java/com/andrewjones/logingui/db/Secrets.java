package com.andrewjones.logingui.db;

public final class Secrets {

    public static final String PSQL_USER = "postgres";

    public static final String PSQL_URL = "jdbc:postgresql://localhost:12345/".concat(PSQL_USER);

    public static final String PSQL_PASSWORD = System.getenv("PSQL_PASS");

}
