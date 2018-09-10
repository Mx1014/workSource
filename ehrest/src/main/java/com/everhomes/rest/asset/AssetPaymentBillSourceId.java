package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1: 缴费新增/li>
 *     <li>2: 缴费导入/li>
 * </ul>
 */
public enum AssetPaymentBillSourceId {
    CREATE((byte)1), IMPORT((byte)2);

    private Byte code;

    private AssetPaymentBillSourceId(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetPaymentBillSourceId fromCode(Byte code) {
        for(AssetPaymentBillSourceId v : AssetPaymentBillSourceId.values()) {
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
