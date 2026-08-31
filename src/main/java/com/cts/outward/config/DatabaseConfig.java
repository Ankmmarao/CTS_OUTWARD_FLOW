
package com.cts.outward.config;

public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static final String DRIVER =
            "org.postgresql.Driver";

    public static final String URL =
            "jdbc:postgresql://localhost:5432/superDB";

    public static final String USERNAME =
            "postgres";

    public static final String PASSWORD =
            "postgres";
}

