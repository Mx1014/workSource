package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>	SUCESS((byte) 0,"成功"), CANNOT_FIND_USER((byte) 1,"找不到用户-废弃此状态"),NULL_CONTACT((byte) 2,"员工不存在"),USER_UNTRACK((byte) 3,"号码未注册");</li>
 * </ul>
 */
public enum PayslipImportResult {
	SUCESS((byte) 0,"成功"), CANNOT_FIND_USER((byte) 1,"找不到用户"),NULL_CONTACT((byte) 2,"员工不存在"),USER_UNTRACK((byte) 3,"号码未注册");
	private Byte code;
	private String descri;

	PayslipImportResult(Byte code, String descri) {
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
	public static PayslipImportResult fromCode(Byte code) {
		if (code != null) {
			for (PayslipImportResult a : PayslipImportResult.values()) {
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
