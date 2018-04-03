package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬组数值类是否需要核算
 * <li>UNWANT (0): 不需要核算 </li>
 * <li>WANT (1): 需要核算</li>
 * </ul>
 */
public enum SalaryNeedCheckType {
	UNWANT((byte) 0), WANT((byte) 1);
	private Byte code;

	private SalaryNeedCheckType(Byte code) {
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
