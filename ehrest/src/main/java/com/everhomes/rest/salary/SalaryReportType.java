package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>	SALARY_DETAIL((byte) 0,"工资明细表"), DPT_STATISTIC((byte) 1,"部门汇总表"),SALARY_EMPLOYEE((byte) 2,"导出工资表");</li>
 * </ul>
 */
public enum SalaryReportType {
	SALARY_DETAIL((byte) 0,"工资明细表"), DPT_STATISTIC((byte) 1,"部门汇总表"),SALARY_EMPLOYEE((byte) 2,"导出工资表");
	private Byte code;
	private String descri;

	SalaryReportType(Byte code, String descri) {
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
	public static SalaryReportType fromCode(Byte code) {
		if (code != null) {
			for (SalaryReportType a : SalaryReportType.values()) {
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
