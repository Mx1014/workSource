package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>result: 检查结果 Byte 0 :通过   Byte 1:重复不通过</li>
 * <li>checkObjectType: 检查对象类型 参考{@link com.everhomes.rest.equipment.CheckObjectType}</li>
 * </ul>
 */

public class CheckResultResponse {

    private  Byte result;

    private  Byte checkObjectType;

    public Byte getResult() {
        return result;
    }

    public void setResult(Byte result) {
        this.result = result;
    }

    public Byte getCheckObjectType() {
        return checkObjectType;
    }

    public void setCheckObjectType(Byte checkObjectType) {
        this.checkObjectType = checkObjectType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
