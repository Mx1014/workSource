package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>equipmentId: 设备id</li>
 *  <li>attachmentType: 类型 0: none, 1: 操作图示, 2: 说明书</li>
 * </ul>
 */
public class ListAttachmentsByEquipmentIdCommand {
	
	private Long equipmentId;
	
	private Byte attachmentType;

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
