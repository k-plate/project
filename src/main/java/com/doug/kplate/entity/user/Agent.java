package com.doug.kplate.entity.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * agent
 *
 * @author
 */
public class Agent implements Serializable {
    /**
     * 主键,自增
     */
    private Integer id;

    /**
     * 客户编号
     */
    private String agentCode;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 类型(1：经纪人2：代理商3：客户)
     */
    private Integer agentType;

    /**
     * 对应user表中的id
     */
    private Integer userId;

    /**
     * 对应role表中的id
     */
    private Integer roleId;

    /**
     * 真实姓名
     */
    private String contactName;

    /**
     * 客户手机号
     */
    private String contactMobile;

    /**
     * 备注用户名
     */
    private String noteUserName;

    /**
     * 红利比例
     */
    private String dividends;

    /**
     * 佣金比例
     */
    private String commissionRatio;

    /**
     * 保证金
     */
    private BigDecimal margin;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 赠送金额
     */
    private BigDecimal givingMoney;

    /**
     * 创建时间
     */
    private Timestamp createtime;

    /**
     * 修改时间
     */
    private Timestamp updatetime;

    /**
     * 邀请码
     */
    private String recommendCode;

    /**
     * 被邀请码
     */
    private String beRecommendCode;

    private static final long serialVersionUID = 1L;

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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getNoteUserName() {
        return noteUserName;
    }

    public void setNoteUserName(String noteUserName) {
        this.noteUserName = noteUserName;
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

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getGivingMoney() {
        return givingMoney;
    }

    public void setGivingMoney(BigDecimal givingMoney) {
        this.givingMoney = givingMoney;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public String getRecommendCode() {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getBeRecommendCode() {
        return beRecommendCode;
    }

    public void setBeRecommendCode(String beRecommendCode) {
        this.beRecommendCode = beRecommendCode;
    }

}