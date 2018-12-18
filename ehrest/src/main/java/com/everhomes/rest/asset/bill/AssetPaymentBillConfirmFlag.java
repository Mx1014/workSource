package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 2018年12月5日 下午7:52:04
 */

/**
 * 支付状态是否已确认字段，1：已确认；0：待确认
 */
public enum AssetPaymentBillConfirmFlag {
    CONFIRM((byte)1, "已确认"), UNCONFIRM((byte)0, "待确认");

    private byte code;
    private String name;

    private AssetPaymentBillConfirmFlag(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetPaymentBillConfirmFlag fromStatus(byte code) {
        for(AssetPaymentBillConfirmFlag v : AssetPaymentBillConfirmFlag.values()) {
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
