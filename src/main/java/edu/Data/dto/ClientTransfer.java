package edu.Data.dto;


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
            boolean isInPaymentProcess
    ) {
    //Чтобы ничего не сломать, добавим ещё 2 конструктора
    public ClientTransfer(Long tgUserId, String phone, String name, Date userLastVisited, String vpnProfile, Boolean isVpnProfileAlive, Date expiredAt) {
        this(null, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt, false);
    }
    public ClientTransfer(Long id, Long tgUserId, String phone, String name, Date userLastVisited, String vpnProfile, Boolean isVpnProfileAlive, Date expiredAt) {
        this(id, tgUserId, phone, name, userLastVisited, vpnProfile, isVpnProfileAlive, expiredAt, false);
    }
}

