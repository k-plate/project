package com.doug.kplate.enums;

public enum EnumAgentType {

    BROKER(1, "经纪人"), AGENT(2, "代理商"), CUSTOMER(3, "客户");

    private Integer code;
    private String name;

    EnumAgentType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(Integer code) {
        for (EnumAgentType enumAgentType : EnumAgentType.values()) {
            if (enumAgentType.code == code) {
                return enumAgentType.name;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
