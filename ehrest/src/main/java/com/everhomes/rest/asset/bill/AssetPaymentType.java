package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 2018年12月5日 下午8:02:52
 */

/**
 * 1000：CM线下支付
 */
public enum AssetPaymentType {
    CMPAID(1000, "CM线下支付");

    private Integer code;
    private String name;

    private AssetPaymentType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AssetPaymentType fromStatus(byte code) {
        for(AssetPaymentType v : AssetPaymentType.values()) {
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
