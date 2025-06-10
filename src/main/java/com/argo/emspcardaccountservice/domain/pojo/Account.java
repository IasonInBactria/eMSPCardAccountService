package com.argo.emspcardaccountservice.domain.pojo;

import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import com.argo.emspcardaccountservice.domain.exception.InvalidStatusTransitionException;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@TableName("accounts") // 映射到数据库中的 accounts 表
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 保护的无参构造函数，MyBatis-Plus 需要
public class Account extends BaseEntity {

    @TableId(type = IdType.AUTO) // MyBatis-Plus 的ID 生成策略
    private Long id; // UUID存储为String 类型

    @Setter
    @TableField(value = "email") // 明确映射列名
    private String email;

    @TableField(value = "status")
    private AccountStatus status;

    // 设置关联卡片列表的方法，通常由服务层调用
    // MyBatis-Plus 不直接支持 @OneToMany 关系，这个字段不会映射到数据库列
    // 它用于在内存中存储关联的卡片列表，需要服务层手动设置
    @Setter
    @TableField(exist = false)
    private List<Card> cards;

    public Account(String email) {
        this.email = email;
        this.status = AccountStatus.CREATED;
    }

    public void activate() {
        if (!AccountStatus.isValidTransition(this.status, AccountStatus.ACTIVATED)) {
            throw new InvalidStatusTransitionException("Account", this.status.name(), AccountStatus.ACTIVATED.name());
        }
        this.status = AccountStatus.ACTIVATED;
    }

    public void deactivate() {
        if (!AccountStatus.isValidTransition(this.status, AccountStatus.DEACTIVATED)) {
            throw new InvalidStatusTransitionException("Account", this.status.name(), AccountStatus.DEACTIVATED.name());
        }
        this.status = AccountStatus.DEACTIVATED;
    }
}
