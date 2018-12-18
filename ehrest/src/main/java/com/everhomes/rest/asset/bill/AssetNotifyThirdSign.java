package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 2018年12月5日 下午3:23:30
 */

/**
 * 0: 错误, 1: 正确
 */
public enum AssetNotifyThirdSign {
    ERROR((byte)0, "错误"), CORRECT((byte)1, "正确");

    private byte code;
    private String name;

    private AssetNotifyThirdSign(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetNotifyThirdSign fromStatus(byte code) {
        for(AssetNotifyThirdSign v : AssetNotifyThirdSign.values()) {
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
