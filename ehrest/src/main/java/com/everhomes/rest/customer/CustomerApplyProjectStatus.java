package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1、进行中；2、已结束，3、搁置；4、异常、5、其他</li>
 * </ul>
 * Created by ying.xiong on 2017/12/7.
 */
public enum CustomerApplyProjectStatus {
    UNDER_WAY((byte)1, "进行中"), COMPLETED((byte)2, "已结束"), ABEYANCE((byte)3, "搁置"),
    ABNORMAL((byte)4, "异常"), OTHERS((byte)5, "其他");

    private byte code;
    private String name;

    private CustomerApplyProjectStatus(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CustomerApplyProjectStatus fromStatus(byte code) {
        for(CustomerApplyProjectStatus v : CustomerApplyProjectStatus.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }
    public static CustomerApplyProjectStatus fromName(String name){
        for(CustomerApplyProjectStatus v : CustomerApplyProjectStatus.values()) {
            if(v.getName().equals(name))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
