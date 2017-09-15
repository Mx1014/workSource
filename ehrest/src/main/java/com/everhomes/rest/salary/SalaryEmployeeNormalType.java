package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 *
 * <ul>员工异常状态
 * <li>NORMAL (0): 非异常 </li>
 * <li>ABNORMAL (1): 异常</li>
 * </ul>
 */
public enum SalaryEmployeeNormalType {
    NORMAL((byte) 0), ABNORMAL((byte) 1);
    private Byte code;

    private SalaryEmployeeNormalType(Byte code) {
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
