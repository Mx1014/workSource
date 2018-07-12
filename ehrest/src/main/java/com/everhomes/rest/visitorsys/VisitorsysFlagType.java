// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * 
 * <ul>
 * <li>NO((byte)0): 否定</li>
 * <li>YES((byte)1): 肯定</li>
 * </ul>
 */
public enum VisitorsysFlagType {
	NO((byte)0),
	YES((byte)1);

	private byte code;

	private VisitorsysFlagType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static VisitorsysFlagType fromCode(Byte code) {
		if (code != null) {
			for (VisitorsysFlagType status : VisitorsysFlagType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
}
