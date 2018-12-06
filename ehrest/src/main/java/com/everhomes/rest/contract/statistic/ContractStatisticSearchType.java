package com.everhomes.rest.contract.statistic;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>用于区分数据查询类型： 1-汇总记录, 2-明细记录</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractStatisticSearchType {
	SUMMARYRECORD((byte)1,"汇总记录"), DETAILRECORD((byte)2,"明细记录");

    private byte code;

    private String description;

    ContractStatisticSearchType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ContractStatisticSearchType fromStatus(Byte code) {
        if(code != null) {
            for(ContractStatisticSearchType v : ContractStatisticSearchType.values()) {
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
