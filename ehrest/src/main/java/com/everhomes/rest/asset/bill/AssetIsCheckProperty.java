package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 2018年12月5日 下午4:27:46
 */

/**
 * 0: 不校验是否是CM资产, 1: 校验是否是CM资产
 */
public enum AssetIsCheckProperty {
    CHECK((byte)1, "校验是否是CM资产"), NOTCHECK((byte)0, "不校验是否是CM资产");

    private byte code;
    private String name;

    private AssetIsCheckProperty(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetIsCheckProperty fromStatus(byte code) {
        for(AssetIsCheckProperty v : AssetIsCheckProperty.values()) {
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
