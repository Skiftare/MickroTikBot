package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import edu.Data.formatters.UserProfileFormatter;
import edu.Integrations.telegram.SubscriptionChecker;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.sql.Date;
import java.util.logging.Logger;
import static edu.Data.formatters.EncryptionUtil.encrypt;
import static edu.Integrations.chr.RouterConnector.initialisationTrial;
import static edu.utility.Constants.DAY_LENGTH_IN_MILLISECONDS;

public class GetFreeVpnCommand implements Command {
    private static final String TELEGRAM_CHANNEL_ID = System.getenv("TELEGRAM_CHANNEL_ID");
    private static final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private final DataManager dataManager;

    public GetFreeVpnCommand(DataManager incomingDataManager) {
        this.dataManager = incomingDataManager;
    }

    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());

        // Получаем информацию о пользователе
        UserInfo userInfo = dataManager.getInfoById(update.getMessage().getChatId());
        ClientTransfer clientTransfer = userInfo.client();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Проверка подписки на канал
            if (isUserSubscribedToChannel(update.getMessage().getFrom().getId())) {
                // Пользователь подписан на канал, выдаем VPN профиль
                String vpnProfile = initialisationTrial(clientTransfer);

                // Устанавливаем новый срок действия VPN на день
                Date newDateExpiredAt = new Date(System.currentTimeMillis() + DAY_LENGTH_IN_MILLISECONDS);

                String encryptedVpnProfile = encrypt(vpnProfile);
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
                        clientTransfer.balance() // Баланс не меняется
                );

                dataManager.update(updatedClient);
                stringBuilder.append("Ваш пробный VPN профиль успешно создан:\n").append(vpnProfile);
            } else {
                // Пользователь не подписан на канал
                stringBuilder.append("Для получения пробного VPN профиля подпишитесь на наш канал "
                        + "(https://t.me/MikroTikBotTGC) "
                        + "и повторите команду.");
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().info("Ошибка при выдаче VPN профиля: " + e.getMessage());
            stringBuilder.append("Произошла ошибка при выдаче VPN профиля. Пожалуйста, попробуйте позже.");
        }
        String responseText = stringBuilder.toString();
        String markdownWrappedTest = UserProfileFormatter.formatCredentialsForConnection(responseText);
        message.setText(markdownWrappedTest);
        message.enableMarkdown(true);
        return message;
    }

    private boolean isUserSubscribedToChannel(Long userId) {
        SubscriptionChecker subscriptionChecker = new SubscriptionChecker(BOT_TOKEN);
        try {
            // Используем метод для проверки подписки на канал
            return subscriptionChecker.isUserSubscribed(userId, TELEGRAM_CHANNEL_ID);
        } catch (Exception e) {
            // Логируем ошибку, если что-то пошло не так
            Logger.getAnonymousLogger().warning("Ошибка при проверке подписки на канал: " + e.getMessage());
            // В случае ошибки считаем, что пользователь не подписан
            return false;
        }
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return true;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return status == UserProfileStatus.NO_VPN;
    }

    @Override
    public String getCommandName() {
        return "/try_vpn";
    }

    @Override
    public String getCommandDescription() {
        return "Выдаёт VPN профиль бесплатно на пробный период в 1 день, если пользователь подписан на канал.";
    }
}
