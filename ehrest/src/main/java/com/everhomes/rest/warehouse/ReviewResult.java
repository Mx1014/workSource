package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: none</li>
 *  <li>1: qualified</li>
 *  <li>2: unqualified</li>
 *  <li>3: 未发起</li>
 * </ul>
 * Created by ying.xiong on 2017/5/17.
 */
public enum ReviewResult {
    NONE((byte)0), QUALIFIED((byte)1), UNQUALIFIED((byte)2), UNINITIALIZED((byte)3);
    private byte code;

    private ReviewResult(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ReviewResult fromStatus(byte code) {
        for(ReviewResult v : ReviewResult.values()) {
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
