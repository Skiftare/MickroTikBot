package edu.Integrations.server;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class SecretInitialiser {

    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 1000;

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

            final String loginCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz";
            final String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789@#!%";

            StringBuilder loginSuffix = new StringBuilder(5);

            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                int index = random.nextInt(loginCharacters.length());
                loginSuffix.append(loginCharacters.charAt(index));
            }

            StringBuilder password = new StringBuilder(10);

            for (int i = 0; i < 10; i++) {
                int index = random.nextInt(passwordCharacters.length());
                password.append(passwordCharacters.charAt(index));
            }

            finalLogin = UserID + "_" + loginSuffix.toString();
            finalPass = password.toString();

            // Устанавливаем соединение
            session.connect();
            LOGGER.info("Подключение для создания ключа успешно установлено.");

            // Создаем канал для выполнения команды
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            // Команда для выполнения на Mikrotik
            String command = "/ppp secret add profile=user-profile name=" + finalLogin + " password=" + finalPass + " service=l2tp";
            LOGGER.info(command);
            channel.setCommand(command);
            channel.connect();

            // Закрываем канал и сессию
            channel.disconnect();
            session.disconnect();

            String result = "VPN профиль успешно создан!\n\nВаш логин для VPN: " + finalLogin + "\n\nВаш пароль для VPN: " + finalPass;
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }

    public static String rewriteEndDataOfSecret(Long UserID, Duration date) {
        return "Not realized yet";
    }
}
