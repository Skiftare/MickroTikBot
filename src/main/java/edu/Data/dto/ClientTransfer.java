package edu.Data.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Запись, представляющая данные о клиенте.
 *
 * @param id уникальный идентификатор клиента
 * @param tgUserId идентификатор пользователя в Telegram
 * @param phone номер телефона клиента
 * @param name имя клиента
 * @param userLastVisited дата последнего посещения
 * @param vpnProfile профиль VPN клиента
 * @param isVpnProfileAlive статус активности VPN профиля
 * @param expiredAt дата истечения срока действия VPN
 * @param isInPaymentProcess флаг процесса оплаты
 * @param paymentKey ключ платежа
 * @param balance баланс клиента
 * @param heldBalance заблокированный баланс клиента
 */
public record ClientTransfer(Long id,
                           Long tgUserId,
                           String phone,
                           String name,
                           Date userLastVisited,
                           String vpnProfile,
                           Boolean isVpnProfileAlive,
                           Date expiredAt,
                           boolean isInPaymentProcess,
                           String paymentKey,
                           BigDecimal balance,
                           BigDecimal heldBalance) {

    /**
     * Конструктор для создания записи о клиенте без ID.
     *
     * @param tgUserId идентификатор пользователя Telegram
     * @param phone номер телефона
     * @param name имя клиента
     * @param userLastVisited дата последнего посещения
     * @param vpnProfile профиль VPN
     * @param isVpnProfileAlive статус активности VPN профиля
     * @param expiredAt дата истечения действия
     */
    public ClientTransfer(Long tgUserId,
                         String phone,
                         String name,
                         Date userLastVisited,
                         String vpnProfile,
                         Boolean isVpnProfileAlive,
                         Date expiredAt) {
        this(null, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt,
             false, "0", BigDecimal.ZERO, BigDecimal.ZERO);
    }

    /**
     * Конструктор для создания записи о клиенте с ID.
     *
     * @param id идентификатор клиента
     * @param tgUserId идентификатор пользователя Telegram
     * @param phone номер телефона
     * @param name имя клиента
     * @param userLastVisited дата последнего посещения
     * @param vpnProfile профиль VPN
     * @param isVpnProfileAlive статус активности VPN профиля
     * @param expiredAt дата истечения действия
     */
    @SuppressWarnings("ParameterNumber")
    public ClientTransfer(Long id,
                         Long tgUserId,
                         String phone,
                         String name,
                         Date userLastVisited,
                         String vpnProfile,
                         Boolean isVpnProfileAlive,
                         Date expiredAt) {
        this(id, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt,
             false, "0", BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @SuppressWarnings("ParameterNumber")
    public ClientTransfer(Long id,
    Long tgUserId,
    String phone,
    String name,
    Date userLastVisited,
    String vpnProfile,
    Boolean isVpnProfileAlive,
    Date expiredAt,
    boolean isInPaymentProcess,
    String paymentKey,
    BigDecimal balance) {
     this(id, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt, isInPaymentProcess,
             paymentKey, balance, BigDecimal.ZERO);
    }
}
