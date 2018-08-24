// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authList:授权申请信息列表 {@link com.everhomes.rest.aclink.CreateLocalVistorCommand}</li>
 * <li>doorIds：门禁id列表</li>
 * <li>validFromMs: 有效期开始时间</li>
 * <li>validEndMs: 有效期终止时间</li>
 * </ul>
 *
 */
public class CreateVisitorBatchCommand {
	private List<CreateLocalVistorCommand> authList;
	private List<Long> doorIds;
	private Long validFromMs;
	private Long validEndMs;
	
	public Long getValidFromMs() {
		return validFromMs;
	}

	public void setValidFromMs(Long validFromMs) {
		this.validFromMs = validFromMs;
	}

	public Long getValidEndMs() {
		return validEndMs;
	}

	public void setValidEndMs(Long validEndMs) {
		this.validEndMs = validEndMs;
	}

	public List<Long> getDoorIds() {
		return doorIds;
	}

	public void setDoorIds(List<Long> doorIds) {
		this.doorIds = doorIds;
	}

	public List<CreateLocalVistorCommand> getAuthList() {
		return authList;
	}

	public void setAuthList(List<CreateLocalVistorCommand> authList) {
		this.authList = authList;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
