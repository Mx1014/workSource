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
 * <li>HIDDEN((byte)5): 隐藏状态（内部使用）</li>
 * </ul>
 */
public enum VisitorsysStatus {
	DELETED((byte)0,"已删除"),
	NOT_VISIT((byte)1,"未到访"),
	WAIT_CONFIRM_VISIT((byte)2,"等待确认"),
	HAS_VISITED((byte)3,"已到访"),
	REJECTED_VISIT((byte)4,"已拒绝"),
	HIDDEN((byte)5,"隐藏");//内部使用，用于临时访客控制

	private byte code;
	private String  desc;

	private VisitorsysStatus(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public byte getCode(){
		return code;
	}

	public static VisitorsysStatus fromCode(Byte bookingCode) {
		if (bookingCode != null) {
			for (VisitorsysStatus status : VisitorsysStatus.values()) {
				if (status.code == bookingCode.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}

	public static VisitorsysStatus fromBookingCode(Byte bookingCode) {
		if (bookingCode != null) {
			for (VisitorsysStatus status : getBookingStatus()) {
				if (status.code == bookingCode.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}

//	public static VisitorsysStatus fromVisitStatusCode(Byte visitStatusCode) {
//		if (visitStatusCode != null) {
//			for (VisitorsysStatus status : getVisitStatus()) {
//				if (status.code == visitStatusCode.byteValue()) {
//					return status;
//				}
//			}
//		}
//		return null;
//	}

	/**
	 * 获取访客管理状态
	 * @return
	 */
	public static List<VisitorsysStatus> getVisitStatus() {
		return new ArrayList<>(Arrays.asList(DELETED,WAIT_CONFIRM_VISIT,HAS_VISITED,REJECTED_VISIT));
	}

	/**
	 *
	 * @return
	 */
	public static List<VisitorsysStatus> getBookingStatus() {
		return new ArrayList<>(Arrays.asList(DELETED,NOT_VISIT,HAS_VISITED,REJECTED_VISIT));
	}

    public String getDesc() {
		return desc;
    }
}
