// @formatter:off
package com.everhomes.rest.approval;

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
