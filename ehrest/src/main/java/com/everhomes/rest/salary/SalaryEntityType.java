package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬批次状态
 * <li>	GRANT((byte) 0,"发放项"), DEDUCTION((byte) 1,"扣款项"),COST((byte)2,"成本项"),REDUN((byte)3,"冗余项");</li>
 * </ul>
 */
public enum SalaryEntityType {
	GRANT((byte) 0,"发放项"), DEDUCTION((byte) 1,"扣款项"),COST((byte)2,"成本项"),REDUN((byte)3,"冗余项");
	private Byte code;
	private String descri;

	SalaryEntityType(Byte code,String descri) {
		this.code = code;
		this.descri = descri;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static SalaryEntityType fromCode(Byte code) {
		if (code != null) {
			for (SalaryEntityType a : SalaryEntityType.values()) {
				if (code.byteValue() == a.getCode().byteValue()) {
					return a;
				}
			}
		}
		return null;
	}

	public String getDescri() {
		return descri;
	}
}
