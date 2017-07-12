package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>abnormal: 0</li>
 *     <li>normal: 1</li>
 * </ul>
 * Created by ying.xiong on 2017/4/21.
 */
public enum ItemResultNormalFlag {
    ABNORMAL((byte)0), NORMAL((byte)1);

    private byte code;

    private ItemResultNormalFlag(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ItemResultNormalFlag fromStatus(byte code) {
        for(ItemResultNormalFlag v : ItemResultNormalFlag.values()) {
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
