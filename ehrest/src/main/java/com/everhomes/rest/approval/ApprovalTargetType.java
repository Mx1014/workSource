// @formatter:off
package com.everhomes.rest.approval;

/**
 * 
 * <ul>审批流程用户类型
 * <li>USER: 1，用户</li>
 * <li>ROLE: 2，角色</li>
 * </ul>
 */
public enum ApprovalTargetType {
	USER((byte)1), ROLE((byte)2);
	
	private byte code;
	
	private ApprovalTargetType(byte code) {
		this.code = code;
	}
	
	public static ApprovalTargetType fromCode(Byte code){
		if (code != null) {
			for (ApprovalTargetType approvalTargetType : ApprovalTargetType.values()) {
				if (approvalTargetType.getCode() == code.byteValue()) {
					return approvalTargetType;
				}
			}
		}
		return null;
	}
	
	public byte getCode(){
		return this.code;
	}
}
