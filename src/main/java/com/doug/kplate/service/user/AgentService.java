package com.doug.kplate.service.user;

import com.baomidou.mybatisplus.service.IService;
import com.doug.kplate.dto.user.AgentDto;
import com.doug.kplate.entity.user.Agent;

import java.util.List;
import java.util.Map;

public interface AgentService extends IService<Agent> {

    List<AgentDto> queryAll(Map<String, Object> param);

    Integer queryCount(Map<String, Object> param);
}
