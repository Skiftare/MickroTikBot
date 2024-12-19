package edu.handles.commands.enteties;


import edu.Integrations.chr.RouterGrpcConnector;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;
import proto.RouterProtos;


public class ProfileCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private static final String COMMAND_DESCRIPTION = "Просмотреть состояние роутера";
    private static final String COMMAND_NAME = "/chr_profile";
    private final RouterGrpcConnector routerGrpcConnector;
    RouterProtos.ClientRequestWithoutIdentification request;

    public ProfileCommand(RouterGrpcConnector routerGrpcConnector) {
        this.routerGrpcConnector = routerGrpcConnector;
    }

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {

        return new BotResponseToUserWrapper(update.userId(), routerGrpcConnector.establishSSH(request));
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {

        return (status == UserProfileStatus.NO_VPN || status == UserProfileStatus.ACTIVE_VPN);

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
