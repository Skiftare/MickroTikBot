package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.PaymentDataManager;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.xml.crypto.Data;
import java.time.Duration;

public class CheckPayment implements Command {
    private DataManager dataManager;

    public CheckPayment(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public String getCommandName() {
        return "/check";
    }

    @Override
    public String getCommandDescription() {
        return "Проверить оплату";
    }

    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());

        if(PaymentDataManager.isPaymentSuccess(update.getMessage().getFrom().getId())){
            dataManager.setPaymentProcessStatus(update.getMessage().getFrom().getId(), false);
            if(dataManager.getUserProfileStatus(update.getMessage().getFrom().getId()) == UserProfileStatus.NO_VPN){
                dataManager.extendVpnProfile(update.getMessage().getFrom().getId(), Duration.ofDays(30));
                message.setText("Ваш профиль успешно продлен на 30 дней.\n");
            }

            message.setText("Ваша оплата успешно обработана.");


        }
        message.setText("Пока что Вашей оплаты нет в базе данных. Попробуйте позже.");
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return status == UserProfileStatus.ACTIVE_PAYMENT;
    }


}
