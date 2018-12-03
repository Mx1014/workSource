package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0：删除失败，1：删除成功</li>
 * </ul>
 */
public enum DeleteContractBillFlag {
    FAIL((byte)0, "删除失败"), SUCCESS((byte)1, "删除成功");

    private byte code;
    private String name;

    private DeleteContractBillFlag(byte code, String name){
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static DeleteContractBillFlag fromStatus(byte code) {
        for(DeleteContractBillFlag v : DeleteContractBillFlag.values()) {
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
