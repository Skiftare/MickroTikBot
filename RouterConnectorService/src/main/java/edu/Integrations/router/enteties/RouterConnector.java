package edu.Integrations.router.enteties;

import edu.EncryptionUtil;
import edu.Integrations.router.VpnProfileServerManager;
import edu.dto.ClientDtoToRouter;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import edu.dto.ClientDtoToRouterWithVpnProfile;

import java.util.Random;
import java.util.logging.Logger;

@SuppressWarnings("HideUtilityClassConstructor")
public class RouterConnector implements VpnProfileServerManager {
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
    private static final String ERROR_AT_CONNECTION = "Не удалось установить соединение! Ошибка: ";
    private static final String ERROR_FOR_MORE_INFO = "Ошибка: ";
    private static final String LOG_MESSAGE_FOR_CONNECT = "Попытка подключения для создания ключа...";
    private static final String LOG_SUCCESSFUL_CONNECT = "Подключение для создания ключа успешно установлено.";

    private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private static final String PREFERRED_AUTHENTICATIONS = "PreferredAuthentications";
    private static final String PUBLICKEY_PASSWORD = "publickey,password";

    private static final String LOGIN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";
    private static final Integer USERS_LOGIN_LENGTH = 5;
    private static final String PASSWORD_CHARACTERS = LOGIN_CHARACTERS
            + "0123456789" + LOGIN_CHARACTERS;
    private static final Integer USERS_PASSWORD_LENGTH = 10;

    private static final String MONTHLY_PROFILE = "30d";
    private static final String TRIAL_PROFILE = "1d";

    private static final String COMMAND_FOR_ADDING_USER_TO_USER_MANAGER =
            "/tool user-manager user add customer=admin disabled=no password=";
    private static final String AMOUNT_OF_SHARED_USERS_IN_USER_MANAGER = " shared-users=1";
    private static final String COMMAND_FOR_ACTIVATING_USER_TO_USER_MANAGER =
            "/tool user-manager user create-and-activate-profile \"";
    private static final String USERNAME_STRING_AS_PART_OF_COMMAND = " username=";
    private static final String PROFILE_STRING_AS_PART_OF_COMMAND = " profile=";
    private static final String CUSTOMER_ADMIN_STRING_AS_PART_OF_COMMAND = "\" customer=admin";
    private static final String EXEC_COMMAND = "exec";
    private static final String LOGIN_STRING_FOR_PARSING = "Login for l2tp:";


    private static String generateSuccessMessageForUser(String finalLogin, String finalPass) {
        String res = "VPN профиль успешно создан!\n"
                + "Адрес VPN-сервера: " + SERVER_IP + "\n"
                + "\nLogin for l2tp: " + finalLogin
                + "\n\nPassword for l2tp: " + finalPass
                + "\n\nSecret: vpn";
        return res;
    }

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String initialisationSecret(ClientDtoToRouter clientTransfer) {
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
                    COMMAND_FOR_ADDING_USER_TO_USER_MANAGER
                            + finalPass + AMOUNT_OF_SHARED_USERS_IN_USER_MANAGER
                            + USERNAME_STRING_AS_PART_OF_COMMAND + finalLogin,
                    COMMAND_FOR_ACTIVATING_USER_TO_USER_MANAGER
                            + finalLogin + CUSTOMER_ADMIN_STRING_AS_PART_OF_COMMAND
                            + PROFILE_STRING_AS_PART_OF_COMMAND + MONTHLY_PROFILE
            };

            for (String command : commands) {
                // Создаем новый канал для каждой команды
                ChannelExec channel = (ChannelExec) session.openChannel(EXEC_COMMAND);
                LOGGER.info(command);
                channel.setCommand(command);

                // Подключаем канал, выполняем команду и закрываем канал
                channel.connect();
                Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
                channel.disconnect();
            }

            session.disconnect();

            String result = generateSuccessMessageForUser(finalLogin, finalPass);
            return result;

        } catch (Exception e) {
            stateString.append(ERROR_AT_CONNECTION).append(e.getMessage());
            LOGGER.severe(ERROR_FOR_MORE_INFO + e);
            return stateString.toString();
        }
    }

    // Метод для инициализации секретного ключа (вызывается из другого класса)
    public static String initialisationTrial(ClientDtoToRouter clientTransfer) {
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
                    COMMAND_FOR_ADDING_USER_TO_USER_MANAGER + finalPass
                            + AMOUNT_OF_SHARED_USERS_IN_USER_MANAGER
                            + USERNAME_STRING_AS_PART_OF_COMMAND + finalLogin,
                    COMMAND_FOR_ACTIVATING_USER_TO_USER_MANAGER + finalLogin
                            + CUSTOMER_ADMIN_STRING_AS_PART_OF_COMMAND
                            + PROFILE_STRING_AS_PART_OF_COMMAND + TRIAL_PROFILE
            };

            for (String command : commands) {
                // Создаем новый канал для каждой команды
                ChannelExec channel = (ChannelExec) session.openChannel(EXEC_COMMAND);
                LOGGER.info(command);
                channel.setCommand(command);

                // Подключаем канал, выполняем команду и закрываем канал
                channel.connect();
                Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
                channel.disconnect();
            }

            session.disconnect();

            String result = generateSuccessMessageForUser(finalLogin, finalPass);
            return result;

        } catch (Exception e) {
            stateString.append(ERROR_AT_CONNECTION).append(e.getMessage());
            LOGGER.severe(ERROR_FOR_MORE_INFO + e);
            return stateString.toString();
        }
    }

    public static String prolongSecret(ClientDtoToRouterWithVpnProfile clientTransfer) {
        StringBuilder stateString = new StringBuilder();
        String profileData = null;
        try {
            profileData = EncryptionUtil.decrypt(clientTransfer.vpnProfile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Извлекаем логин из profileData
        String finalLogin = profileData.lines()
                .filter(line -> line.startsWith(LOGIN_STRING_FOR_PARSING))
                .map(line -> line.replace(LOGIN_STRING_FOR_PARSING, "").trim())
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
            String command = COMMAND_FOR_ACTIVATING_USER_TO_USER_MANAGER
                    + finalLogin + CUSTOMER_ADMIN_STRING_AS_PART_OF_COMMAND
                    + PROFILE_STRING_AS_PART_OF_COMMAND + MONTHLY_PROFILE;

            ChannelExec channel = (ChannelExec) session.openChannel(EXEC_COMMAND);
            LOGGER.info(command);
            channel.setCommand(command);

            // Подключаем канал, выполняем команду и закрываем канал
            channel.connect();
            Thread.sleep(TIMEOUT);  // Даем время на выполнение команды
            channel.disconnect();
            session.disconnect();

            return profileData;

        } catch (Exception e) {
            stateString.append(ERROR_AT_CONNECTION).append(e.getMessage());
            LOGGER.severe(ERROR_FOR_MORE_INFO + e);
            return stateString.toString();
        }
    }
}
