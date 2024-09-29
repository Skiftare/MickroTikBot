package edu.Data.dto;

import java.sql.Date;


public record Dto_user(
        Long id,
        Long tgUserId,
        String phone,
        String name,
        String vpnProfile,
        Date expiredAt
) {

}