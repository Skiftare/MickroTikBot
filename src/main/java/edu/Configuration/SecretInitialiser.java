package edu.Configuration;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Random;
import java.util.logging.Logger;

public class SecretInitialiser {

    private static final int TIMEOUT = 2500;

    private static final Logger LOGGER = Logger.getLogger(SecretInitialiser.class.getName());

    // Настройки подключения
    private static final String HOST = System.getenv("CHR_ROUTER_IP");  // Имя сервиса в docker-compose
    private static final int PORT = 2222;             // Порт SSH
    private static final String USER = System.getenv("NEW_ROUTER_LOGIN");
    private static final String PASSWORD = System.getenv("NEW_ROUTER_PASS");

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String initialisationSecret(Long UserID) {
        StringBuilder stateString = new StringBuilder();
        String finalLogin = "";
        String finalPass = "";
        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException("NEW_ROUTER_LOGIN или NEW_ROUTER_PASS не установлены");
            }

            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,password");

            LOGGER.info("Попытка подключения для создания ключа...");

            final String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789@#!%";

            Random random = new Random();

            StringBuilder password = new StringBuilder(10);

            for (int i = 0; i < 10; i++) {
                int index = random.nextInt(passwordCharacters.length());
                password.append(passwordCharacters.charAt(index));
            }

            finalLogin = String.valueOf(UserID);
            finalPass = password.toString();

            // Устанавливаем соединение
            session.connect();
            LOGGER.info("Подключение для создания ключа успешно установлено.");

            final String useProfile = "30d";

    // Команды для выполнения на Mikrotik
            String[] commands = {
                    "/tool user-manager user add customer=admin disabled=no password=" + finalPass + " shared-users=1 " +
                            "username=" + finalLogin,
                    "/tool user-manager user create-and-activate-profile \"" + finalLogin + "\" customer=admin " +
                            "profile=" + useProfile
            };

            for (String command : commands) {
                // Создаем новый канал для каждой команды
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                LOGGER.info(command);
                channel.setCommand(command);

                // Подключаем канал, выполняем команду и закрываем канал
                channel.connect();
                Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
                channel.disconnect();
            }

            session.disconnect();

            String result = "VPN профиль успешно создан!\n\nАдрес VPN-сервера: hcs088zaj9a.sn.mynetname.net\n\nSecret: vpn\n\nВаш логин для VPN: " + finalLogin + "\n\nВаш пароль для VPN: " + finalPass;
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }
}
