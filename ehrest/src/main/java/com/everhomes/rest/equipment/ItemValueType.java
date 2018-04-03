package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>NONE: 0</li>
 *     <li>TWO_TUPLE: 1</li>
 *     <li>RANGE: 2</li>
 * </ul>
 * Created by ying.xiong on 2017/4/21.
 */
public enum ItemValueType {
    NONE((byte)0), TWO_TUPLE((byte)1), RANGE((byte)2);

    private byte code;

    private ItemValueType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ItemValueType fromStatus(byte code) {
        for(ItemValueType v : ItemValueType.values()) {
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
