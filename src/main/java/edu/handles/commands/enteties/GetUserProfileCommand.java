package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.UserInfo;
import edu.Data.formatters.UserProfileFormatter;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GetUserProfileCommand implements Command {
    DataManager dataManager;
    UserProfileFormatter userProfileFormatter;

    public GetUserProfileCommand(DataManager incomingDataManager, UserProfileFormatter incomingUserProfileFormatter) {
        dataManager = incomingDataManager;
        userProfileFormatter = incomingUserProfileFormatter;
    }


    @Override
    public SendMessage execute(Update update) {
        SendMessage result = new SendMessage();
        Long tgIdLong = update.getMessage().getChatId();
        result.setChatId(tgIdLong);
        UserInfo allUserInfo = dataManager.getInfoById(tgIdLong);
        result.setText(userProfileFormatter.format(allUserInfo));

        return result;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return true;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "/get_profile";
    }

    @Override
    public String getCommandDescription() {
        return "Команда позволяет получить профиль пользователя";
    }

}
