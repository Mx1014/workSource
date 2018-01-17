package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>CUSTOMER("customer")：同步企业客户</li>
 *     <li>INDIVIDUAL("individual")：同步个人客户</li>
 *     <li>CONTRACT("contract"): 同步合同</li>
 * </ul>
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
