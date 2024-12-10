package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.UserInfo;
import edu.Data.formatters.UserProfileFormatter;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;

import java.util.logging.Logger;

public class GetUserProfileCommand implements Command {
    DataManager dataManager;
    UserProfileFormatter userProfileFormatter;
    private static final String INSTRUCTION_VIDEO_URL = "URL To Hosted Video";

    public GetUserProfileCommand(DataManager incomingDataManager, UserProfileFormatter incomingUserProfileFormatter) {
        dataManager = incomingDataManager;
        userProfileFormatter = incomingUserProfileFormatter;
    }


    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {
        Long tgIdLong = update.userId();
        UserInfo allUserInfo = dataManager.getInfoById(tgIdLong);
        String formattedInfo = userProfileFormatter.format(allUserInfo);

        Logger.getAnonymousLogger().info(formattedInfo);
        return new BotResponseToUserWrapper(tgIdLong, formattedInfo, true, null, null, INSTRUCTION_VIDEO_URL);
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
