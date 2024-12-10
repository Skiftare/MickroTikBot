package edu.handles.commands.enteties;

import java.math.BigDecimal;
import java.sql.Date;

import edu.Data.formatters.UserProfileFormatter;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.UserMessageFromBotWrapper;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;

import static edu.Data.formatters.EncryptionUtil.encrypt;
import static edu.Integrations.chr.RouterConnector.initialisationSecret;
import static edu.Integrations.chr.RouterConnector.prolongSecret;

import edu.handles.commands.Command;
import edu.models.UserProfileStatus;

import static edu.utility.Constants.CONNECTION_PRICE;
import static edu.utility.Constants.MONTH_LENGTH_IN_MILLISECONDS;
import static edu.utility.Constants.PUBLIC_ADDRESS;

public class BuyConnectionCommand implements Command {
    private final DataManager dataManager;

    public BuyConnectionCommand(DataManager incomingDataManager) {
        dataManager = incomingDataManager;
    }

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {
        Long chatId = update.userId();
        UserInfo userInfo = dataManager.getInfoById(chatId);
        ClientTransfer clientTransfer = userInfo.client();

        StringBuilder stringBuilder = new StringBuilder();

        // Проверяем достаточно ли средств
        if (clientTransfer.balance().compareTo(CONNECTION_PRICE) <= 0) {
            return handleInsufficientFunds(clientTransfer);
        }

        try {
            // Переводим средства на удержание
            ClientTransfer clientWithHeldBalance = holdFunds(clientTransfer);
            String vpnProfile = "TEST";
            Date newDateExpiredAt = clientWithHeldBalance.expiredAt();

            if (update.status() == UserProfileStatus.ACTIVE_VPN) {
                prolongSecret(clientWithHeldBalance);
                newDateExpiredAt = new Date(clientTransfer.expiredAt().getTime() + MONTH_LENGTH_IN_MILLISECONDS);
            } else {
                vpnProfile = initialisationSecret(clientWithHeldBalance);
                newDateExpiredAt = new Date(System.currentTimeMillis() + MONTH_LENGTH_IN_MILLISECONDS);
            }


            // Если операция успешна, списываем удержанные средства и обновляем профиль
            if (!vpnProfile.startsWith("!Не удалось установить")) {
                vpnProfile = encrypt(vpnProfile);

                takeFundsToCompanyBalance(clientWithHeldBalance, newDateExpiredAt);

                stringBuilder.append(vpnProfile);
                String responseText = stringBuilder.toString();
                String responseMessage = UserProfileFormatter.formatCredentialsForConnection(responseText);
                return new BotResponseToUserWrapper(chatId, responseMessage, true, null);
            } else {
                // Если операция не удалась, возвращаем средства
                releaseFunds(clientWithHeldBalance);
                return new BotResponseToUserWrapper(chatId, vpnProfile, true, null);
            }
        } catch (Exception e) {
            // В случае ошибки возвращаем средства
            releaseFunds(clientTransfer);
            throw new RuntimeException("Ошибка при обработке платежа", e);
        }
    }

    private BotResponseToUserWrapper handleInsufficientFunds(ClientTransfer clientTransfer) {
        final String endlWithMarkdown = "`\n";

        String stringBuilder = "У вас недостаточно средств для покупки подключения к интернету\n"
                + "Ваш баланс: " + clientTransfer.balance() + "\n"
                + "Кошелек для пополнения: `" + PUBLIC_ADDRESS + endlWithMarkdown
                + "Комментарий для идентификации платежа: `"
                + clientTransfer.paymentKey() + endlWithMarkdown;
        return new BotResponseToUserWrapper(clientTransfer.tgUserId(), stringBuilder, true, null);
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
