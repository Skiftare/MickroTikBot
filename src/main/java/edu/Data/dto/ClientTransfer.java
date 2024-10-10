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
            Date expiredAt
    ) {

    }

