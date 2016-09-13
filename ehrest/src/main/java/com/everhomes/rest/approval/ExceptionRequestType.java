package com.everhomes.rest.approval;

/**
 * 
 * <ul>异常申请类型
 * <li>ALL_DAY: 1，整天异常</li>
 * <li>MORNING: 2，上午异常申请</li>
 * <li>AFTERNOON: 3，下午异常申请</li>
 * </ul>
 */
public enum ExceptionRequestType {
	ALL_DAY((byte)1), MORNING((byte)2), AFTERNOON((byte)3);
	
	private byte code;

	private ExceptionRequestType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public ExceptionRequestType fromCode(Byte code){
		if (code != null) {
			for (ExceptionRequestType forgetToPunchType : ExceptionRequestType.values()) {
				if (forgetToPunchType.getCode() == code.byteValue()) {
					return forgetToPunchType;
				}
			}
		}
		return null;
	}

}
