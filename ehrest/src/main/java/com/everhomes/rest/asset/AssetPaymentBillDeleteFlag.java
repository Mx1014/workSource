package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0：已删除，1：有效</li>
 * </ul>
 */
public enum AssetPaymentBillDeleteFlag {
    DELETE((byte)0), VALID((byte)1);

    private Byte code;

    private AssetPaymentBillDeleteFlag(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetPaymentBillDeleteFlag fromCode(Byte code) {
        for(AssetPaymentBillDeleteFlag v : AssetPaymentBillDeleteFlag.values()) {
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
