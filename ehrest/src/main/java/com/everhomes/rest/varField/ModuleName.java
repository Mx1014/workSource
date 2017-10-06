package com.everhomes.rest.varField;

/**
 * Created by ying.xiong on 2017/8/21.
 */
public enum ModuleName {
    CONTRACT("contract"), ENTERPRISE_CUSTOMER("enterprise_customer");

    private String name;

    private ModuleName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ModuleName fromName(String name) {
        if(name != null) {
            ModuleName[] values = ModuleName.values();
            for(ModuleName value : values) {
                if(value.equals(name)) {
                    return value;
                }
            }
        }

        return null;
    }
}
