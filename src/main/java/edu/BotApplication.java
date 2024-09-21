package edu;

import edu.Configuration.TelegramBotCore;
import edu.handles.commands.Command;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.tables.CommandTable;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SuppressWarnings("HideUtilityClassConstructor")
public class BotApplication {
    private static Logger logger = Logger.getLogger(BotApplication.class.getName());

    private static CommandTable commandTableAssembling() {
        logger.info("Assembling command table");
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        CommandTable preCommandTable = new CommandTable(infoCommand, authorsCommand);
        Command helpCommand = new HelpCommand(preCommandTable);
        logger.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand);

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            // Сборка бота
            //TODO: стоит ли всё в одном try хранить, или лучше растащить процесс сборки и получше прологировать его?

            CommandTable coreCommandTable = commandTableAssembling();
            logger.info("Registering bot");
            botsApi.registerBot(new TelegramBotCore(coreCommandTable));
            logger.info("Bot registered");
        } catch (TelegramApiException e) {
            logger.info("Bot registration failed with stacktrace: " + e.getStackTrace());
        }
    }
}
