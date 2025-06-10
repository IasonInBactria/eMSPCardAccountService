package com.argo.emspcardaccountservice.service;

import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface AccountService {
    Account createAccount(String email);
    Account changeAccountStatus(String email, AccountStatus newStatus);
    Account getAccountByEmail(String email);
    Page <Account> queryAccountsAndCards(int page, int size);

}
