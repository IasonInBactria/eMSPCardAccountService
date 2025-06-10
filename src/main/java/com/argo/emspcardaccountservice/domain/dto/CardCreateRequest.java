package com.argo.emspcardaccountservice.domain.dto;


import lombok.Value;

@Value
public class CardCreateRequest {
    String rfidUid;
    String rfidVisibleNumber;
}
