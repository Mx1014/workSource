// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * 
 * <ul>
 * <li>NOT_VISIT((byte)0): 未到访</li>
 * <li>WAIT_CONFIRM_VISIT((byte)1): 等待确认</li>
 * <li>HAS_VISITED((byte)2): 已到访</li>
 * <li>REJECTED_VISIT((byte)3): 已拒绝</li>
 * <li>DELETED((byte)4): 已删除</li>
 * </ul>
 */
public enum VisitorsysVisitStatus {
	NOT_VISIT((byte)0),
	WAIT_CONFIRM_VISIT((byte)1),
	HAS_VISITED((byte)2),
	REJECTED_VISIT((byte)3),
	DELETED((byte)4);

	private byte code;

	private VisitorsysVisitStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static VisitorsysVisitStatus fromCode(Byte code) {
		if (code != null) {
			for (VisitorsysVisitStatus status : VisitorsysVisitStatus.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
}
