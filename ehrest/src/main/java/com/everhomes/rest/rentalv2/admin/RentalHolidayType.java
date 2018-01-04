package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>NORMAL_WEEKEND(1): 普通双休</li>
 * <li>LEGAL_HOLIDAY(2): 法定节假日</li>
 * </ul>
 */
public enum RentalHolidayType {
	NORMAL_WEEKEND((byte)1), LEGAL_HOLIDAY((byte)2);

	private Byte code;

	private RentalHolidayType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static RentalHolidayType fromCode(Byte code) {
		if (code != null) {
			for (RentalHolidayType a : RentalHolidayType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
