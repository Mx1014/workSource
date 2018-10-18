package com.everhomes.rest.rentalv2;

public enum SceneType {
    PM_ADMIN("pm_admin","管理公司员工"),
    ENTERPRISE("enterprise","普通公司员工"),
    PARK_TOURIST("park_tourist","未认证用户");

    private String code;
    private String describe;


    SceneType(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return this.code;
    }

    public static SceneType fromCode(String code) {
        SceneType[] values = SceneType.values();
        for(SceneType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
