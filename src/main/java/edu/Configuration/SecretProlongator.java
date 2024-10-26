package edu.Configuration;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Random;
import java.util.logging.Logger;

public class SecretProlongator {

    private static final int TIMEOUT = 2500;

    private static final Logger LOGGER = Logger.getLogger(SecretProlongator.class.getName());

    // Настройки подключения
    private static final String HOST = System.getenv("CHR_ROUTER_IP");  // Имя сервиса в docker-compose
    private static final int PORT = 2222;             // Порт SSH
    private static final String USER = System.getenv("NEW_ROUTER_LOGIN");
    private static final String PASSWORD = System.getenv("NEW_ROUTER_PASS");

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String prolongSecret(Long UserID) {
        StringBuilder stateString = new StringBuilder();

        String finalLogin = String.valueOf(UserID);
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

            LOGGER.info("Попытка подключения для продления аккаунта...");

            Random random = new Random();

            // Устанавливаем соединение
            session.connect();
            LOGGER.info("Подключение для создания ключа успешно установлено.");

            final String useProfile = "30d";


            // Команды для выполнения на Mikrotik
            String command = "/tool user-manager user create-and-activate-profile \"" + finalLogin + "\" customer=admin " +
                            "profile=" + useProfile;

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            LOGGER.info(command);
            channel.setCommand(command);

            // Подключаем канал, выполняем команду и закрываем канал
            channel.connect();
            Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
            channel.disconnect();
            session.disconnect();

            String result = "VPN профиль успешно продлён!\n\nАдрес VPN-сервера: hcs088zaj9a.sn.mynetname.net\nSecret: vpn";
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }
}
