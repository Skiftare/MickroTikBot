package edu.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@SuppressWarnings("HideUtilityClassConstructor")
public class SSHConnection {

    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 1000;

    private static final Logger LOGGER = Logger.getLogger(SSHConnection.class.getName());

    // Настройки подключения
    private static final String HOST = System.getenv("CHR_ROUTER_IP");  // Имя сервиса в docker-compose
    private static final int PORT = 2222;             // Порт SSH
    private static final String USER = System.getenv("NEW_ROUTER_LOGIN");
    private static final String PASSWORD = System.getenv("NEW_ROUTER_PASS");

    public static String establishingSSH() {
        StringBuilder stateString = new StringBuilder();
        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException("NEW_ROUTER_LOGIN или NEW_ROUTER_PASS не установлены");
            }
            
            LOGGER.info("Попытка подключения с использованием USER: " + USER);
            
            // Проверка доступности хоста
            LOGGER.info("Проверка доступности хоста " + HOST + " на порту " + PORT);
            if (!isHostReachable(HOST, PORT)) {
                throw new IllegalStateException("Хост " + HOST + " недоступен на порту " + PORT);
            }

            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,password");

            LOGGER.info("Попытка подключения к SSH...");

            // Устанавливаем соединение
            session.connect();
            LOGGER.info("Подключение успешно установлено.");

            // Создаем канал для выполнения команды
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            // Команда для выполнения на Mikrotik
            String command = "/system resource print";
            channel.setCommand(command);

            // Получаем входной поток для чтения результата выполнения команды
            InputStream in = channel.getInputStream();
            channel.connect();

            // Читаем результат выполнения команды
            byte[] tmp = new byte[BUFFER_SIZE];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, BUFFER_SIZE);
                    if (i < 0) {
                        break;
                    }
                    stateString.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() == 0) {
                        break;
                    }
                    LOGGER.info("Exit-status: " + channel.getExitStatus());
                }
                Thread.sleep(TIMEOUT);
            }

            // Закрываем канал и сессию
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            stateString.append("Не удалось установить соединение! Ошибка!");
            LOGGER.severe("Ошибка: " + e);
        }

        return stateString.toString();
    }

    private static boolean isHostReachable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            return true;
        } catch (IOException e) {
            LOGGER.warning("Не удалось подключиться к " + host + ":" + port + ". Ошибка: " + e.getMessage());
            return false;
        }
    }
}
