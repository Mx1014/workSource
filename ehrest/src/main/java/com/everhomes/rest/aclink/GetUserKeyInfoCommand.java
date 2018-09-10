// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 以下参数只会取一个作为条件,优先拿上面的
 * <li>authId:授权Id</li>
 * <li>doorId:门禁id</li>
 * <li>groupId:门禁组id,仅临时授权有效</li>
 * <li>hardwareId:门禁的完整mac地址</li>
 * </ul>
 */
public class GetUserKeyInfoCommand {
	private Long authId;
	private Long doorId;
	private Long groupId;
	private String hardwareId;
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getHardwareId() {
		return hardwareId;
	}
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
