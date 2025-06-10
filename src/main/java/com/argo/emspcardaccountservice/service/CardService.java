package com.argo.emspcardaccountservice.service;

import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import com.argo.emspcardaccountservice.domain.pojo.Card;

public interface CardService {

    Card createCard(String rfidUid, String rfidVisibleNumber);
    Card assignCardToAccount(String contractId, String accountEmail);
    Card changeCardStatus(String contractId, CardStatus newStatus);
    Card getCardByContractId(String contractId);
}
