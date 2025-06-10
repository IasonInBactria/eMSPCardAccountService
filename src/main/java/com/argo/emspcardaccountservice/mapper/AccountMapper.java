package com.argo.emspcardaccountservice.mapper;

import com.argo.emspcardaccountservice.domain.pojo.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Mapper // 标识这是一个 MyBatis Mapper 接口
public interface AccountMapper extends BaseMapper<Account> {

    // 通过 email 查询账户
    @Select("SELECT * FROM accounts WHERE email = #{email}")
    Optional<Account> findByEmail(@Param("email") String email);

    // 检查 email 是否存在
    @Select("SELECT COUNT(*) FROM accounts WHERE email = #{email}")
    boolean existsByEmail(@Param("email") String email);

    // 如果需要更复杂的查询，可以自定义 SQL 或使用 MyBatis-Plus 的 LambdaQueryWrapper
    // 示例：查询指定时间范围内的账户，实际应用中通常结合 MyBatis-Plus 的 selectPage 方法
    List<Account> selectByLastUpdatedBetween(@Param("start") Instant start, @Param("end") Instant end);
}
