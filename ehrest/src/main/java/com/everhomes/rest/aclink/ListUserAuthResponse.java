// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>auths:内网服务器列表{@link com.everhomes.rest.aclink.DoorAuthLiteDTO}</li>
 * <li>nextPageAnchor: 下一页锚点(CREATE_TIME)</li>
 * </ul>
 */
public class ListUserAuthResponse {
	@ItemType(DoorAuthLiteDTO.class)
	private List<DoorAuthLiteDTO> auths;
	private Long nextPageAnchor;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<DoorAuthLiteDTO> getAuths() {
		return auths;
	}

	public void setAuths(List<DoorAuthLiteDTO> auths) {
		this.auths = auths;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
