package edu.Data.formatters;

import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import edu.EncryptionUtil;
import edu.models.UserProfileStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.junitpioneer.jupiter.WritesEnvironmentVariable;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.math.BigDecimal;
import java.sql.Date;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SystemStubsExtension.class)
public class UserProfileFormatterTest {
    @SystemStub
    private EnvironmentVariables environment = new EnvironmentVariables("DB_ENCRYPTION_KEY", "c2VjdXJlU2VjcmV0S2V5Mg==");

    @Test
    public void testThatFormatHandlesFullProfileWithVpnDetails() throws Exception {

        ClientTransfer client = new ClientTransfer(
                123L,
                12345L,
                "+1234567890",
                "TestUser",
                new Date(12345L),
                EncryptionUtil.encrypt("Test profile"),
                Boolean.TRUE,
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24),
                false,
                "pay_key",
                new BigDecimal("100.00")
        );

        UserInfo userInfo = new UserInfo(client, UserProfileStatus.ACTIVE_VPN);

        UserProfileFormatter formatter = new UserProfileFormatter();

        String result = formatter.format(userInfo);


        assertTrue(result.contains("📋 Ваш профиль"));
        assertTrue(result.contains("🆔 ID: 12345"));
        assertTrue(result.contains("📱 Телефон: +1234567890"));
        assertTrue(result.contains("💰 Баланс: 100.00"));
        assertTrue(result.contains("🔐 VPN профиль:"));
        assertTrue(result.contains("✅ Активен"));
        assertTrue(result.contains("⏰ Действует до:"));
    }


    @Test
    public void testThatFormatReturnsProfileNotFoundWhenUserInfoIsNull() {
        UserProfileFormatter formatter = new UserProfileFormatter();
        String result = formatter.format(null);
        assertEquals("Профиль не найден", result);
    }

    @Test
    public void testThatFormatReturnsProfileNotFoundWhenClientIsNull() {
        UserProfileFormatter formatter = new UserProfileFormatter();

        UserInfo userInfo = new UserInfo(null, UserProfileStatus.ACTIVE_VPN);
        String result = formatter.format(userInfo);
        assertEquals("Профиль не найден", result);
    }

    @Test
    public void testThatFormatReturnsMinimalProfileWhenOnlyIdIsPresent() {
        ClientTransfer client = new ClientTransfer(12345L, null, null, null, null, false, null);
        UserInfo userInfo = new UserInfo(client, UserProfileStatus.ACTIVE_VPN);
        UserProfileFormatter formatter = new UserProfileFormatter();
        String result = formatter.format(userInfo);

        assertTrue(result.contains("📋 Ваш профиль"));
        assertTrue(result.contains("🆔 ID: 12345"));
    }

    @Test
    public void testThatFormatCredentialsForConnectionHandlesEmptyString() {
        String result = UserProfileFormatter.formatCredentialsForConnection("");
        assertEquals("", result);
    }

    @Test
    public void testThatFormatCredentialsForConnectionProcessesLoginPasswordAndSecret() {
        String input = "Login for l2tp: user\nPassword for l2tp: pass\nSecret: key";
        String result = UserProfileFormatter.formatCredentialsForConnection(input);

        assertTrue(result.contains("<code>user</code>"));
        assertTrue(result.contains("<code>pass</code>"));
        assertTrue(result.contains("<code>key</code>"));
    }

    @Test
    public void testThatFormatCredentialsForConnectionDoNotChangeIrrelevantLines() {
        String input = "Some random line\nLogin for l2tp: user\nAnother line";
        String result = UserProfileFormatter.formatCredentialsForConnection(input);

        assertTrue(result.contains("<code>user</code>"));
        assertTrue(result.contains("Some random line"));
        assertFalse(result.contains("<code>Some random line</code>"));
    }
}
