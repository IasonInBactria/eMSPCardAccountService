package com.argo.emspcardaccountservice.service.impl;

import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import com.argo.emspcardaccountservice.domain.exception.DuplicateResourceException;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.argo.emspcardaccountservice.domain.exception.ResourceNotFoundException;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.argo.emspcardaccountservice.domain.pojo.RFID;
import com.argo.emspcardaccountservice.mapper.AccountMapper;
import com.argo.emspcardaccountservice.mapper.CardMapper;
import com.argo.emspcardaccountservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public Card createCard(String rfidUid, String rfidVisibleNumber) {
        if (cardMapper.existsByRfidUid(rfidUid)) {
            throw new DuplicateResourceException("Card", "RFID UID", rfidUid);
        }
        Card card = new Card(new RFID(rfidUid, rfidVisibleNumber));
        cardMapper.insert(card);
        return card;
    }

    @Override
    @Transactional
    public Card assignCardToAccount(String contractId, String accountEmail) {
        Optional<Card> optionalCard = cardMapper.findByContractIdValue(contractId);
        Card card = optionalCard
                .orElseThrow(() -> new ResourceNotFoundException("Card", contractId));

        if (card.getAccountId() != null) { // 检查卡片是否已分配
            throw new IllegalStateException("Card with contractId '" + contractId + "' is already assigned to an account.");
        }

        Optional<Account> optionalAccount = accountMapper.findByEmail(accountEmail);
        Account account = optionalAccount
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountEmail));

        card.assignToAccount(account);
        cardMapper.updateById(card);
        return card;
    }

    @Override
    @Transactional
    public Card changeCardStatus(String contractId, CardStatus newStatus) {
        Optional<Card> optionalCard = cardMapper.findByContractIdValue(contractId);
        Card card = optionalCard
                .orElseThrow(() -> new ResourceNotFoundException("Card", contractId));

        CardStatus oldStatus = card.getStatus();
        if (!CardStatus.isValidTransition(oldStatus, newStatus)) {
            throw new InvalidStatusTransitionException("Card", oldStatus.name(), newStatus.name());
        }

        switch (newStatus) {
            case ACTIVATED:
                card.activate();
                break;
            case DEACTIVATED:
                card.deactivate();
                break;
            case CREATED:
                card.removeCardFromAccount(); // 如果从其他状态回到 CREATED，可能意味着解绑
                break;
            case ASSIGNED:
                // ASSIGNED 状态通常由 assignCardToAccount 设置
                break;
            default:
                throw new IllegalArgumentException("Unsupported card status: " + newStatus);
        }
        cardMapper.updateById(card); // MyBatis-Plus 更新操作
        return card;
    }

    @Override
    @Transactional(readOnly = true)
    public Card getCardByContractId(String contractId) {
        return cardMapper.findByContractIdValue(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", contractId));
    }
}
