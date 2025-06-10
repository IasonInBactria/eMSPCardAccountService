package com.argo.emspcardaccountservice.domain.dto;

import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class AccountResponse {
    Long id;
    String email;
    AccountStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Instant lastUpdated;
    List<CardResponse> cards; // 包含关联的卡片列表

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getLastUpdated(),
                account.getCards() != null ?
                        account.getCards().stream().map(CardResponse::from).collect(Collectors.toList()) :
                        Collections.emptyList() // 如果没有卡片，返回空列表
        );
    }
}
