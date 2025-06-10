package com.argo.emspcardaccountservice.service.impl;

import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import com.argo.emspcardaccountservice.domain.exception.DuplicateResourceException;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.argo.emspcardaccountservice.domain.exception.ResourceNotFoundException;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.argo.emspcardaccountservice.mapper.AccountMapper;
import com.argo.emspcardaccountservice.mapper.CardMapper;
import com.argo.emspcardaccountservice.service.AccountService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper; // 用于查询关联卡片

    @Override
    @Transactional
    public Account createAccount(String email) {
        if (accountMapper.existsByEmail(email)) {
            throw new DuplicateResourceException("Account", "email", email);
        }
        Account account = new Account(email);
        accountMapper.insert(account);
        return account;
    }

    @Override
    @Transactional
    public Account changeAccountStatus(String email, AccountStatus newStatus) {
        Optional<Account> optionalAccount = accountMapper.findByEmail(email);
        Account account = optionalAccount
                .orElseThrow(() -> new ResourceNotFoundException("Account", email));

        AccountStatus oldStatus = account.getStatus();
        if (!AccountStatus.isValidTransition(oldStatus, newStatus)) {
            throw new InvalidStatusTransitionException("Account", oldStatus.name(), newStatus.name());
        }

        // 根据新状态调用领域对象的行为
        switch (newStatus) {
            case ACTIVATED:
                account.activate();
                break;
            case DEACTIVATED:
                account.deactivate();
                break;
            case CREATED:
                // CREATED 状态通常只在创建时设置，此处不做处理
                break;
        }
        accountMapper.updateById(account); // MyBatis-Plus 更新操作
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByEmail(String email) {
        Account account = accountMapper.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", email));
        // 手动加载关联卡片
        List<Card> cards = cardMapper.findByAccountId(account.getId());
        account.setCards(cards);
        return account;
    }

    @Override
    public Page <Account> queryAccountsAndCards(int page, int size) {
        Page <Account> pageResult = accountMapper.selectPage(new Page<>(page, size), null);
         for (Account account : pageResult.getRecords()) {
            List<Card> cards = cardMapper.findByAccountId(account.getId());
            account.setCards(cards);
        }

        return pageResult;
    }
}
