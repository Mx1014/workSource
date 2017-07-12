// @formatter:off
package com.everhomes.rest.approval;

/**
 * 
 * <ul>审批类型
 * <li>ABSENCE: 1，请假申请类型</li>
 * <li>EXCEPTION: 2，异常申请类型</li>
 * <li>OVERTIME: 3，加班申请类型</li>
 * </ul>
 */
public enum ApprovalType {
	ABSENCE((byte)1), EXCEPTION((byte)2), OVERTIME((byte)3);
	
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
