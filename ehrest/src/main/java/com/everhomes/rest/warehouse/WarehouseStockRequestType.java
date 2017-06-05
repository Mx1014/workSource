package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: stock in 入库</li>
 *     <li>1: stock out 出库</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public enum WarehouseStockRequestType {
    STOCK_IN((byte)0, "入库"), STOCK_OUT((byte)1, "出库");

    private byte code;
    private String name;

    private WarehouseStockRequestType(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static WarehouseStockRequestType fromCode(Byte code) {
        if(code != null) {
            WarehouseStockRequestType[] values = WarehouseStockRequestType.values();
            for(WarehouseStockRequestType value : values) {
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
