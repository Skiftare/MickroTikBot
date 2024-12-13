package edu.Data.formatters;

import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Класс для форматирования профиля пользователя.
 */
public final class UserProfileFormatter {
    private final static String LOGIN_STRING = "Login for l2tp:";
    private final static String PASSWORD_STRING = "Password for l2tp:";
    private final static String SECRET_STRING = "Secret:";
    private final static String ENDL_STRING = "\n";

    /**
     * Форматирует информацию о пользователе.
     *
     * @param userInfo информация о пользователе
     * @return отформатированная строка профиля пользователя
     */
    public String format(final UserInfo userInfo) {
        if (userInfo == null || userInfo.client() == null) {
            return "Профиль не найден";
        }
        return formatClientInfo(userInfo);
    }

    private String formatClientInfo(final UserInfo userInfo) {
        ClientTransfer client = userInfo.client();

        StringBuilder result = new StringBuilder();
        result.append("📋 Ваш профиль:");
        result.append(ENDL_STRING);
        result.append(ENDL_STRING);
        result.append("🆔 ID: ").append(client.tgUserId()).append(ENDL_STRING);
        result.append("😎 Ваш статус в Системе: ").append(userInfo.status().toString())
                .append(ENDL_STRING);

        if (client.name() != null) {
            result.append("👤 Имя: ").append(client.name()).append(ENDL_STRING);
        }

        if (client.phone() != null) {
            result.append("📱 Телефон: ").append(client.phone()).append(ENDL_STRING);
        }
        if (client.balance() != null) {
            result.append("💰 Баланс: ").append(client.balance()).append(ENDL_STRING);
        }

        if (client.vpnProfile() != null) {
            try {
                result.append(ENDL_STRING);
                result.append("🔐 VPN профиль: ").append((EncryptionUtil.decrypt(client.vpnProfile())));
            } catch (Exception e) {
                Logger.getAnonymousLogger().info("Exception: " + e.getMessage());
            }
            result.append(ENDL_STRING);
            result.append("Статус: ").append(client.isVpnProfileAlive() ? "✅ Активен" : "❌ Неактивен");

            if (client.expiredAt() != null && client.expiredAt().getTime() != 0
                    && !(client.expiredAt().equals(new Date(0)))) {
                result.append(ENDL_STRING);
                result.append("⏰ Действует до: ").append(client.expiredAt());
            }
            result.append("\n");
            result.append("https://t.me/MikroTikBotTGC/7");
        }
        return result.toString();
    }

    public static String formatCredentialsForConnection(String incomedString) {
        String[] lines = incomedString.split(ENDL_STRING);

        StringBuilder result = new StringBuilder();
        String bufferedLine;
        for (String line : lines) {
            bufferedLine = line;
            if (line.startsWith(LOGIN_STRING)) {
                bufferedLine = formatLine(line, LOGIN_STRING);
            } else if (line.startsWith(PASSWORD_STRING)) {
                bufferedLine = formatLine(line, PASSWORD_STRING);
            } else if (line.startsWith(SECRET_STRING)) {
                bufferedLine = formatLine(line, SECRET_STRING);
            }
            result.append(bufferedLine).append(ENDL_STRING);
        }

        return result.toString().trim();
    }

    private static String formatLine(String line, String prefix) {
        String value = line.substring(prefix.length()).trim();
        String plainTextMarkdownFormatter = "`";
        return prefix + " " + plainTextMarkdownFormatter + value + plainTextMarkdownFormatter;
    }

}
