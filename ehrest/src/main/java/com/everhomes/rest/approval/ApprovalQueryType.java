// @formatter:off
package com.everhomes.rest.approval;

public enum ApprovalQueryType {
	WAITING_FOR_APPROVE((byte)1), APPROVED((byte)2);
	
	private byte code;
	
	private ApprovalQueryType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public static ApprovalQueryType fromCode(Byte code){
		if (code != null) {
			for (ApprovalQueryType approvalQueryType : ApprovalQueryType.values()) {
				if (approvalQueryType.getCode() == code.byteValue()) {
					return approvalQueryType;
				}
			}
		}
		return null;
	}
}
