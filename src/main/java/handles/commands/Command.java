package handles.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    SendMessage execute(Update update);

    boolean isVisibleForKeyboard();

    String getCommandName();

    String getCommandDescription();
}
