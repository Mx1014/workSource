package com.everhomes.rest.approval;

/**
 * 
 * <ul>忘打卡申请类型
 * <li>ALL_DAY: 1，整天忘打卡</li>
 * <li>MORNING: 2，上午忘打卡申请</li>
 * <li>AFTERNOON: 3，下午忘打卡申请</li>
 * </ul>
 */
public enum ForgetToPunchType {
	ALL_DAY((byte)1), MORNING((byte)2), AFTERNOON((byte)3);
	
	private byte code;

	private ForgetToPunchType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public ForgetToPunchType fromCode(Byte code){
		if (code != null) {
			for (ForgetToPunchType forgetToPunchType : ForgetToPunchType.values()) {
				if (forgetToPunchType.getCode() == code.byteValue()) {
					return forgetToPunchType;
				}
			}
		}
		return null;
	}

}
