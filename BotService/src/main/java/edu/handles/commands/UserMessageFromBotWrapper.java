package edu.handles.commands;

import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.objects.Update;

public record UserMessageFromBotWrapper(
        Long userId,
        String userMessage,
        UserProfileStatus status,
        String firstName,
        String lastName,
        Boolean hasContact,
        String phoneNumber

) {
    public UserMessageFromBotWrapper {

        if (!Boolean.TRUE.equals(hasContact)) {
            phoneNumber = null;
        }
    }

    public UserMessageFromBotWrapper(Long userId,
                                     String userMessage,
                                     UserProfileStatus status,
                                     String firstName,
                                     String lastName) {

        this(userId, userMessage, status, firstName, lastName, false, null);

    }

    public UserMessageFromBotWrapper(Update update, UserProfileStatus status) {
        this(
                update.getMessage().getChatId(),
                update.getMessage().getText(),
                status,
                update.getMessage().getChat().getFirstName(),
                update.getMessage().getChat().getLastName(),
                update.getMessage().hasContact(),
                (update.getMessage().hasContact()
                        ? update.getMessage().getContact().getPhoneNumber() : null)
        );
    }
}
