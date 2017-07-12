package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: none 未审核</li>
 *     <li>1: qualified 审核通过</li>
 *     <li>2: unqualified 审核不通过</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public enum WarehouseRequestReviewResult {
    NONE((byte)0), QUALIFIED((byte)1), UNQUALIFIED((byte)2);

    private byte code;

    private WarehouseRequestReviewResult(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static WarehouseRequestReviewResult fromCode(Byte code) {
        if(code != null) {
            WarehouseRequestReviewResult[] values = WarehouseRequestReviewResult.values();
            for(WarehouseRequestReviewResult value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
