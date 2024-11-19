package edu;

import edu.Configuration.DataConnectConfigurator;
import edu.Configuration.KeyboardMarkupBuilder;
import edu.Configuration.TelegramBotCore;
import edu.Data.JdbcDataManager;
import edu.Data.formatters.UserProfileFormatter;
import edu.Integrations.wallet.ctrypto.stellar.AccountListener;
import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import edu.handles.commands.Command;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.GetFreeVpnCommand;
import edu.handles.commands.enteties.UserProfileCommand;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.AuthentificateCommand;
import edu.handles.commands.enteties.BuyConnectionCommand;
import edu.handles.commands.enteties.GetUserProfileCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.commands.enteties.ProfileCommand;
import edu.handles.commands.enteties.StateCommand;
import edu.handles.commands.enteties.RegisterCommand;
import edu.handles.tables.CommandTable;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.Logger;


@SuppressWarnings({"HideUtilityClassConstructor"})
public class BotApplication {
    private static final Logger LOGGER = Logger.getLogger(BotApplication.class.getName());

    private static DataConnectConfigurator dataConnectionAssembling() {
        LOGGER.info("Assembling data connection");
        DataConnectConfigurator dataConnection = new DataConnectConfigurator();

        LOGGER.info("Data connection assembled");
        return dataConnection;
    }

    private static CommandTable commandTableAssembling(JdbcDataManager jdbcDataManager) {
        LOGGER.info("Assembling command table");
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        Command registerCommand = new RegisterCommand(jdbcDataManager);
        Command authentificateCommand = new AuthentificateCommand(jdbcDataManager);
        Command stateCommand = new StateCommand();
        Command profileCommand = new ProfileCommand();
        Command userProfileCommand = new UserProfileCommand(jdbcDataManager);
        Command buyCommand = new BuyConnectionCommand(jdbcDataManager);
        Command getUserProfileCommand = new GetUserProfileCommand(jdbcDataManager, new UserProfileFormatter());
        Command getFreeVpnCommand = new GetFreeVpnCommand(jdbcDataManager);
        AccountListener accountListener = new AccountListener(new StellarConnection(),
                jdbcDataManager
        );
        accountListener.startListening();


        CommandTable preCommandTable = new CommandTable(infoCommand,
                authorsCommand, registerCommand,
                authentificateCommand, stateCommand,
                profileCommand, userProfileCommand,
                buyCommand, getUserProfileCommand,
                getFreeVpnCommand
        );
        Command helpCommand = new HelpCommand(preCommandTable);
        LOGGER.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand);

    }


    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Сборка бота
            //TODO: стоит ли всё в одном try хранить, или лучше растащить процесс сборки и получше прологировать его?
            DataConnectConfigurator dataConnection = dataConnectionAssembling();

            JdbcDataManager jdbcDataManager = new JdbcDataManager(dataConnection);
            CommandTable coreCommandTable = commandTableAssembling(jdbcDataManager);
            KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(coreCommandTable);

            LOGGER.info("Registering bot");
            botsApi.registerBot(new TelegramBotCore(coreCommandTable, keyboardMarkupBuilder, jdbcDataManager));
            LOGGER.info("Bot registered");
        } catch (TelegramApiException e) {
            LOGGER.info("Bot registration failed with stacktrace: " + e.getStackTrace());
        }
    }
}
