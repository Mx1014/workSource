// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>根据门禁id找到服务id,发送同步通知
 * <li>DoorId:门禁Id</li>
 * </ul>
 *
 */
public class NotifySyncVistorsCommand {
	private Long DoorId;

	public Long getDoorId() {
		return DoorId;
	}

	public void setDoorId(Long doorId) {
		DoorId = doorId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
