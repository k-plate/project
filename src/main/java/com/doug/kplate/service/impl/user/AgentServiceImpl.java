package com.doug.kplate.service.impl.user;

import com.doug.kplate.Conversion.AgentConversion;
import com.doug.kplate.dao.user.AgentDao;
import com.doug.kplate.dto.user.AgentDto;
import com.doug.kplate.entity.user.Agent;
import com.doug.kplate.service.user.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AgentServiceImpl extends ServiceImpl<AgentDao, Agent> implements AgentService {

    @Autowired
    private AgentDao agentDao;

    @Override
    public List<AgentDto> queryAll(Map<String, Object> param) {
        List<Agent> agents = agentDao.getAgentAll(param);
        List<AgentDto> agentDtos = new ArrayList<>();
        if (agents != null && agents.size() > 0) {
            for (Agent agent : agents) {
                agentDtos.add(AgentConversion.entityToDto(agent));
            }
        }
        return agentDtos;
    }

    @Override
    public Integer queryCount(Map<String, Object> param) {
        return agentDao.getAgentCount(param);
    }
}
