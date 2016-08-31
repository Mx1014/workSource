// @formatter:off
package com.everhomes.rest.approval;

public enum ApprovalTargetType {
	USER((byte)1), ROLE((byte)2);
	
	private byte code;
	
	private ApprovalTargetType(byte code) {
		this.code = code;
	}
	
	public ApprovalTargetType fromCode(Byte code){
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
