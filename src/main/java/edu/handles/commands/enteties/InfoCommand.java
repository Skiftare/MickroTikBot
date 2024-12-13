package edu.handles.commands.enteties;


import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;

public class InfoCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private static final String COMMAND_DESCRIPTION = "Ознакомительная информация о боте";
    private static final String COMMAND_NAME = "/info";
    private static final String INFO_MESSAGE =
            "Этот бот помогает установить безопасное приватное VPN-соединение, "
                    + "что особенно полезно для повышения уровня конфиденциальности в "
                    + "общественных местах, таких как кофейни.";

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {

        return new BotResponseToUserWrapper(update.userId(), INFO_MESSAGE);
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return true;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
