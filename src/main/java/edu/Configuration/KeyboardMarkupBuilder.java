package edu.Configuration;

import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static edu.models.UserProfileStatus.*;

public class KeyboardMarkupBuilder {

    private LinkedHashMap<UserProfileStatus,ReplyKeyboardMarkup> mapOfKeyboards = new LinkedHashMap<>();
    private final CommandTable commandTable;
    public KeyboardMarkupBuilder( CommandTable commandTable) {

        this.commandTable = commandTable;

        mapOfKeyboards.put(GUEST, buildGuestKeyboard());
        mapOfKeyboards.put(UserProfileStatus.UNCONFIRMED, buildUnconfirmedKeyboard());
        mapOfKeyboards.put(UserProfileStatus.ACTIVE_VPN, buildActiveVPNKeyboard());
        mapOfKeyboards.put(UserProfileStatus.NO_VPN, buildNoVPNKeyboard());

    }

    private  List<KeyboardRow> generateKeyboardByPrivelege(UserProfileStatus status){
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        int countOfUploadedButtons = 0;
        for(Command command : commandTable.getCommands().values()){
            if(command.isVisibleForKeyboard(status)){
                KeyboardButton button = new KeyboardButton(command.getCommandName());
                button.setText(command.getCommandName());
                row.add(button);
                countOfUploadedButtons++;
                if(countOfUploadedButtons % 2 == 0){
                    keyboardRows.add(row);
                    row = new KeyboardRow();
                }
            }
        }
        //Обработка случая когда количество кнопок нечетное
        if(countOfUploadedButtons % 2 != 0){
            keyboardRows.add(row);
        }

        return keyboardRows;
    }




    // Раскладка для гостей (Guest)
    private ReplyKeyboardMarkup buildGuestKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard =  new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(GUEST));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;
    }

    // Раскладка для пользователей без подтвержденного телефона (Unconfirmed)
    private ReplyKeyboardMarkup buildUnconfirmedKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard =  new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(UNCONFIRMED));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;

    }

    // Раскладка для пользователей с активным VPN-профилем (ActiveVPN)
    private ReplyKeyboardMarkup buildActiveVPNKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard =  new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(ACTIVE_VPN));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;

    }

    // Раскладка для пользователей без активного VPN-профиля (NoVPN)
    private ReplyKeyboardMarkup buildNoVPNKeyboard() {
        ReplyKeyboardMarkup rezultKeyboard =  new ReplyKeyboardMarkup();
        rezultKeyboard.setKeyboard(generateKeyboardByPrivelege(NO_VPN));
        rezultKeyboard.setResizeKeyboard(true);
        rezultKeyboard.setOneTimeKeyboard(true);
        return rezultKeyboard;
    }
    public ReplyKeyboardMarkup getKeyboardByStatus(UserProfileStatus status){
        return mapOfKeyboards.get(status);
    }
}
