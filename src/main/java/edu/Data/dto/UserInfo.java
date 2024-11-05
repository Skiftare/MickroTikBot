package edu.Data.dto;


import java.sql.Date;

import edu.models.UserProfileStatus;


public record UserInfo(ClientTransfer client, UserProfileStatus status) {}
