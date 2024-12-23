package edu;

import edu.Configuration.DataConnectConfigurator;
import edu.Configuration.KeyboardMarkupBuilder;
import edu.Configuration.TelegramBotCore;
import edu.Data.JdbcDataManager;
import edu.Data.formatters.UserProfileFormatter;
import edu.Integrations.chr.RouterGrpcConnector;
import edu.Integrations.wallet.ctrypto.stellar.AccountListener;
import edu.Integrations.wallet.ctrypto.stellar.StellarConnection;
import edu.handles.commands.Command;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.GetFreeVpnCommand;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.AuthentificateCommand;
import edu.handles.commands.enteties.BuyConnectionCommand;
import edu.handles.commands.enteties.GetUserProfileCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.commands.enteties.ProfileCommand;
import edu.handles.commands.enteties.RegisterCommand;
import edu.handles.tables.CommandTable;
import edu.startup.HeldFundsReleaser;
import io.grpc.NameResolverRegistry;
import io.grpc.internal.DnsNameResolverProvider;
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
        // Регистрация DNS-резолвера
        NameResolverRegistry.getDefaultRegistry().register(new DnsNameResolverProvider());
        Logger.getAnonymousLogger().info("DNS NameResolver registered");

        RouterGrpcConnector routerGrpcConnector = new RouterGrpcConnector("dns:///RouterConnector:8090");

        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        Command registerCommand = new RegisterCommand(jdbcDataManager);
        Command authentificateCommand = new AuthentificateCommand(jdbcDataManager);
        Command profileCommand = new ProfileCommand(routerGrpcConnector);
        Command buyCommand = new BuyConnectionCommand(jdbcDataManager, routerGrpcConnector);
        Command getUserProfileCommand = new GetUserProfileCommand(jdbcDataManager, new UserProfileFormatter());
        Command getFreeVpnCommand = new GetFreeVpnCommand(jdbcDataManager, routerGrpcConnector);
        AccountListener accountListener = new AccountListener(new StellarConnection(
                System.getenv("STELLAR_PUBLIC_KEY"),
                System.getenv("STELLAR_SECRET_KEY"),
                System.getenv("STELLAR_NETWORK")
        ),
                jdbcDataManager
        );
        accountListener.startListening();


        CommandTable preCommandTable = new CommandTable(infoCommand,
                authorsCommand, registerCommand,
                authentificateCommand,
                profileCommand,
                buyCommand, getUserProfileCommand,
                getFreeVpnCommand
        );
        Command helpCommand = new HelpCommand(preCommandTable);
        LOGGER.info("Command table assembled");
        return new CommandTable(preCommandTable, helpCommand);

    }

    private static void releaseHeldFunds(JdbcDataManager jdbcDataManager) {
        LOGGER.info("Releasing held funds");
        HeldFundsReleaser fundsReleaser = new HeldFundsReleaser(jdbcDataManager);
        fundsReleaser.releaseAllHeldFunds();
        LOGGER.info("Held funds released");
    }


    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Сборка бота
            DataConnectConfigurator dataConnection = dataConnectionAssembling();

            JdbcDataManager jdbcDataManager = new JdbcDataManager(dataConnection);

            releaseHeldFunds(jdbcDataManager);
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
