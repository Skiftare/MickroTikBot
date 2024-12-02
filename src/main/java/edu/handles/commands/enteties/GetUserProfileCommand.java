package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.UserInfo;
import edu.Data.formatters.UserProfileFormatter;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;

public class GetUserProfileCommand implements Command {
    DataManager dataManager;
    UserProfileFormatter userProfileFormatter;

    public GetUserProfileCommand(DataManager incomingDataManager, UserProfileFormatter incomingUserProfileFormatter) {
        dataManager = incomingDataManager;
        userProfileFormatter = incomingUserProfileFormatter;
    }


    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {
        Long tgIdLong = update.userId();
        UserInfo allUserInfo = dataManager.getInfoById(tgIdLong);

        return new BotResponseToUserWrapper(tgIdLong, userProfileFormatter.format(allUserInfo));
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
