package edu.models;


public enum UserProfileStatus {
    GUEST,         // Гость, еще не зарегистрировался
    UNCONFIRMED,   // Зарегистрировался, но не подтвердил телефон
    ACTIVE_VPN,    // Подтвердил телефон, активный VPN-профиль
    NO_VPN         // Подтвердил телефон, но VPN-профиль не активен
}
