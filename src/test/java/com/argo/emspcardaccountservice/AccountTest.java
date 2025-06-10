package com.argo.emspcardaccountservice;

import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTest {

    @Test
    void testCreateAccount() {

        String email = "test@example.com";

        Account account = new Account(email);
        account.setId(100L);

        assertNotNull(account.getId());
        assertEquals(email, account.getEmail());
        assertEquals(AccountStatus.CREATED, account.getStatus());
        assertNull(account.getCards()); // 默认不加载卡片
    }

    @Test
    void testActivateAccount() {
        Account account = new Account("test@example.com");
        assertEquals(AccountStatus.CREATED, account.getStatus());

        account.activate();

        assertEquals(AccountStatus.ACTIVATED, account.getStatus());
    }

    @Test
    void testDeactivateAccount() {

        Account account = new Account("test@example.com");
        account.activate(); // First activate
        assertEquals(AccountStatus.ACTIVATED, account.getStatus());

        account.deactivate();

        assertEquals(AccountStatus.DEACTIVATED, account.getStatus());
    }

    @Test
    void testReactivateAccount() {
        Account account = new Account("test@example.com");
        account.activate();
        account.deactivate(); // First deactivate
        assertEquals(AccountStatus.DEACTIVATED, account.getStatus());

        account.activate(); // Reactivate
        assertEquals(AccountStatus.ACTIVATED, account.getStatus());
    }

    @Test
    void testReactivateTransaction() {
        Account account = new Account("test@example.com");
        assertEquals(AccountStatus.CREATED, account.getStatus());

        assertThrows(InvalidStatusTransitionException.class, account::deactivate);
    }
}
