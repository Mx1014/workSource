package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: auto, 1: third party, 2: manual</li>
 * </ul>
 */
public enum AssetBillSource {
    AUTO((byte)0), THIRD_PARTY((byte)1), MANUAL((byte)2);

    private Byte code;

    private AssetBillSource(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetBillSource fromCode(Byte code) {
        for(AssetBillSource v : AssetBillSource.values()) {
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
