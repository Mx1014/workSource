// @formatter:off
package com.everhomes.rest.group;

/**
 * 
 * <ul>审批状态：
 * <li>WAITING_FOR_APPROVING: 0，待审批</li>
 * <li>AGREEMENT: 1，同意</li>
 * <li>REJECTION: 2，驳回</li>
 * </ul>
 */
public enum ApprovalStatus {
	WAITING_FOR_APPROVING((byte)0), AGREEMENT((byte)1), REJECTION((byte)2);
	
	private byte code;
	
	private ApprovalStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public static ApprovalStatus fromCode(Byte code){
		if (code != null) {
			for (ApprovalStatus approvalStatus : ApprovalStatus.values()) {
				if (approvalStatus.getCode() == code.byteValue()) {
					return approvalStatus;
				}
			}
		}
		return null;
	}
}
