package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 *     <li>0: inactive</li>
 *     <li>1: disable</li>
 *     <li>2: enable</li>
 * </ul>
 * Created by ying.xiong on 2017/5/10.
 */
public enum WarehouseStatus {
    INACTIVE((byte)0), DISABLE((byte)1), ENABLE((byte)2);

    private byte code;

    private WarehouseStatus(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static WarehouseStatus fromCode(Byte code) {
        if(code != null) {
            WarehouseStatus[] values = WarehouseStatus.values();
            for(WarehouseStatus value : values) {
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
