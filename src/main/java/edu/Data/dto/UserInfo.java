package edu.Data.dto;


import edu.models.UserProfileStatus;


public record UserInfo(ClientTransfer client, UserProfileStatus status) {
}
