package com.argo.emspcardaccountservice.mapper;

import com.argo.emspcardaccountservice.domain.pojo.Card;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CardMapper extends BaseMapper<Card> {

    // 通过 contractId 查询卡片
    @Select("SELECT * FROM cards WHERE contract_id = #{contractId}")
    Optional<Card> findByContractIdValue(@Param("contractId") String contractId);

    // 检查 contractId 是否存在
    @Select("SELECT COUNT(*) FROM cards WHERE contract_id = #{contractId}")
    boolean existsByContractIdValue(@Param("contractId") String contractId);

    // 检查 rfidUid 是否存在
    @Select("SELECT COUNT(*) FROM cards WHERE rfid_uid = #{rfidUid}")
    boolean existsByRfidUid(@Param("rfidUid") String rfidUid);

    // 查询属于某个账户的所有卡片
    @Select("SELECT * FROM cards WHERE account_id = #{accountId}")
    List<Card> findByAccountId(@Param("accountId") Long accountId);
}
