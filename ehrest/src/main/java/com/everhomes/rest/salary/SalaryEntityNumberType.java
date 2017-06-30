package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬批次状态
 * <li>VALUE (0): 值 </li>
 * <li>FORMULA (1): 公式</li>
 * </ul>
 */
public enum SalaryEntityNumberType {
	VALUE((byte) 0), FORMULA((byte) 1);
	private Byte code;

	private SalaryEntityNumberType(Byte code) {
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
