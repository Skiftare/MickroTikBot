package edu.Data.formatters;

import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Класс для форматирования профиля пользователя.
 */
public final class UserProfileFormatter {

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
        result.append("📋 Ваш профиль:\n\n");
        result.append("🆔 ID: ").append(client.tgUserId()).append("\n");
        result.append("😎 Ваш статус в Системе: ").append(userInfo.status().toString()).append("\n");

        if (client.name() != null) {
            result.append("👤 Имя: ").append(client.name()).append("\n");
        }

        if (client.phone() != null) {
            result.append("📱 Телефон: ").append(client.phone()).append("\n");
        }
        if (client.balance() != null) {
            result.append("💰 Баланс: ").append(client.balance()).append("\n");
        }

        if (client.vpnProfile() != null) {
            try {
                result.append("\n🔐 VPN профиль: ").append(EncryptionUtil.decrypt(client.vpnProfile()));
            } catch (Exception e) {
                Logger.getAnonymousLogger().info("Exception: " + e.getMessage());
            }
            result.append("\nСтатус: ").append(client.isVpnProfileAlive() ? "✅ Активен" : "❌ Неактивен");

            if (client.expiredAt() != null && client.expiredAt().getTime() != 0
                    && !(client.expiredAt().equals(new Date(0)))) {
                result.append("\n⏰ Действует до: ").append(client.expiredAt());
            }
        }
        return result.toString();
    }
}
