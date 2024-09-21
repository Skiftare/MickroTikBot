package edu.Configuration;

import edu.handles.commands.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardMarkupBuilder {

    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private final List<KeyboardRow> keyboard;

    public KeyboardMarkupBuilder(List<Command> commands) {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard = new ArrayList<>();

        for (Command command : commands) {
            if (command.isVisibleForKeyboard()) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(command.getCommandName()));
                keyboard.add(row);
            }
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public ReplyKeyboardMarkup build() {
        return replyKeyboardMarkup;
    }
}
