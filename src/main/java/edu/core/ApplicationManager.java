package edu.core;

import edu.Configuration.TelegramBotCore;
import edu.Data.JdbcDataManager;
import edu.Data.PaymentDataManager;
import edu.Integrations.wallet.ctrypto.stellar.AccountListener;

public class ApplicationManager {
    //Самый главный класс.
    //Сюда мы прикручиваем Bot с одной стороны
    //Payment с другой
    //Listener с третьей
    TelegramBotCore bot;
    JdbcDataManager dataManager;
    PaymentDataManager paymentDataManager;
    AccountListener stellarAccountListener;

    ApplicationManager(TelegramBotCore bot,
                       JdbcDataManager dataManager,
                       PaymentDataManager paymentDataManager,
                       AccountListener stellarAccountListener) {
        this.bot = bot;
        this.dataManager = dataManager;
        this.paymentDataManager = paymentDataManager;
        this.stellarAccountListener = stellarAccountListener;

    }
}
