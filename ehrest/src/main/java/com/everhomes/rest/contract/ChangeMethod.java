package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1: 按金额递增; 2: 按金额递减; 3: 按比例递增; 4: 按比例递减</li>
 * </ul>
 * Created by ying.xiong on 2017/10/10.
 */
public enum ChangeMethod {
    INCREASE_BY_AMOUNT((byte)1,"按金额递增"), DECREASE_BY_AMOUNT((byte)2,"按金额递减"), 
    INCREASE_BY_PROPORTION((byte)3,"按比例递增"), DECREASE_BY_PROPORTION((byte)4,"按比例递减");
    
	private byte code;
	private String description;

    private ChangeMethod(byte code,String description){
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    
    public static ChangeMethod fromStatus(byte code) {
        for(ChangeMethod v : ChangeMethod.values()) {
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
