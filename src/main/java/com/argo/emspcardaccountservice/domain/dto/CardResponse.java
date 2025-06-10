package com.argo.emspcardaccountservice.domain.dto;

import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.Instant;

@Value
public class CardResponse {
    Long id;
    String contractId;
    String rfidUid;
    String rfidVisibleNumber;
    CardStatus status;
    Long accountId; // 直接返回 accountId 字符串
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Instant lastUpdated;

    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getId(),
                card.getContractId(),
                card.getRfidUid(),
                card.getRfidVisibleNumber(),
                card.getStatus(),
                card.getAccountId(), // 直接获取 accountId
                card.getCreatedAt(),
                card.getLastUpdated()
        );
    }
}
