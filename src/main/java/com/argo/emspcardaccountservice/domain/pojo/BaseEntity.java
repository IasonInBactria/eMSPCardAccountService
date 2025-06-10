package com.argo.emspcardaccountservice.domain.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


import java.time.Instant; // 保持 Instant，MyBatis-Plus 默认可处理


@Data
public abstract class BaseEntity {

    @TableField(fill = FieldFill.INSERT)
    protected Instant createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    protected Instant lastUpdated;
}
