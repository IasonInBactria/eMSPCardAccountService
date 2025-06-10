package com.argo.emspcardaccountservice.controller;


import com.argo.emspcardaccountservice.domain.dto.AccountCreateRequest;
import com.argo.emspcardaccountservice.domain.dto.AccountResponse;
import com.argo.emspcardaccountservice.domain.dto.AccountStatusUpdateRequest;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.argo.emspcardaccountservice.service.AccountService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        Account account = accountService.createAccount(request.getEmail());
        return new ResponseEntity<>(AccountResponse.from(account), HttpStatus.CREATED);
    }

    @PatchMapping("/{email}/status")
    public ResponseEntity<AccountResponse> changeAccountStatus(
            @PathVariable String email,
            @Valid @RequestBody AccountStatusUpdateRequest request) {
        Account account = accountService.changeAccountStatus(email, request.getNewStatus());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping("/{email}")
    public ResponseEntity<AccountResponse> getAccountByEmail(@PathVariable String email) {
        Account account = accountService.getAccountByEmail(email);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> queryAccounts(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant lastUpdatedStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant lastUpdatedEnd,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Account> accountsPage = accountService.queryAccountsAndCards(
                page, size);
        // 手动将Account列表转为AccountResponse列表，并构造新的PageResponse
        List<AccountResponse> responseList = accountsPage.getRecords().stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());

        Page<AccountResponse> responsePage = new Page<>();
        responsePage.setRecords(responseList);
        responsePage.setCurrent(accountsPage.getCurrent());
        responsePage.setSize(accountsPage.getSize());
        responsePage.setTotal(accountsPage.getTotal());
        responsePage.setPages(accountsPage.getPages());

        return ResponseEntity.ok(responsePage);
    }

}
