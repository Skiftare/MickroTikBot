package edu;

import edu.Configuration.DataConnectConfigurator;
import edu.Configuration.KeyboardMarkupBuilder;
import edu.Configuration.TelegramBotCore;
import edu.Data.JdbcDataManager;
import edu.Integrations.wallet.ctrypto.stellar.AccountListener;
import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import edu.handles.commands.Command;
import edu.handles.commands.enteties.*;
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

    private static CommandTable commandTableAssembling(JdbcDataManager jdbcDataManager) {
        logger.info("Assembling command table");
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        Command registerCommand = new RegisterCommand(jdbcDataManager);
        Command authentificateCommand = new AuthentificateCommand(jdbcDataManager);
        Command stateCommand = new StateCommand();
        Command profileCommand = new ProfileCommand();
        Command getProfileCommand = new GetProfileCommand(jdbcDataManager);
        Command checkPayment = new CheckPayment(jdbcDataManager);
        Command userProfileCommand = new UserProfileCommand(jdbcDataManager);

        AccountListener accountListener = new AccountListener(new StellarConnection());
        accountListener.startListening();


        CommandTable preCommandTable = new CommandTable(infoCommand,
                authorsCommand, registerCommand,
                getProfileCommand, checkPayment,
                authentificateCommand, stateCommand,
                profileCommand, userProfileCommand
        );
        Command helpCommand = new HelpCommand(preCommandTable);
        logger.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand);

    }


    public static void main(String[] args) throws TelegramApiException {
        var c = "2";
        var w = 2;
        System.out.println(c == String.valueOf(w)); // false
        System.out.println(c.equals(String.valueOf(w))); // true
        System.out.println(Integer.parseInt(c)==w); // true
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Сборка бота
            //TODO: стоит ли всё в одном try хранить, или лучше растащить процесс сборки и получше прологировать его?
            DataConnectConfigurator dataConnection = dataConnectionAssembling();
            JdbcDataManager jdbcDataManager = new JdbcDataManager(dataConnection);
            CommandTable coreCommandTable = commandTableAssembling(jdbcDataManager);
            KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(coreCommandTable);

            logger.info("Registering bot");
            botsApi.registerBot(new TelegramBotCore(coreCommandTable, keyboardMarkupBuilder, jdbcDataManager));
            logger.info("Bot registered");
        } catch (TelegramApiException e) {
            logger.info("Bot registration failed with stacktrace: " + e.getStackTrace());
        }
    }
}
