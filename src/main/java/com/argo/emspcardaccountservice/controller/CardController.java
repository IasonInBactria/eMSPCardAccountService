package com.argo.emspcardaccountservice.controller;

import com.argo.emspcardaccountservice.domain.dto.CardAssignRequest;
import com.argo.emspcardaccountservice.domain.dto.CardCreateRequest;
import com.argo.emspcardaccountservice.domain.dto.CardResponse;
import com.argo.emspcardaccountservice.domain.dto.CardStatusUpdateRequest;
import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.argo.emspcardaccountservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {
        Card card = cardService.createCard(request.getRfidUid(), request.getRfidVisibleNumber());
        return new ResponseEntity<>(CardResponse.from(card), HttpStatus.CREATED);
    }

    @PatchMapping("/{contractId}/assign")
    public ResponseEntity<CardResponse> assignCardToAccount(
            @PathVariable String contractId,
            @Valid @RequestBody CardAssignRequest request) {
        Card card = cardService.assignCardToAccount(contractId, request.getAccountEmail());
        return ResponseEntity.ok(CardResponse.from(card));
    }

    @PatchMapping("/{contractId}/status")
    public ResponseEntity<CardResponse> changeCardStatus(
            @PathVariable String contractId,
            @Valid @RequestBody CardStatusUpdateRequest request) {
        Card card = cardService.changeCardStatus(contractId, request.getNewStatus());
        return ResponseEntity.ok(CardResponse.from(card));
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<CardResponse> getCardByContractId(@PathVariable String contractId) {
        Card card = cardService.getCardByContractId(contractId);
        return ResponseEntity.ok(CardResponse.from(card));
    }
}
