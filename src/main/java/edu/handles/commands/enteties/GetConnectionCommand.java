package edu.handles.commands.enteties;

import edu.Data.PaymentDataManager;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

import static edu.Integrations.server.CryptoGenerator.generateCheckSum;

public class GetConnectionCommand implements Command {
    private final PaymentDataManager paymentDataManager;
    private final String salt = System.getenv("SALT");

    public GetConnectionCommand(PaymentDataManager paymentDataManager) {
        this.paymentDataManager = paymentDataManager;
    }





    @Override
    public SendMessage execute(Update update) {
        update.getMessage().getFrom().getId();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        String checkSum = generateCheckSum(update.getMessage().getFrom().getId());
        paymentDataManager.addWaitingForPayment(update.getMessage().getFrom().getId(), checkSum, 100L);
        message.setText("Отправьте 100 XLM на криптокошелек бота для получения ссылки в течение 1 часа. ");

        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return status == UserProfileStatus.NO_VPN || status == UserProfileStatus.ACTIVE_VPN;
    }

    @Override
    public String getCommandName() {
        return "/connect";
    }

    @Override
    public String getCommandDescription() {
        return "Эта команда позволяет получить профиль для подключения к VPN";
    }
}
