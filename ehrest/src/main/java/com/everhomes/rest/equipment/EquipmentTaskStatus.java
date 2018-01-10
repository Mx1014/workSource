package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0失效</li>
 *	<li>WAITING_FOR_EXECUTING : 1 待执行</li>
 *	<li>NEED_MAINTENANCE : 2 需维修</li>
 *	<li>IN_MAINTENANCE : 3 待维修</li>
 *	<li>CLOSE : 4 关闭</li>
 *	<li>DELAY : 5 已过期</li>
 * </ul>
 */
public enum EquipmentTaskStatus {

	NONE((byte) 0, ""),
	WAITING_FOR_EXECUTING((byte) 1, "任务待执行"),
	NEED_MAINTENANCE((byte) 2, "需维修"),//取消
	IN_MAINTENANCE((byte) 3, "待维修"), //取消
	CLOSE((byte) 4, "任务完成待审核"),
	DELAY((byte) 5, "任务已延期"),
	QUALIFIED((byte) 6, "任务完成审核通过"),
	REVIEW_DELAY((byte) 7, "任务完成审批过期");
	
	private byte code;
	private String name;
	
	 EquipmentTaskStatus(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static EquipmentTaskStatus fromStatus(byte code) {
		for(EquipmentTaskStatus v : EquipmentTaskStatus.values()) {
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
