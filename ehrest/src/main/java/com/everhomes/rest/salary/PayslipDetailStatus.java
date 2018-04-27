package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>SENDED((byte) 0,"已发送"), REVOKED((byte) 1,"已撤回"),CONFIRMED((byte) 2,"已确认")</li>
 * </ul>
 */
public enum PayslipDetailStatus {
	SENDED((byte) 0,"已发送"), REVOKED((byte) 1,"已撤回"),CONFIRMED((byte) 2,"已确认");
	private Byte code;
	private String descri;

	PayslipDetailStatus(Byte code, String descri) {
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
	public static PayslipDetailStatus fromCode(Byte code) {
		if (code != null) {
			for (PayslipDetailStatus a : PayslipDetailStatus.values()) {
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
