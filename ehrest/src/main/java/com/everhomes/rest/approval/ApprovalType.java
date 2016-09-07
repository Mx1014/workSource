// @formatter:off
package com.everhomes.rest.approval;

/**
 * 
 * <ul>审批类型
 * <li>ABSENCE: 1，请假类型</li>
 * <li>FORGOT: 2，忘打卡类型</li>
 * </ul>
 */
public enum ApprovalType {
	ABSENCE((byte)1), FORGOT((byte)2);
	
	private byte code;
	
	private ApprovalType(byte code){
		this.code = code;
	}
	
	public static ApprovalType fromCode(Byte code){
		if (code != null) {
			for (ApprovalType approvalType : ApprovalType.values()) {
				if (code.byteValue() == approvalType.code) {
					return approvalType;
				}
			}
		}
		return null;
	}
	
	public byte getCode(){
		return this.code;
	};
}
