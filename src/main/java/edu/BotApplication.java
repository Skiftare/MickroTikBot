package edu;

import java.util.logging.Logger;

import edu.Configuration.DataConnectConfigurator;
import edu.Configuration.KeyboardMarkupBuilder;
import edu.Data.DataManager;
import edu.handles.commands.enteties.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import edu.Configuration.TelegramBotCore;
import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;


@SuppressWarnings("HideUtilityClassConstructor")
public class BotApplication {
    private static Logger logger = Logger.getLogger(BotApplication.class.getName());
    private static final DataConnectConfigurator dataConnectConfigurator = new DataConnectConfigurator();
    private static final DataManager dataManager = new DataManager(dataConnectConfigurator);

    private static CommandTable commandTableAssembling() {
        logger.info("Assembling command table");

        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();

        Command stateCommand = new StateCommand();
        CommandTable preCommandTable = new CommandTable(infoCommand, authorsCommand, stateCommand);

        Command helpCommand = new HelpCommand(preCommandTable);
        logger.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand, new AuthentificateCommand(dataManager), new RegisterCommand(dataManager), new StateCommand());

    }


    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            // Сборка бота
            //TODO: стоит ли всё в одном try хранить, или лучше растащить процесс сборки и получше прологировать его?
            CommandTable coreCommandTable = commandTableAssembling();
            logger.info("Registering bot");
            botsApi.registerBot(new TelegramBotCore(coreCommandTable, new KeyboardMarkupBuilder(coreCommandTable), dataManager));
            logger.info("Bot registered");
        } catch (TelegramApiException e) {
            logger.info("Bot registration failed with stacktrace: " + e.getStackTrace());
        }
    }



}
