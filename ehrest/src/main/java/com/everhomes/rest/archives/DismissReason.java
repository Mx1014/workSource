package com.everhomes.rest.archives;

public enum DismissReason {
    SALARY("薪资"), CULTURE("文化"), BALANCE("生活平衡"), PERSONALREASON("个人原因"), CAREERDEVELOPMENT("职业发展"),
    FIRE("不胜任"), ADJUSTMENT("编制调整"), BREAKRULE("违纪"), OTHER("其他");

    private String code;
    private DismissReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static DismissReason fromCode(String code) {
        if(code != null) {
            DismissReason[] values = DismissReason.values();
            for(DismissReason value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}