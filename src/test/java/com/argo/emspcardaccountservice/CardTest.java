package com.argo.emspcardaccountservice;


import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.argo.emspcardaccountservice.domain.pojo.EMAID;
import com.argo.emspcardaccountservice.domain.pojo.RFID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardCreation() {

        RFID rfid = new RFID("ABCDEF12345", "1234 5678 9012");

        Card card = new Card(rfid);
        card.setId(100L);

        assertNotNull(card.getId()); // ID 现在是数据库生成的，但在构造函数中不会有
        assertNotNull(card.getContractId());
        assertTrue(EMAID.isValid(card.getContractId())); // 检查 EMAID 格式
        assertEquals(rfid.getUid(), card.getRfidUid());
        assertEquals(rfid.getVisibleNumber(), card.getRfidVisibleNumber());
        assertEquals(CardStatus.CREATED, card.getStatus());
        assertNull(card.getAccountId());
        assertNull(card.getAccount()); // 默认不加载账户
    }

    @Test
    void testAssignment() {

        Card card = new Card(new RFID("ABC", "123"));
        Account account = new Account("account@example.com");
        account.setId(1L); // 模拟一个数据库生成的账户ID
        assertEquals(CardStatus.CREATED, card.getStatus());
        assertNull(card.getAccountId());

        card.assignToAccount(account);

        assertEquals(CardStatus.ASSIGNED, card.getStatus());
        assertEquals(account.getId(), card.getAccountId());
        assertEquals(account, card.getAccount());
    }

    @Test
    void teestRemoval() {
        Card card = new Card(new RFID("ABC", "123"));
        Account account = new Account("account@example.com");
        account.setId(1L);
        card.assignToAccount(account);
        assertEquals(CardStatus.ASSIGNED, card.getStatus());
        assertNotNull(card.getAccountId());

        card.removeCardFromAccount();

        assertEquals(CardStatus.CREATED, card.getStatus());
        assertNull(card.getAccountId());
        assertNull(card.getAccount());
    }

    @Test
    void testActivation() {
        Card card = new Card(new RFID("ABC", "123"));
        Account account = new Account("account@example.com");
        account.setId(1L);
        card.assignToAccount(account);
        assertEquals(CardStatus.ASSIGNED, card.getStatus());

        card.activate();

        assertEquals(CardStatus.ACTIVATED, card.getStatus());
    }

    @Test
    void testDeactivate() {
        // Given
        Card card = new Card(new RFID("ABC", "123"));
        Account account = new Account("account@example.com");
        account.setId(1L);
        card.assignToAccount(account);
        card.activate();
        assertEquals(CardStatus.ACTIVATED, card.getStatus());
        card.deactivate();

        assertEquals(CardStatus.DEACTIVATED, card.getStatus());
    }

    @Test
    void testReactivate() {
        // Given
        Card card = new Card(new RFID("ABC", "123"));
        Account account = new Account("account@example.com");
        account.setId(1L);
        card.assignToAccount(account);
        card.activate();
        card.deactivate();
        assertEquals(CardStatus.DEACTIVATED, card.getStatus());

        card.activate();

        assertEquals(CardStatus.ACTIVATED, card.getStatus());
    }

    @Test
    void testInvalidStatusTransition() {
        Card card = new Card(new RFID("ABC", "123"));
        assertEquals(CardStatus.CREATED, card.getStatus());

        assertThrows(InvalidStatusTransitionException.class, card::activate);
    }
}
