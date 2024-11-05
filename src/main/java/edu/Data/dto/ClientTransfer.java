package edu.Data.dto;

import java.math.BigDecimal;
import java.sql.Date;
public record ClientTransfer(
            Long id,
            Long tgUserId,
            String phone,
            String name,
            Date userLastVisited,
            String vpnProfile,
            Boolean isVpnProfileAlive,
            Date expiredAt,
            boolean isInPaymentProcess,
            String paymentKey,
            BigDecimal balance
    ) {
    //Чтобы ничего не сломать, добавим ещё 2 конструктора
    public ClientTransfer(Long tgUserId, String phone, String name, Date userLastVisited, String vpnProfile, Boolean isVpnProfileAlive, Date expiredAt) {
        this(null, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt, false, "0", BigDecimal.ZERO);
    }
    public ClientTransfer(Long id, Long tgUserId, String phone, String name, Date userLastVisited, String vpnProfile, Boolean isVpnProfileAlive, Date expiredAt) {
        this(id, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt, false,"0", BigDecimal.ZERO);
    }
}
