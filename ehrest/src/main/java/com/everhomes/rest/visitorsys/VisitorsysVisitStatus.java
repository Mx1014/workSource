// @formatter:off
package com.everhomes.rest.visitorsys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <ul>
 * <li>DELETED((byte)0): 已删除（visitStatus,bookingStatus共用）</li>
 * <li>NOT_VISIT((byte)1): 未到访（bookingStatus 预约访客用）</li>
 * <li>WAIT_CONFIRM_VISIT((byte)2): 等待确认（visitStatus 访客管理用）</li>
 * <li>HAS_VISITED((byte)3): 已到访(visitStatus,bookingStatus共用)</li>
 * <li>REJECTED_VISIT((byte)4): 已拒绝（visitStatus 访客管理用）</li>
 * </ul>
 */
public enum VisitorsysVisitStatus {
	DELETED((byte)0,"已删除"),
	NOT_VISIT((byte)1,"未到访"),
	WAIT_CONFIRM_VISIT((byte)2,"等待确认"),
	HAS_VISITED((byte)3,"已到访"),
	REJECTED_VISIT((byte)4,"已拒绝");

	private byte code;
	private String  desc;

	private VisitorsysVisitStatus(byte code,String desc) {
		this.code = code;
		this.desc = desc;
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

	public static List<Byte> getNormalStatus() {
		return new ArrayList<>(Arrays.asList(NOT_VISIT.getCode(),WAIT_CONFIRM_VISIT.getCode(),HAS_VISITED.getCode(),REJECTED_VISIT.getCode()));
	}

    public String getDesc() {
		return desc;
    }
}
