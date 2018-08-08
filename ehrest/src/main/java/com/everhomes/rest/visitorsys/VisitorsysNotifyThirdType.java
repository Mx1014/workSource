// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * 
 * <ul>
 * <li>NOT_YET((byte)0): 未回调</li>
 * <li>CALLBACK_FAILED((byte)1): 回调失败</li>
 * <li>CALLBACK_SUCCESS((byte)2): 回调成功</li>
 * </ul>
 */
public enum VisitorsysNotifyThirdType {
	NOT_YET((byte)0,"未回调"),
	CALLBACK_FAILED((byte)1,"回调失败"),
	CALLBACK_SUCCESS((byte)2,"回调成功");

	private byte code;
	private String  desc;

	private VisitorsysNotifyThirdType(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public byte getCode(){
		return code;
	}

	public static VisitorsysNotifyThirdType fromCode(Byte aByte) {
		if (aByte != null) {
			for (VisitorsysNotifyThirdType status : VisitorsysNotifyThirdType.values()) {
				if (status.code == aByte.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
}
