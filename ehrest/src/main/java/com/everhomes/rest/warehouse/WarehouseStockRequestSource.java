package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: request 申请</li>
 *     <li>1: manual input 手工录入</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public enum WarehouseStockRequestSource {
    REQUEST((byte)0), MANUAL_INPUT((byte)1);

    private byte code;

    private WarehouseStockRequestSource(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static WarehouseStockRequestSource fromCode(Byte code) {
        if(code != null) {
            WarehouseStockRequestSource[] values = WarehouseStockRequestSource.values();
            for(WarehouseStockRequestSource value : values) {
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
