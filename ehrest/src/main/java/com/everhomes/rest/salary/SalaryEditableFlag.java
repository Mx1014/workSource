package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬组是否可编辑状态
 * <li>UNEDITABLE (0): 不可编辑 </li>
 * <li>EDITABLE (1): 可编辑</li>
 * </ul>
 */
public enum SalaryEditableFlag {
	UNEDITABLE((byte) 0), EDITABLE((byte) 1);
	private Byte code;

	private SalaryEditableFlag(Byte code) {
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
