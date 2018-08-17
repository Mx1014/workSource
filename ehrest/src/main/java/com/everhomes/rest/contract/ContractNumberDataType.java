package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0:年、1:月、2:日</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractNumberDataType {
	YEAR("0"), MONTH("1"), DAY("2");

    private String code;

    private ContractNumberDataType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ContractNumberDataType fromStatus(String code) {
        if(code != null) {
            for(ContractNumberDataType v : ContractNumberDataType.values()) {
                if(v.getCode().equals(code))
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
