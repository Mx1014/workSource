package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬批次状态
 * <li>TEXT (0): 文本类 </li>
 * <li>NUMBER (1): 数值类</li>
 * </ul>
 */
public enum SalaryEntityType {
	TEXT((byte) 0), NUMBER((byte) 1);
	private Byte code;

	private SalaryEntityType(Byte code) {
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
