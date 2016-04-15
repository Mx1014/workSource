package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>DISABLED : 0</li>
 *	<li>WAITING : 1</li>
 *	<li>ACTIVE : 2</li>
 * </ul>
 *
 */
public enum QualityInspectionCategoryStatus {

	DISABLED((byte)0), WAITING((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private QualityInspectionCategoryStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionCategoryStatus fromStatus(byte code) {
		for(QualityInspectionCategoryStatus v : QualityInspectionCategoryStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
