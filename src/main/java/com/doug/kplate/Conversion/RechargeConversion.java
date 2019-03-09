package com.doug.kplate.Conversion;

import com.doug.kplate.dto.recharge.RechargeDto;
import com.doug.kplate.dto.user.AgentDto;
import com.doug.kplate.entity.recharge.Recharge;
import com.doug.kplate.entity.user.Agent;
import com.doug.kplate.enums.EnumAgentType;

import java.sql.Timestamp;

public class RechargeConversion {

    public static RechargeDto entityToDto(Recharge recharge) {
        if (recharge != null) {
            RechargeDto rechargeDto = new RechargeDto();
            rechargeDto.setRechargeCode(recharge.getRechargeCode());
            rechargeDto.setUserId(recharge.getUserId());
            rechargeDto.setUserName(recharge.getUserName());
            rechargeDto.setUpdateTime(recharge.getUpdateTime());
            rechargeDto.setRechargeMoney(recharge.getRechargeMoney());
            rechargeDto.setRechargeBalance(recharge.getRechargeBalance());
            rechargeDto.setRemark(recharge.getRemark());
            rechargeDto.setAuditTime(recharge.getAuditTime());
            rechargeDto.setAuditStatus(recharge.getAuditStatus());
            rechargeDto.setAuditUser(recharge.getAuditUser());
            return rechargeDto;
        }
        return null;
    }
}
