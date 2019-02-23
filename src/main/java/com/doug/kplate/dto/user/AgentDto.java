package com.doug.kplate.dto.user;

import com.doug.kplate.enums.EnumAgentType;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AgentDto {

    private Integer id;//主键 id
    private String agentCode;//客户编号
    private String contactName;//真实姓名
    private Timestamp createtime;//创建时间
    private Timestamp lastLoginTime;//最后一次登录时间
    private Integer orderCount;//订单数
    private BigDecimal balance;//账户余额
    private String type;//身份
    private String dividends;//红利比例
    private String commissionRatio;//佣金比例
    private String superior;//上级

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDividends() {
        return dividends;
    }

    public void setDividends(String dividends) {
        this.dividends = dividends;
    }

    public String getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(String commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }
}
