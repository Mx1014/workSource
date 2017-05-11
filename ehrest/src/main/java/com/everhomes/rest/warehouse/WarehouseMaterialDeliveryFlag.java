package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0:no</li>
 *     <li>1: yes</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public enum WarehouseMaterialDeliveryFlag {
    NO((byte)0), YES((byte)1);

    private byte code;

    private WarehouseMaterialDeliveryFlag(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static WarehouseMaterialDeliveryFlag fromCode(Byte code) {
        if(code != null) {
            WarehouseMaterialDeliveryFlag[] values = WarehouseMaterialDeliveryFlag.values();
            for(WarehouseMaterialDeliveryFlag value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
