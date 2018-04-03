package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0:NO 不需要targetid为0的数据</li>
 *     <li>1:YES 需要targetid为0的数据</li>
 * </ul>
 * Created by ying.xiong on 2017/7/21.
 */
public enum TargetIdFlag {
    NO((byte)0), YES((byte)1);

    private Byte code;

    private TargetIdFlag(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static TargetIdFlag fromStatus(Byte code) {
        for(TargetIdFlag v : TargetIdFlag.values()) {
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
