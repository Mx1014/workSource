package com.everhomes.rest.customer;

/**
 * Created by ying.xiong on 2018/1/15.
 */
public enum SyncDataTaskType {
    CUSTOMER("customer"), INDIVIDUAL("individual"), APARTMENT_STATUS("apartment_status"), CONTRACT("contract");


    private String code;

    private SyncDataTaskType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static SyncDataTaskType fromCode(String code) {
        SyncDataTaskType[] values = SyncDataTaskType.values();
        for(SyncDataTaskType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
