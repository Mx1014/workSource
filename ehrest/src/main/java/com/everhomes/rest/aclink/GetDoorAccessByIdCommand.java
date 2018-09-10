// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 单个门禁id</li>
 * </ul>
 *
 */
public class GetDoorAccessByIdCommand {
    Long doorId;
    
	public Long getDoorId() {
		return doorId;
	}

	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
