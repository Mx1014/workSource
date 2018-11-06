package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1: 缴费新增/li>
 *     <li>2: 缴费导入/li>
 * </ul>
 */
public enum AssetPaymentBillSourceId {
    CREATE(1L), IMPORT(2L);

    private Long code;

    private AssetPaymentBillSourceId(Long code){
        this.code = code;
    }

    public Long getCode() {
        return code;
    }

    public static AssetPaymentBillSourceId fromCode(Long code) {
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
