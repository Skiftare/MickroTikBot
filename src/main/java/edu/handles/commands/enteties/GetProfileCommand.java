package edu.handles.commands.enteties;

import edu.Configuration.SSHConnection;
import edu.Data.JdbcDataManager;
import edu.Data.PaymentDataManager;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static edu.Integrations.server.CryptoGenerator.generateCheckSum;
import static javax.crypto.Cipher.PUBLIC_KEY;

public class GetProfileCommand implements Command {
    private  JdbcDataManager jdbcDataManager;
    public GetProfileCommand(JdbcDataManager jdbcDataManager) {
        this.jdbcDataManager = jdbcDataManager;
    }
    private boolean isSSHActive(){
        return !SSHConnection.establishingSSH().contains("Не удалось");
    }
    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        if(isSSHActive()){
            message.setText("Приносим свои извинения, точка подключения временно недоступна.");
        }
        else{
            String checkSum = generateCheckSum(update.getMessage().getFrom().getId());
            PaymentDataManager.addWaitingForPayment(update.getMessage().getFrom().getId(), checkSum, 100L);
            message.setText("" +
                    "Отправьте 100 XLM на " +
                    "криптокошелек " +
                    "бота для получения ссылки в течение 1 часа. " +
                    "Обязательно проверяйте, прошёл ли ваш платёж. " +
                    "В целях защиты от брутфорса ваша оплата не проверяется автоматически.\n\n\n" +
                    "```"+PUBLIC_KEY+"```");
            jdbcDataManager.setPaymentProcessStatus(update.getMessage().getFrom().getId(), true);

        }
        return message;

    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return status == UserProfileStatus.ACTIVE_VPN || status == UserProfileStatus.NO_VPN;
    }

    @Override
    public String getCommandName() {
        return "Купить/продлить профиль за 100 XLM";
    }

    @Override
    public String getCommandDescription() {
        return "Позволяет купить или продлить VPN-профиль за 100 XLM";
    }
}
