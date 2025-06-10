package com.argo.emspcardaccountservice.domain.pojo;

import com.argo.emspcardaccountservice.domain.enums.CardStatus;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@TableName("cards") // 映射到数据库中的 cards 表
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("contract_id") // 明确映射列名
    private String contractId; // 直接存储 EMAID 的字符串值

    @TableField("rfid_uid")
    private String rfidUid; // 直接存储 RFID UID

    @TableField("rfid_visible_number")
    private String rfidVisibleNumber; // 直接存储 RFID Visible Number

    @TableField("status")
    private CardStatus status;

    @TableField("account_id") // 映射外键列名
    @Setter
    private Long accountId; // 存储关联账户的 ID 字符串

    // 这个字段不映射到数据库，用于在内存中存储关联的 Account 对象，由服务层手动设置
    @TableField(exist = false)
    private Account account;

    public Card(RFID rfid) {
        this.rfidUid = rfid.getUid();
        this.rfidVisibleNumber = rfid.getVisibleNumber();
        this.contractId = EMAID.generate().getValue(); // 自动生成 EMAID 字符串值
        this.status = CardStatus.CREATED;
    }

    public void assignToAccount(Account account) {
        if (this.accountId != null && this.accountId.equals(account.getId())) {
            return; // 已经是这个账户，无需重复操作
        }
        if (this.status != CardStatus.CREATED) { // 只有 CREATED 状态的卡片才能分配
            throw new InvalidStatusTransitionException("Card", this.status.name(), CardStatus.ASSIGNED.name());
        }
        this.accountId = account.getId(); // 设置关联账户 ID
        this.account = account; // 在内存中设置关联对象
        this.status = CardStatus.ASSIGNED;
    }

    public void removeCardFromAccount() {
        if (this.accountId != null) {
            this.accountId = null; // 解除关联账户 ID
            this.account = null; // 移除内存中的关联对象
            this.status = CardStatus.CREATED; // 示例：解绑后回到 CREATED 状态
        }
    }

    public void activate() {
        if (!CardStatus.isValidTransition(this.status, CardStatus.ACTIVATED)) {
            throw new InvalidStatusTransitionException("Card", this.status.name(), CardStatus.ACTIVATED.name());
        }
        this.status = CardStatus.ACTIVATED;
    }

    public void deactivate() {
        if (!CardStatus.isValidTransition(this.status, CardStatus.DEACTIVATED)) {
            throw new InvalidStatusTransitionException("Card", this.status.name(), CardStatus.DEACTIVATED.name());
        }
        this.status = CardStatus.DEACTIVATED;
    }

    // 设置关联账户的方法，通常由服务层调用
    public void setAccount(Account account) {
        this.account = account;
    }

    // 辅助方法：从字符串值获取 EMAID 值对象
    public EMAID getEMAIDObject() {
        return EMAID.of(this.contractId);
    }

    // 辅助方法：从字段值获取 RFID 值对象
    public RFID getRFIDObject() {
        return new RFID(this.rfidUid, this.rfidVisibleNumber);
    }
}
