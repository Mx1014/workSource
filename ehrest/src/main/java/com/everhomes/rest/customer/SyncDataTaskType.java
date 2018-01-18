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
    CUSTOMER((byte)4, "customer"), INDIVIDUAL((byte)5, "individual"), APARTMENT_STATUS((byte)6, "apartment_status"), CONTRACT((byte)7, "contract");


    private byte name;
    private String code;

    private SyncDataTaskType(Byte name, String code) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }
    public Byte getName() {
        return this.name;
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

    public static SyncDataTaskType fromName(byte name) {
        SyncDataTaskType[] values = SyncDataTaskType.values();
        for(SyncDataTaskType value : values) {
            if(value.name == name) {
                return value;
            }
        }

        return null;
    }
}
