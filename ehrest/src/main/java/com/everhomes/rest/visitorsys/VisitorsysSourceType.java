// @formatter:off
package com.everhomes.rest.visitorsys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <ul>
 * <li>INTERNAL((byte)0): 内部录入</li>
 * <li>OUTER((byte)1): 外部对接</li>
 * </ul>
 */
public enum VisitorsysSourceType {
	INTERNAL((byte)0,"内部录入"),
	OUTER((byte)1,"外部对接");

	private byte code;
	private String  desc;

	private VisitorsysSourceType(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public byte getCode(){
		return code;
	}

	public static VisitorsysSourceType fromCode(Byte aByte) {
		if (aByte != null) {
			for (VisitorsysSourceType status : VisitorsysSourceType.values()) {
				if (status.code == aByte.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
}
