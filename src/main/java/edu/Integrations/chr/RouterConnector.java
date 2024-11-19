package edu.Integrations.chr;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import edu.Data.dto.ClientTransfer;

import java.util.Random;
import java.util.logging.Logger;

import static edu.Data.formatters.EncryptionUtil.decrypt;

@SuppressWarnings("HideUtilityClassConstructor")
public class RouterConnector {
    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 1000;

    private static final Logger LOGGER = Logger.getLogger(RouterConnector.class.getName());

    // Настройки подключения
    private static final String HOST = System.getenv("CHR_ROUTER_IP");  // Имя сервиса в docker-compose
    private static final int PORT = 2222;             // Порт SSH
    private static final String USER = System.getenv("NEW_ROUTER_LOGIN");
    private static final String PASSWORD = System.getenv("NEW_ROUTER_PASS");
    private static final String SERVER_IP = System.getenv("SERVER_IP");

    private static final String ERROR_AT_CREDENTIALS_ARE_NULL = "NEW_ROUTER_LOGIN или NEW_ROUTER_PASS не установлены";
    private static final String LOG_MESSAGE_FOR_CONNECT = "Попытка подключения для создания ключа...";
    private static final String LOG_SUCCESSFUL_CONNECT = "Подключение для создания ключа успешно установлено.";

    private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private static final String PREFERRED_AUTHENTICATIONS = "PreferredAuthentications";
    private static final String PUBLICKEY_PASSWORD = "publickey,password";

    private static final String LOGIN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";
    private static final Integer USERS_LOGIN_LENGTH = 5;
    private static final String PASSWORD_CHARACTERS = LOGIN_CHARACTERS
            + "0123456789@#!%";
    private static final Integer USERS_PASSWORD_LENGTH = 10;

    private static final String MONTHLY_PROFILE = "30d";
    private static final String TRIAL_PROFILE = "1d";

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String initialisationSecret(ClientTransfer clientTransfer) {
        StringBuilder stateString = new StringBuilder();
        String finalLogin = "";
        String finalPass = "";
        Long userId = clientTransfer.tgUserId();
        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException(ERROR_AT_CREDENTIALS_ARE_NULL);
            }

            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста
            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(PREFERRED_AUTHENTICATIONS, PUBLICKEY_PASSWORD);

            LOGGER.info(LOG_MESSAGE_FOR_CONNECT);


            StringBuilder loginSuffix = new StringBuilder(USERS_LOGIN_LENGTH);

            Random random = new Random();
            for (int i = 0; i < USERS_LOGIN_LENGTH; i++) {
                int index = random.nextInt(LOGIN_CHARACTERS.length());
                loginSuffix.append(LOGIN_CHARACTERS.charAt(index));
            }

            StringBuilder password = new StringBuilder(USERS_PASSWORD_LENGTH);

            for (int i = 0; i < USERS_PASSWORD_LENGTH; i++) {
                int index = random.nextInt(PASSWORD_CHARACTERS.length());
                password.append(PASSWORD_CHARACTERS.charAt(index));
            }

            finalLogin = userId + "_" + loginSuffix;
            finalPass = password.toString();

            // Устанавливаем соединение
            session.connect();
            LOGGER.info(LOG_SUCCESSFUL_CONNECT);


            // Команды для выполнения на Mikrotik
            String[] commands = {
                    "/tool user-manager user add customer=admin disabled=no password="
                            + finalPass + " shared-users=1 "
                            + "username=" + finalLogin,
                    "/tool user-manager user create-and-activate-profile \""
                            + finalLogin + "\" customer=admin "
                            + "profile=" + MONTHLY_PROFILE
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

            String result = "VPN профиль успешно создан!\n" +
                    "Адрес VPN-сервера: " + SERVER_IP + "\n" +
                    "\nLogin for l2tp: " + finalLogin + "\n\nPassword for l2tp: " + finalPass +
                    "\n\nSecret: vpn";
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String initialisationTrial(ClientTransfer clientTransfer) {
        StringBuilder stateString = new StringBuilder();
        String finalLogin = "";
        String finalPass = "";
        Long userId = clientTransfer.tgUserId();
        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException(ERROR_AT_CREDENTIALS_ARE_NULL);
            }

            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста
            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(PREFERRED_AUTHENTICATIONS, PUBLICKEY_PASSWORD);

            LOGGER.info(LOG_MESSAGE_FOR_CONNECT);


            StringBuilder loginSuffix = new StringBuilder(USERS_LOGIN_LENGTH);

            Random random = new Random();
            for (int i = 0; i < USERS_LOGIN_LENGTH; i++) {
                int index = random.nextInt(LOGIN_CHARACTERS.length());
                loginSuffix.append(LOGIN_CHARACTERS.charAt(index));
            }

            StringBuilder password = new StringBuilder(USERS_PASSWORD_LENGTH);

            for (int i = 0; i < USERS_PASSWORD_LENGTH; i++) {
                int index = random.nextInt(PASSWORD_CHARACTERS.length());
                password.append(PASSWORD_CHARACTERS.charAt(index));
            }

            finalLogin = userId + "_" + loginSuffix;
            finalPass = password.toString();

            // Устанавливаем соединение
            session.connect();
            LOGGER.info(LOG_SUCCESSFUL_CONNECT);



            // Команды для выполнения на Mikrotik
            String[] commands = {
                    "/tool user-manager user add customer=admin disabled=no password=" + finalPass + " shared-users=1 " +
                            "username=" + finalLogin,
                    "/tool user-manager user create-and-activate-profile \"" + finalLogin + "\" customer=admin " +
                            "profile=" + TRIAL_PROFILE
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

            String result = "VPN профиль успешно создан!\n" +
                    "Адрес VPN-сервера: " + SERVER_IP + "\n" +
                    "\nLogin for l2tp: " + finalLogin + "\n\nPassword for l2tp: " + finalPass +
                    "\n\nSecret: vpn";
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }

    public static String
    prolongSecret(ClientTransfer clientTransfer) {
        StringBuilder stateString = new StringBuilder();
        String profileData = null;
        try {
            profileData = decrypt(clientTransfer.vpnProfile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Извлекаем логин из profileData
        String finalLogin = profileData.lines()
                .filter(line -> line.startsWith("Login for l2tp:"))
                .map(line -> line.replace("Login for l2tp:", "").trim())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не удалось найти логин в VPN профиле"));

        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException(ERROR_AT_CREDENTIALS_ARE_NULL);
            }

            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста
            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(PREFERRED_AUTHENTICATIONS, PUBLICKEY_PASSWORD);

            LOGGER.info("Попытка подключения для продления аккаунта...");


            // Устанавливаем соединение
            session.connect();
            LOGGER.info(LOG_SUCCESSFUL_CONNECT);




            // Команды для выполнения на Mikrotik
            String command = "/tool user-manager user create-and-activate-profile \""
                    + finalLogin + "\" customer=admin "
                    + "profile=" + MONTHLY_PROFILE;

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            LOGGER.info(command);
            channel.setCommand(command);

            // Подключаем канал, выполняем команду и закрываем канал
            channel.connect();
            Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
            channel.disconnect();
            session.disconnect();

            String result = profileData;
            return result;

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка: ").append(e.getMessage());
            LOGGER.severe("Ошибка: " + e);
            return stateString.toString();
        }
    }
}
