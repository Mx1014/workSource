package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>统计时间类型： 1-统计月份取（格式为xxxx-xx）, 2-统计年份取（格式为xxxx）</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractStatisticDateType {
	YEARMMSTR((byte)1,"按月份统计"), YEARSTR((byte)2,"按年份统计");

    private byte code;

    private String description;

    ContractStatisticDateType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ContractStatisticDateType fromStatus(Byte code) {
        if(code != null) {
            for(ContractStatisticDateType v : ContractStatisticDateType.values()) {
                if(v.getCode() == code)
                    return v;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
