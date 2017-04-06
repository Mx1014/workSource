package com.everhomes.rest.ems;

/**
 * 
 * <ul>
 * <li>: </li>
 * </ul>
 */
public enum EmsBusinessType {
//	1:标准快递
//	8:代收到付
//	9:快递包裹
	STANDARD((byte)1), POST_PAYMENT((byte)8), PACKAGE((byte)9);
	
	private byte code;
	
	private EmsBusinessType(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EmsBusinessType fromCode(Byte code) {
		if (code != null) {
			for (EmsBusinessType businessType : EmsBusinessType.values()) {
				if (businessType.code == code.byteValue()) {
					return businessType;
				}
			}
		}
		return null;
	}
}
