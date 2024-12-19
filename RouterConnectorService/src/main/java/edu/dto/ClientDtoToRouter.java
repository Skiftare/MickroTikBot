package edu.dto;

import jakarta.validation.constraints.NotNull;

public record ClientDtoToRouter(
        @NotNull Long tgUserId) {
    public ClientDtoToRouter(RouterProtos.ClientRequest clientRequest) {
        this(clientRequest.getTgUserId());
    }
}
