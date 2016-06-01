package com.everhomes.rest.techpark.rental.admin;

/**
 * 
 * <ul>
 * <li>HOUR: 按小时</li>
 * <li>HALF_DAY: 按半天</li>
 * <li>DAY: 按天</li>
 * <li>HALF_DAY_NIGHT: 按带晚上的半天</li>
 * </ul>
 */
public enum RentalType {
	HOUR((byte) 0), HALF_DAY((byte) 1), DAY((byte) 2), HALF_DAY_NIGHT((byte) 3);

	private Byte code;

	private RentalType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static RentalType fromCode(Byte code) {
		if (code != null) {
			for (RentalType r : RentalType.values()) {
				if (code.byteValue() == r.code.byteValue()) {
					return r;
				}
			}
		}
		return null;
	}
}
