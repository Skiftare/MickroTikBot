package edu.Data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
        jdbcTemplate.update("DELETE FROM stellar_transactions");
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

    @Test
    public void testUserTableConstraints() {
        // Тест на уникальность tg_user_id
        jdbcTemplate.update(
            "INSERT INTO users (tg_user_id, name) VALUES (?, ?)",
            12345L, "Test User"
        );
        
        assertThrows(Exception.class, () -> 
            jdbcTemplate.update(
                "INSERT INTO users (tg_user_id, name) VALUES (?, ?)",
                12345L, "Another User"
            )
        );
    }

    @Test
    public void testBalanceOperations() {
        // Вставка пользователя с балансом
        jdbcTemplate.update(
            "INSERT INTO users (tg_user_id, name, balance) VALUES (?, ?, ?)",
            12345L, "Test User", new BigDecimal("100.0000000")
        );

        // Проверка обновления баланса
        jdbcTemplate.update(
            "UPDATE users SET balance = balance + ? WHERE tg_user_id = ?",
            new BigDecimal("50.0000000"), 12345L
        );

        BigDecimal balance = jdbcTemplate.queryForObject(
            "SELECT balance FROM users WHERE tg_user_id = ?",
            BigDecimal.class,
            12345L
        );

        assertEquals(new BigDecimal("150.0000000"), balance);
    }

    @Test
    public void testPaymentPendingStatus() {
        // Вставка пользователя
        jdbcTemplate.update(
            "INSERT INTO users (tg_user_id, name, is_payment_pending) VALUES (?, ?, ?)",
            12345L, "Test User", false
        );

        // Обновление статуса платежа
        jdbcTemplate.update(
            "UPDATE users SET is_payment_pending = true WHERE tg_user_id = ?",
            12345L
        );

        Boolean isPending = jdbcTemplate.queryForObject(
            "SELECT is_payment_pending FROM users WHERE tg_user_id = ?",
            Boolean.class,
            12345L
        );

        assertTrue(isPending);
    }

    @Test
    public void testStellarTransactions() {
        // Тест вставки транзакции
        String transactionHash = "test_hash_123";
        String memo = "test_memo";
        BigDecimal amount = new BigDecimal("100.0000000");
        String sourceAccount = "test_account";

        jdbcTemplate.update(
            "INSERT INTO stellar_transactions (transaction_hash, memo, amount, source_account) VALUES (?, ?, ?, ?)",
            transactionHash, memo, amount, sourceAccount
        );

        Map<String, Object> transaction = jdbcTemplate.queryForMap(
            "SELECT * FROM stellar_transactions WHERE transaction_hash = ?",
            transactionHash
        );

        assertEquals(transactionHash, transaction.get("transaction_hash"));
        assertEquals(memo, transaction.get("memo"));
        assertEquals(amount, transaction.get("amount"));
        assertEquals(sourceAccount, transaction.get("source_account"));
    }

}