package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.ws.rs.client.Client;
import javax.xml.crypto.Data;

public class UserProfileCommand implements Command {
    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private DataManager dataManager;
    public UserProfileCommand(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public SendMessage execute(Update update) {
        UserProfileStatus status = dataManager.getUserProfileStatus(update.getMessage().getFrom().getId());
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        ClientTransfer clientTransfer = dataManager.findById(update.getMessage().getFrom().getId());
        message.setText("Ваш статус: " + status + "\n" +
                "Есть ли у вас ВПН: "+ clientTransfer.isVpnProfileAlive() +
                "\n" +
                "Коннекшн будет жив до: " + clientTransfer.expiredAt()
        );

        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "/my_profile";
    }

    @Override
    public String getCommandDescription() {
        return "Позволяет посмотреть свой профиль и статус";
    }
}
