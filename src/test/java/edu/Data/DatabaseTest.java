/*package edu.Data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest extends IntegrationTest {

    private static DataSource source;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setUpDataSource() {
        source = DataSourceBuilder.create()
                .url(POSTGRES.getJdbcUrl())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .build();
        jdbcTemplate = new JdbcTemplate(source);

    }

    @BeforeEach
    public void setUp() {
        // Очистка таблицы перед каждым тестом для изоляции
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    public void testThatDatabaseIsEmptyInitially() {
        List<Long> results = jdbcTemplate.query("SELECT tg_user_id FROM users", (ResultSet rs, int rowNum) -> rs.getLong("tg_user_id"));
        assertTrue(results.isEmpty());
    }


    @Test
    public void testThatInsertsDataCorrectly() {
        jdbcTemplate.update(
                "INSERT INTO users (tg_user_id, phone, name, user_last_visited, vpn_profile, is_vpn_profile_alive, expired_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                12345L, "1234567890", "Test User", OffsetDateTime.now().minus(1, ChronoUnit.DAYS), "vpn_profile_1", true, OffsetDateTime.now().plus(1, ChronoUnit.MONTHS)
        );

        List<Long> results = jdbcTemplate.query("SELECT tg_user_id FROM users WHERE tg_user_id = 12345", (ResultSet rs, int rowNum) -> rs.getLong("tg_user_id"));
        assertEquals(1, results.size());
        assertEquals(Long.valueOf(12345), results.get(0));
    }


}*/