package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * 0: inactive, 1: unpaid, 2: paid
 */
public enum AssetBillStatus {
    INACTIVE((byte)0, "已删除"), UNPAID((byte)1, "待缴"), PAID((byte)2, "已缴");

    private byte code;
    private String name;

    private AssetBillStatus(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetBillStatus fromStatus(byte code) {
        for(AssetBillStatus v : AssetBillStatus.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
