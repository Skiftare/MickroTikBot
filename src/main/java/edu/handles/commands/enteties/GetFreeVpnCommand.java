package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.UserInfo;
import edu.Integrations.telegram.SubscriptionChecker;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Date;
import java.util.logging.Logger;

import static edu.Integrations.chr.RouterConnector.initialisationSecret;

public class GetFreeVpnCommand implements Command {
    private final DataManager dataManager;
    private static final String TELEGRAM_CHANNEL_ID = System.getenv("TELEGRAM_CHANNEL_ID");
    private static final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");

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
                String vpnProfile = initialisationSecret(clientTransfer);

                // Устанавливаем новый срок действия VPN на месяц
                Date newDateExpiredAt = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

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
                        clientTransfer.balance() // Баланс не меняется
                );

                dataManager.update(updatedClient);
                stringBuilder.append("Ваш VPN профиль успешно создан:\n").append(vpnProfile);
            } else {
                // Пользователь не подписан на канал
                stringBuilder.append("Для получения бесплатного VPN профиля подпишитесь на наш канал и повторите команду.");
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().info("Ошибка при выдаче VPN профиля: " + e.getMessage());
            stringBuilder.append("Произошла ошибка при выдаче VPN профиля. Пожалуйста, попробуйте позже.");
        }

        message.setText(stringBuilder.toString());
        return message;
    }

    /**
     * Проверяет, подписан ли пользователь на канал.
     */
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
        return status == UserProfileStatus.GUEST || status == UserProfileStatus.NO_VPN;
    }

    @Override
    public String getCommandName() {
        return "/get_free_vpn";
    }

    @Override
    public String getCommandDescription() {
        return "Выдаёт VPN профиль бесплатно, если пользователь подписан на канал.";
    }
}
