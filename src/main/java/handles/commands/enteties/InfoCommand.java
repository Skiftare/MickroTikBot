package handles.commands.enteties;


import handles.commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private static final String COMMAND_DESCRIPTION = "Ознакомительная информация о боте";
    private static final String COMMAND_NAME = "/info";
    private static final String INFO_MESSAGE = "Этот бот помогает установить безопасное приватное VPN-соединение, что особенно полезно для повышения уровня конфиденциальности в общественных местах, таких как кофейни.";

    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(INFO_MESSAGE);
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
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