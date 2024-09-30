package edu.Configuration;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.util.logging.Logger;

public class SSHConnection {

    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 1000;

    private static Logger logger = Logger.getLogger(SSHConnection.class.getName());

    // Настройки подключения
    private static final String HOST = "localhost";  // IP-адрес Mikrotik, теперь localhost
    private static final int PORT = 2222;            // Порт SSH, используем 2222 для подключения
    private static final String USER = "admin";      // Имя пользователя (admin)
    private static final String PASSWORD = "";       // Пароль (если есть)

    public static String establishingSSH() {
        StringBuilder stateString = new StringBuilder();  // Используем StringBuilder для накопления данных
        try {
            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            // Отключаем проверку хоста, чтобы избежать вопросов о ключе
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password"); // Настройки аутентификации

            logger.info("Trying to connect to SSH...");

            // Устанавливаем соединение
            session.connect();
            logger.info("Connected successfully.");

            // Создаем канал для выполнения команды
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            // Команда, которую вы хотите отправить на Mikrotik
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
                    // Добавляем новые данные к результату
                    stateString.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() == 0) {
                        break;
                    }
                    logger.info("Exit-status: " + channel.getExitStatus());
                }
                Thread.sleep(TIMEOUT);
            }

            // Закрываем канал и сессию
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            stateString.append("Couldn't establish connection! Error: " + e.getMessage());
            logger.info("Error: " + e);
        }

        // Возвращаем накопленные данные
        return stateString.toString();
    }
}
