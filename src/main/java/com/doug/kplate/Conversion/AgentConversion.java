package com.doug.kplate.Conversion;

import com.doug.kplate.dto.user.AgentDto;
import com.doug.kplate.entity.user.Agent;
import com.doug.kplate.enums.EnumAgentType;

import java.sql.Date;
import java.sql.Timestamp;

public class AgentConversion {

    public static AgentDto entityToDto(Agent agent) {
        if (agent != null) {
            AgentDto agentDto = new AgentDto();
            agentDto.setAgentCode(agent.getAgentCode());
            agentDto.setBalance(agent.getBalance());
            agentDto.setType(EnumAgentType.getNameByCode(agent.getAgentType()));
            agentDto.setId(agent.getId());
            agentDto.setCommissionRatio(agent.getCommissionRatio());
            agentDto.setContactName(agent.getContactName());
            agentDto.setCreatetime(agent.getCreatetime());
            agentDto.setDividends(agent.getDividends());
            agentDto.setOrderCount(0);
            agentDto.setSuperior("豆豆");
            agentDto.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            return agentDto;
        }
        return null;
    }
}
