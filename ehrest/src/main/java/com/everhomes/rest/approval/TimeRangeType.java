package com.everhomes.rest.approval;

/**
 * 
 * <ul>时间范围类型：
 * <li>ALL_DAY: 1，整天</li>
 * <li>MORNING_HALF_DAY: 2，上午半天</li>
 * <li>AFTERNOON_HALF_DAY: 3，下午半天</li>
 * <li>TIME: 4，时间范围</li>
 * </ul>
 */
public enum TimeRangeType {
	ALL_DAY((byte)1), MORNING_HALF_DAY((byte)2), AFTERNOON_HALF_DAY((byte)3), TIME((byte)4);

	private byte code;
	
	private TimeRangeType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public TimeRangeType fromCode(Byte code){
		if (code != null) {
			for (TimeRangeType timeRangeType : TimeRangeType.values()) {
				if (code.byteValue() == timeRangeType.getCode()) {
					return timeRangeType;
				}
			}
		}
		return null;
	}
}
