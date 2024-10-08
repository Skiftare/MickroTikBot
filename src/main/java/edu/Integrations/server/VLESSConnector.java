package edu.Integrations.server;

import java.io.File;
import java.util.UUID;

public class VLESSConnector implements Connector {

    private static final String BASE_PATH = "/etc/xray/config/";

    @Override
    public String generateProfile() {
        String uuid = UUID.randomUUID().toString();
        String config = String.format(
                "vless://%s@vpn.example.com:443?type=tcp&security=tls", uuid
        );

        // Логика сохранения профиля в файл (например, /etc/xray/config/vless_<uuid>.json)
        // Здесь можно записать файл с конфигурацией в нужную директорию

        System.out.println("Profile created with UUID: " + uuid);
        return config;
    }

    @Override
    public boolean deleteProfile(String profileId) {
        // Логика удаления профиля по profileId (например, удаление файла /etc/xray/config/vless_<profileId>.json)
        // Проверяем существование и удаляем файл
        File file = new File(BASE_PATH + "vless_" + profileId + ".json");
        if (file.exists()) {
            return file.delete();
        } else {
            System.err.println("Profile with ID: " + profileId + " not found.");
            return false;
        }
    }
}
