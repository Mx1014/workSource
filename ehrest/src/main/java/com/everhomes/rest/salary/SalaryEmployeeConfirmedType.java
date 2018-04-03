package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 *
 * <ul>员工工资明细状态
 * <li>CONFIRMED (0): 已设置 </li>
 * <li>NOTCONFIRMED (1): 未设置</li>
 * </ul>
 */
public enum SalaryEmployeeConfirmedType {
    CONFIRMED((byte) 0), NOTCONFIRMED((byte) 1);
    private Byte code;

    private SalaryEmployeeConfirmedType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    public static LayoutType fromCode(Byte code) {
        if (code != null) {
            for (LayoutType a : LayoutType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
