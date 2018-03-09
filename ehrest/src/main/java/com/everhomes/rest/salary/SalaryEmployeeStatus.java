package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬批次状态
 * <li>		NORMAL((byte) 0,"正常"), REALPAY_ERROR((byte) 1,"实发合计为负"),UN_SET((byte)2,"未定薪");</li>
 * </ul>
 */
public enum SalaryEmployeeStatus {
	NORMAL((byte) 0,"正常"), REALPAY_ERROR((byte) 1,"实发合计为负"),UN_SET((byte)2,"未定薪");
	private Byte code;
	private String descri;

	SalaryEmployeeStatus(Byte code, String descri) {
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
	public static SalaryEmployeeStatus fromCode(Byte code) {
		if (code != null) {
			for (SalaryEmployeeStatus a : SalaryEmployeeStatus.values()) {
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
