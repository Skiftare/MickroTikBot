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
    private static final String HOST = "10.10.10.10";  // IP-адрес Mikrotik
    private static final int PORT = Integer.parseInt(System.getenv("RUS_CHR_Mikrotik_SSH_Port"));  // Порт SSH
    private static final String USER = System.getenv("RUS_CHR_Mikrotik_User_Name");         // Имя пользователя
    private static final String PASSWORD = ""; // Пароль

    private static void establishingSSH() {
        try {
            // Создаем сессию SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);

            session.setConfig("StrictHostKeyChecking", "no");

            // Устанавливаем соединение
            session.connect();

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
                    logger.info(new String(tmp, 0, i));
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
            logger.info(String.valueOf(e));
        }
    }

    public static void main(String[] args) {
        establishingSSH();  // Вызываем метод для его выполнения
    }
}
