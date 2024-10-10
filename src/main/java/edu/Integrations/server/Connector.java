package edu.Integrations.server;

public interface Connector {
    /**
     * Генерация нового VPN-профиля.
     * @return Строка конфигурации для подключения (например, VLESS).
     */
    String generateProfile();

    /**
     * Удаление существующего VPN-профиля по идентификатору.
     * @param profileId Уникальный идентификатор профиля.
     * @return true, если профиль был успешно удалён, иначе false.
     */
    boolean deleteProfile(String profileId);
}

