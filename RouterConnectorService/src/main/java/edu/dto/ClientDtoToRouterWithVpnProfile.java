package edu.dto;

import jakarta.validation.constraints.NotNull;

public record ClientDtoToRouterWithVpnProfile(
        @NotNull Long tgUserId,
        @NotNull String vpnProfile) {
    public ClientDtoToRouterWithVpnProfile(RouterProtos.ClientRequestWithProlongationSecret clientRequest) {
        this(clientRequest.getTgUserId(), clientRequest.getProlongationSecret());
    }
}
