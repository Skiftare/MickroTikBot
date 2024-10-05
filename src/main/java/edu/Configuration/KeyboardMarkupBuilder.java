package edu.Configuration;

import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import static edu.models.UserProfileStatus.GUEST;
import static edu.models.UserProfileStatus.UNCONFIRMED;
import static edu.models.UserProfileStatus.ACTIVE_VPN;
import static edu.models.UserProfileStatus.NO_VPN;

public class KeyboardMarkupBuilder {

    private LinkedHashMap<UserProfileStatus, ReplyKeyboardMarkup> mapOfKeyboards = new LinkedHashMap<>();
    private final CommandTable commandTable;

    public KeyboardMarkupBuilder(CommandTable commandTable) {

        this.commandTable = commandTable;

        mapOfKeyboards.put(GUEST, buildGuestKeyboard());
        mapOfKeyboards.put(UNCONFIRMED, buildUnconfirmedKeyboard());
        mapOfKeyboards.put(ACTIVE_VPN, buildActiveVPNKeyboard());
        mapOfKeyboards.put(NO_VPN, buildNoVPNKeyboard());

    }

    private List<KeyboardRow> generateKeyboardByPrivelege(UserProfileStatus status) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        int countOfUploadedButtons = 0;
        for (Command command : commandTable.getCommands().values()) {
            if (command.isVisibleForKeyboard(status)) {
                Logger.getAnonymousLogger().info("Command "
                        + command.getCommandName()
                        + " is visible for status "
                        + status);
                KeyboardButton button = new KeyboardButton(command.getCommandName());
                button.setText(command.getCommandName());
                row.add(button);
                countOfUploadedButtons++;
                if (countOfUploadedButtons % 2 == 0) {
                    keyboardRows.add(row);
                    row = new KeyboardRow();
                }
            }
        }
        //Обработка случая когда количество кнопок нечетное
        if (countOfUploadedButtons % 2 != 0) {
            keyboardRows.add(row);
        }
        Logger.getAnonymousLogger().info("Keyboard for status "
                + status + " was generated with "
                + countOfUploadedButtons
                + " buttons & "
                + keyboardRows.size()
                + " rows");


        return keyboardRows;
    }


    // Раскладка для гостей (Guest)
    private ReplyKeyboardMarkup buildGuestKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard = new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(GUEST));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;
    }

    // Раскладка для пользователей без подтвержденного телефона (Unconfirmed)
    private ReplyKeyboardMarkup buildUnconfirmedKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard = new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(UNCONFIRMED));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;

    }

    // Раскладка для пользователей с активным VPN-профилем (ActiveVPN)
    private ReplyKeyboardMarkup buildActiveVPNKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard = new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(ACTIVE_VPN));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;

    }

    // Раскладка для пользователей без активного VPN-профиля (NoVPN)
    private ReplyKeyboardMarkup buildNoVPNKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard = new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(NO_VPN));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;
    }

    public ReplyKeyboardMarkup getKeyboardByStatus(UserProfileStatus status) {
        return mapOfKeyboards.get(status);
    }
}
