package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>SENDED((byte) 0,"已发送"), REVOKED((byte) 1,"已撤回"),CONFIRMED((byte) 2,"已确认")</li>
 * </ul>
 */
public enum PayslipImportStatus {
	SENDED((byte) 0,"已发送"), REVOKED((byte) 1,"已撤回"),CONFIRMED((byte) 2,"已确认");
	private Byte code;
	private String descri;

	PayslipImportStatus(Byte code, String descri) {
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
	public static PayslipImportStatus fromCode(Byte code) {
		if (code != null) {
			for (PayslipImportStatus a : PayslipImportStatus.values()) {
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
