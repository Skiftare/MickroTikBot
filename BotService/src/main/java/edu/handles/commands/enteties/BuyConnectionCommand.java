package edu.handles.commands.enteties;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.logging.Logger;

import edu.Data.formatters.UserProfileFormatter;
import edu.Integrations.chr.RouterGrpcConnector;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.UserMessageFromBotWrapper;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;

import static edu.Data.formatters.EncryptionUtil.encrypt;

import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.apache.commons.logging.Log;

import static edu.utility.Constants.CONNECTION_PRICE;
import static edu.utility.Constants.MONTH_LENGTH_IN_MILLISECONDS;
import static edu.utility.Constants.PUBLIC_ADDRESS;


public class BuyConnectionCommand implements Command {
    private final DataManager dataManager;
    private static final String INSUFFICIENT_FUNDS_IMAGE_URL = "https://s1.hostingkartinok.com/uploads/images/2024/12/ead747c68f60511d1819838f07811359.jpg";
    private static final String SUCCESS_IMAGE_URL = "https://s1.hostingkartinok.com/uploads/images/2024/12/323a6e17d6fa670a2b2e93fe22dca455.jpg";
    private final RouterGrpcConnector routerGrpcConnector;

    public BuyConnectionCommand(DataManager incomingDataManager, RouterGrpcConnector routerGrpcConnector) {
        dataManager = incomingDataManager;
        this.routerGrpcConnector = routerGrpcConnector;
    }

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {
        Long chatId = update.userId();
        UserInfo userInfo = dataManager.getInfoById(chatId);
        ClientTransfer clientTransfer = userInfo.client();
        String plainTextMarkdownFormatter = "`";
        String startCodeHtmlFormatter = "<code>";
        String endCodeHtmlFormatter = "</code>";
        String endOfString = "\n";

        StringBuilder stringBuilder = new StringBuilder();

        // Проверяем достаточно ли средств
        if (clientTransfer.balance().compareTo(CONNECTION_PRICE) <= 0) {
            stringBuilder.append("У вас недостаточно средств для покупки подключения к интернету").append("\n");
            stringBuilder.append("Ваш баланс: ").append(clientTransfer.balance()).append(endOfString);
            stringBuilder.append("Кошелек для пополнения: ")
                    .append(startCodeHtmlFormatter).append(PUBLIC_ADDRESS)
                    .append(endCodeHtmlFormatter).append(endOfString);
            stringBuilder.append("Комментарий для идентификации платежа: ")
                    .append(startCodeHtmlFormatter)
                    .append(clientTransfer.paymentKey())
                    .append(endCodeHtmlFormatter)
                    .append(endOfString);
            String responseMessage = stringBuilder.toString();
            return new BotResponseToUserWrapper(
                    chatId, responseMessage, true, null,
                    INSUFFICIENT_FUNDS_IMAGE_URL, null
            );
        }

        try {
            // Переводим средства на удержание
            ClientTransfer clientWithHeldBalance = holdFunds(clientTransfer);

            String vpnProfile = "TEST";
            Date newDateExpiredAt = clientWithHeldBalance.expiredAt();

            if (update.status() == UserProfileStatus.ACTIVE_VPN) {
                Logger.getAnonymousLogger().info("Prolonging");
                routerGrpcConnector.prolongSecret(
                        routerGrpcConnector.reformatDtoForGrpcTransfer(clientWithHeldBalance)
                );
                newDateExpiredAt = new Date(clientTransfer.expiredAt().getTime() + MONTH_LENGTH_IN_MILLISECONDS);
            } else {
                Logger.getAnonymousLogger().info("Initializing secret");
                vpnProfile = routerGrpcConnector.initialisationSecret(
                        routerGrpcConnector.reformatDtoForGrpcTransfer(clientWithHeldBalance)
                );
                newDateExpiredAt = new Date(System.currentTimeMillis() + MONTH_LENGTH_IN_MILLISECONDS);
            }


            // Если операция успешна, списываем удержанные средства и обновляем профиль
            if (!vpnProfile.startsWith("!Не удалось установить")) {
                vpnProfile = encrypt(vpnProfile);


                takeFundsToCompanyBalance(clientWithHeldBalance, newDateExpiredAt);

                stringBuilder.append(vpnProfile);
                String responseText = stringBuilder.toString();
                String responseMessage = UserProfileFormatter.formatCredentialsForConnection(responseText);
                return new BotResponseToUserWrapper(
                        update.userId(), responseMessage,
                        true, null, SUCCESS_IMAGE_URL, null
                );
            } else {
                // Если операция не удалась, возвращаем средства
                releaseFunds(clientWithHeldBalance);
                return new BotResponseToUserWrapper(chatId, vpnProfile);
            }
        } catch (Exception e) {
            // В случае ошибки возвращаем средства
            releaseFunds(clientTransfer);
            throw new RuntimeException("Ошибка при обработке платежа", e);
        }
    }

    private ClientTransfer holdFunds(ClientTransfer client) {
        BigDecimal newBalance = client.balance().subtract(CONNECTION_PRICE);
        BigDecimal newHeldBalance = client.heldBalance().add(CONNECTION_PRICE);

        ClientTransfer updatedClient = new ClientTransfer(
                client.id(), client.tgUserId(), client.phone(), client.name(),
                client.userLastVisited(), client.vpnProfile(), client.isVpnProfileAlive(),
                client.expiredAt(), client.isInPaymentProcess(), client.paymentKey(),
                newBalance, newHeldBalance
        );
        dataManager.update(updatedClient);
        return updatedClient;
    }

    private void releaseFunds(ClientTransfer client) {
        BigDecimal newBalance = client.balance().add(CONNECTION_PRICE);
        BigDecimal newHeldBalance = client.heldBalance().subtract(CONNECTION_PRICE);

        ClientTransfer updatedClient = new ClientTransfer(
                client.id(), client.tgUserId(), client.phone(), client.name(),
                client.userLastVisited(), client.vpnProfile(), client.isVpnProfileAlive(),
                client.expiredAt(), client.isInPaymentProcess(), client.paymentKey(),
                newBalance, newHeldBalance
        );
        dataManager.update(updatedClient);
    }

    private void takeFundsToCompanyBalance(ClientTransfer client, Date newDateExpiredAt) {
        BigDecimal newBalance = client.balance();
        BigDecimal newHeldBalance = client.heldBalance().subtract(CONNECTION_PRICE);
        ClientTransfer updatedClient = new ClientTransfer(
                client.id(), client.tgUserId(), client.phone(), client.name(),
                client.userLastVisited(), client.vpnProfile(), client.isVpnProfileAlive(),
                newDateExpiredAt, client.isInPaymentProcess(), client.paymentKey(),
                newBalance, newHeldBalance
        );
        dataManager.update(updatedClient);

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
