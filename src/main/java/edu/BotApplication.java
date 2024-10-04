package edu;

import edu.Configuration.DataConnectConfigurator;
import edu.Configuration.KeyboardMarkupBuilder;
import edu.Configuration.TelegramBotCore;
import edu.Data.DataManager;
import edu.handles.commands.Command;
import edu.handles.commands.enteties.AuthentificateCommand;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.commands.enteties.RegisterCommand;
import edu.handles.commands.enteties.StateCommand;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.tables.CommandTable;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.Logger;


@SuppressWarnings("HideUtilityClassConstructor")
public class BotApplication {
    private static Logger logger = Logger.getLogger(BotApplication.class.getName());

    private static DataConnectConfigurator dataConnectionAssembling() {
        logger.info("Assembling data connection");
        DataConnectConfigurator dataConnection = new DataConnectConfigurator();

        logger.info("Data connection assembled");
        return dataConnection;
    }

    private static CommandTable commandTableAssembling(DataManager dataManager) {
        logger.info("Assembling command table");
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        Command registerCommand = new RegisterCommand(dataManager);
        Command authentificateCommand = new AuthentificateCommand(dataManager);
        Command stateCommand = new StateCommand();

        CommandTable preCommandTable = new CommandTable(infoCommand, authorsCommand, registerCommand);
        Command helpCommand = new HelpCommand(preCommandTable);
        logger.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand, authentificateCommand, stateCommand);

    }


    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            // Сборка бота
            //TODO: стоит ли всё в одном try хранить, или лучше растащить процесс сборки и получше прологировать его?
            DataConnectConfigurator dataConnection = dataConnectionAssembling();
            DataManager dataManager = new DataManager(dataConnection);
            CommandTable coreCommandTable = commandTableAssembling(dataManager);
            KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(coreCommandTable);

            logger.info("Registering bot");
            botsApi.registerBot(new TelegramBotCore(coreCommandTable, keyboardMarkupBuilder, dataManager));
            logger.info("Bot registered");
        } catch (TelegramApiException e) {
            logger.info("Bot registration failed with stacktrace: " + e.getStackTrace());
        }
    }
}
