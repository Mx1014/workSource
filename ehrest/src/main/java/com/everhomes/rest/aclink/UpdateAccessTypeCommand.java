package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId:门禁id</li>
 * <li>doorType:门禁类型</li>
 * </ul>
 * @author liuyilin
 *
 */
public class UpdateAccessTypeCommand {
	Long doorId;
	Byte doorType;
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Byte getDoorType() {
		return doorType;
	}
	public void setDoorType(Byte doorType) {
		this.doorType = doorType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
