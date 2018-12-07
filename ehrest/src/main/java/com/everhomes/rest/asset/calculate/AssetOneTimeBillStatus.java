package com.everhomes.rest.asset.calculate;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>是否是一次性产生费用，1：是，0：否</li>
 * </ul>
 */
public enum AssetOneTimeBillStatus {
    TRUE((byte)1), FALSE((byte)0);

    private Byte code;

    private AssetOneTimeBillStatus(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetOneTimeBillStatus fromCode(Byte code) {
        for(AssetOneTimeBillStatus v : AssetOneTimeBillStatus.values()) {
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
