package com.everhomes.rest.salary;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>薪酬批次状态
 * <li>CLOSE (0): 关闭</li>
 * <li>OPEN (2): 开启</li>
 * </ul>
 */
public enum SalaryEntityStatus {
	CLOSE((byte) 0), OPEN((byte) 2) ;
	private Byte code;

	private SalaryEntityStatus(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static SalaryEntityStatus fromCode(Byte code) {
		if (code != null) {
			for (SalaryEntityStatus a : SalaryEntityStatus.values()) {
				if (code.byteValue() == a.getCode().byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
