package com.argo.emspcardaccountservice.domain.dto;

import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import lombok.Value;

@Value
public class CardStatusUpdateRequest {

    CardStatus newStatus;
}
