package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import edu.Integrations.chr.RouterConnector;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.logging.Logger;

import static edu.Integrations.chr.RouterConnector.initialisationSecret;
import static edu.Integrations.chr.RouterConnector.prolongSecret;
import static edu.utility.Constants.CONNECTION_PRICE;
import static edu.utility.Constants.PUBLIC_ADDRESS;

public class BuyConnectionCommand implements Command {
    private DataManager dataManager;

    public BuyConnectionCommand(DataManager incomingDataManager) {
        dataManager = incomingDataManager;
    }

    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        UserInfo userInfo = dataManager.getInfoById(update.getMessage().getChatId());
        ClientTransfer clientTransfer = userInfo.client();
        StringBuilder stringBuilder = new StringBuilder();

        if (clientTransfer.balance().compareTo(CONNECTION_PRICE) <= 0) {
            stringBuilder.append("У вас недостаточно средств для покупки подключения к интернету").append("\n");
            stringBuilder.append("Ваш баланс: ").append(clientTransfer.balance()).append("\n");
            stringBuilder.append("Кошелек для пополнения: ").append(PUBLIC_ADDRESS).append("\n");
            stringBuilder.append("Комментарий для идентификации платежа: ").append(clientTransfer.paymentKey()).append("\n");
        } else {
            String vpnProfile = "TEST";
            Date newDateExpiredAt;
            BigDecimal newBalance;
            if (userInfo.status() == UserProfileStatus.NO_VPN) {
                //Покупаем подключение
                newBalance = clientTransfer.balance().subtract(CONNECTION_PRICE);

                try {
                    vpnProfile = initialisationSecret(clientTransfer);
                }
                catch (Exception e) {
                    Logger.getAnonymousLogger().info("Не удалось продлить подключение");
                }
                newDateExpiredAt = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
            } else {
                //Продляем на месяц
                try {
                    vpnProfile = prolongSecret(clientTransfer);
                }
                catch (Exception e) {
                    Logger.getAnonymousLogger().info("Не удалось продлить подключение");
                }
                newBalance = clientTransfer.balance().subtract(CONNECTION_PRICE);
                newDateExpiredAt = new Date(clientTransfer.expiredAt().getTime() + 30L * 24 * 60 * 60 * 1000);
            }
            if (vpnProfile.startsWith("!Не удалось установить")) {
                stringBuilder = new StringBuilder(vpnProfile);
            } else {
                ClientTransfer updatedClient = new ClientTransfer(
                        clientTransfer.id(),
                        clientTransfer.tgUserId(),
                        clientTransfer.phone(),
                        clientTransfer.name(),
                        clientTransfer.userLastVisited(),
                        vpnProfile,
                        true,
                        newDateExpiredAt,
                        clientTransfer.isInPaymentProcess(),
                        clientTransfer.paymentKey(),
                        newBalance
                );
                dataManager.update(updatedClient);
            }
            stringBuilder.append(vpnProfile);
        }
        message.setText(stringBuilder.toString());
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return status == UserProfileStatus.ACTIVE_VPN || status == UserProfileStatus.NO_VPN;
    }

    @Override
    public String getCommandName() {
        return "/buy_connection";
    }

    @Override
    public String getCommandDescription() {
        return "Позволяет купить подключение к интернету";
    }
}
