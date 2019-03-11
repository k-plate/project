package com.doug.kplate.dto.recharge;


import java.sql.Timestamp;

public class RechargeDto {

  private long id;//编号
  private String rechargeCode;//充值编号
  private long userId;//交易账号
  private String userName;//交易姓名
  private Timestamp updateTime;//操作时间
  private double rechargeMoney;//金额
  private double rechargeBalance;//会员账户余额
  private String remark;//备注
  private Timestamp auditTime;//审核时间
  private long auditStatus;//审核/状态
  private String auditUser;//审核人
  private int allMoney;

  public int getAllMoney() {
    return allMoney;
  }

  public void setAllMoney(int allMoney) {
    this.allMoney = allMoney;
  }

  public String getAuditUser() {
    return auditUser;
  }

  public void setAuditUser(String auditUser) {
    this.auditUser = auditUser;
  }

  public String getRechargeCode() {
    return rechargeCode;
  }

  public void setRechargeCode(String rechargeCode) {
    this.rechargeCode = rechargeCode;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public double getRechargeMoney() {
    return rechargeMoney;
  }

  public void setRechargeMoney(double rechargeMoney) {
    this.rechargeMoney = rechargeMoney;
  }


  public double getRechargeBalance() {
    return rechargeBalance;
  }

  public void setRechargeBalance(double rechargeBalance) {
    this.rechargeBalance = rechargeBalance;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public java.sql.Timestamp getAuditTime() {
    return auditTime;
  }

  public void setAuditTime(java.sql.Timestamp auditTime) {
    this.auditTime = auditTime;
  }


  public long getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(long auditStatus) {
    this.auditStatus = auditStatus;
  }

}
