// @formatter:off
package com.everhomes.rest.approval;

/**
 * 
 * <ul>审批类型
 * <li>ASK_FOR_LEAVE: 1，请假类型</li>
 * <li>FORGET_TO_PUNCH: 2，忘打卡类型</li>
 * </ul>
 */
public enum ApprovalType {
	ASK_FOR_LEAVE((byte)1), FORGET_TO_PUNCH((byte)2);
	
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
