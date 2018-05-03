// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * 
 * <ul>
 * <li>DELETED((byte)0): 已删除</li>
 * <li>NOT_VISIT((byte)1): 未到访</li>
 * <li>WAIT_CONFIRM_VISIT((byte)2): 等待确认</li>
 * <li>HAS_VISITED((byte)3): 已到访</li>
 * <li>REJECTED_VISIT((byte)4): 已拒绝</li>
 * </ul>
 */
public enum VisitorsysVisitStatus {
	DELETED((byte)0),
	NOT_VISIT((byte)1),
	WAIT_CONFIRM_VISIT((byte)2),
	HAS_VISITED((byte)3),
	REJECTED_VISIT((byte)4);

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
