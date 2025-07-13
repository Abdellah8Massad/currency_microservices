package com.message.message.dto;

public record AccountsMsgDto(
                Long accountId,
                String accountName,
                String accountType,
                String accountStatus) {
}