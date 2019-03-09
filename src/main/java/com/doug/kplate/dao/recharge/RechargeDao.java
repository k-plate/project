package com.doug.kplate.dao.recharge;

import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.dto.recharge.RechargeDto;
import com.doug.kplate.entity.recharge.Recharge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RechargeDao extends BaseDao<Recharge> {

    List<RechargeDto> selectList(Map<String, Object> map);

    int selectTotal(Map<String, Object> map);
}
