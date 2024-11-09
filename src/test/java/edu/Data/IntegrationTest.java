package edu.Data;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

@Testcontainers
public abstract class IntegrationTest {
        private static final String DATABASE_LOGIN = System.getenv("NEW_DATABASE_LOGIN");
        private static final String DATABASE_PASSWORD = System.getenv("NEW_DATABASE_PASS");

    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("table")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        try (Connection connection = DriverManager.getConnection(
                c.getJdbcUrl(),
                c.getUsername(),
                c.getPassword()
        )
        ) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Path changelogPath = new File("").toPath()
                    .toAbsolutePath()
                    .resolve("migrations");
            Logger.getAnonymousLogger().info("Running migrations from " + changelogPath);

            var liquibase =
                    new Liquibase(
                            "master.xml",
                            new DirectoryResourceAccessor(changelogPath),
                            database
                    );

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException(e); // stupid, but wroks
        }
    }

}