package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 下午4:42:40
 */


/**
 * 0: unpaid, 1: paid
 */
public enum AssetPaymentBillStatus {
    UNPAID((byte)0, "待缴"), PAID((byte)1, "已缴");

    private byte code;
    private String name;

    private AssetPaymentBillStatus(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetPaymentBillStatus fromStatus(byte code) {
        for(AssetPaymentBillStatus v : AssetPaymentBillStatus.values()) {
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
