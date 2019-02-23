package com.doug.kplate.dao.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.entity.user.Agent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AgentDao extends BaseMapper<Agent> {

    List<Agent> getAgentAll(@Param("param") Map<String, Object> param);

    Integer getAgentCount(@Param("param") Map<String, Object> param);
}
