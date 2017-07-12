package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>0: execute task</li>
 *  <li>1: review task</li>
 * </ul>
 */
public enum TaskType {
	EXECUTE_TASK((byte)0), REVIEW_TYPE((byte)1);
	
	private byte code;
	
	private TaskType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static TaskType fromStatus(Byte code) {
		if(code != null) {
			for(TaskType v : TaskType.values()) {
				if(v.getCode() == code)
					return v;
			}
		}

		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
