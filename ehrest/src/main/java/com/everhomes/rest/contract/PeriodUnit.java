package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: 分; 1: 小时; 2: 天; 3: 月; 4: 年</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public enum PeriodUnit {
    MINUTE((byte)0), HOUR((byte)1), DAY((byte)2), MONTH((byte)3), YEAR((byte)4);
    private byte code;

    private PeriodUnit(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PeriodUnit fromStatus(byte code) {
        for(PeriodUnit v : PeriodUnit.values()) {
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
