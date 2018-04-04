package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>计税方式
 * <li>	SALARY((byte) 0,"工资"), BONUS((byte) 1,"年终奖")</li>
 * </ul>
 */
public enum SalaryTaxPolicy {
	SALARY((byte) 0,"工资"), BONUS((byte) 1,"年终奖");
	private Byte code;
	private String descri;

	SalaryTaxPolicy(Byte code, String descri) {
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
	public static SalaryTaxPolicy fromCode(Byte code) {
		if (code != null) {
			for (SalaryTaxPolicy a : SalaryTaxPolicy.values()) {
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
