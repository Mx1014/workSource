package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1：代表使用的是默认配置，0：代表有做过个性化的修改</li>
 * </ul>
 */
public enum AssetProjectDefaultFlag {
    DEFAULT((byte)1), PERSONAL((byte)0);

    private Byte code;

    private AssetProjectDefaultFlag(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetProjectDefaultFlag fromCode(Byte code) {
        for(AssetProjectDefaultFlag v : AssetProjectDefaultFlag.values()) {
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
