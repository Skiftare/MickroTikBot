package edu.handles.commands.enteties;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.logging.Logger;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import static edu.Data.formatters.EncryptionUtil.encrypt;
import static edu.Integrations.chr.RouterConnector.initialisationSecret;
import static edu.Integrations.chr.RouterConnector.prolongSecret;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import static edu.utility.Constants.CONNECTION_PRICE;
import static edu.utility.Constants.ERROR_AT_ADVANCING_VPN;
import static edu.utility.Constants.MONTH_LENGTH_IN_MILLISECONDS;
import static edu.utility.Constants.PUBLIC_ADDRESS;

public class BuyConnectionCommand implements Command {
    private final DataManager dataManager;

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
        String endOfString = "\n";
        String plainTextMarkdownFormatter = "`";

        if (clientTransfer.balance().compareTo(CONNECTION_PRICE) <= 0) {
            stringBuilder.append("У вас недостаточно средств для покупки подключения к интернету").append("\n");
            stringBuilder.append("Ваш баланс: ").append(clientTransfer.balance()).append(endOfString);
            stringBuilder.append("Кошелек для пополнения: ")
                    .append(plainTextMarkdownFormatter).append(PUBLIC_ADDRESS)
                    .append(plainTextMarkdownFormatter).append(endOfString);
            stringBuilder.append("Комментарий для идентификации платежа: ")
                    .append(plainTextMarkdownFormatter)
                    .append(clientTransfer.paymentKey()).append(plainTextMarkdownFormatter)
                    .append(endOfString);
        } else {
            String vpnProfile = "TEST";
            Date newDateExpiredAt;
            BigDecimal newBalance;
            if (userInfo.status() == UserProfileStatus.NO_VPN) {
                //Покупаем подключение
                newBalance = clientTransfer.balance().subtract(CONNECTION_PRICE);

                try {
                    vpnProfile = initialisationSecret(clientTransfer);
                } catch (Exception e) {
                    Logger.getAnonymousLogger().info(ERROR_AT_ADVANCING_VPN);
                }
                newDateExpiredAt = new Date(System.currentTimeMillis() + MONTH_LENGTH_IN_MILLISECONDS);
            } else {
                //Продляем на месяц
                try {
                    vpnProfile = prolongSecret(clientTransfer);
                } catch (Exception e) {
                    Logger.getAnonymousLogger().info(ERROR_AT_ADVANCING_VPN);
                }
                newBalance = clientTransfer.balance().subtract(CONNECTION_PRICE);
                newDateExpiredAt = new Date(clientTransfer.expiredAt().getTime() + MONTH_LENGTH_IN_MILLISECONDS);
            }
            if (vpnProfile.startsWith("!Не удалось установить")) {
                stringBuilder = new StringBuilder(vpnProfile);
            } else {
                String encryptedVpnProfile = null;
                try {
                    encryptedVpnProfile = encrypt(vpnProfile);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ClientTransfer updatedClient = new ClientTransfer(
                        clientTransfer.id(),
                        clientTransfer.tgUserId(),
                        clientTransfer.phone(),
                        clientTransfer.name(),
                        clientTransfer.userLastVisited(),
                        encryptedVpnProfile,
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
        message.enableMarkdown(true);
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
