// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取内网服务器列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> aclinkServers: 服务器设备列表，参考{@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * </ul>
 *
 */
public class ListAclinkServersResponse {
	private Long nextPageAnchor;
	
	@ItemType(AclinkServerDTO.class)
	private List<AclinkServerDTO> aclinkServers;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AclinkServerDTO> getAclinkServers() {
		return aclinkServers;
	}

	public void setAclinkServers(List<AclinkServerDTO> aclinkServers) {
		this.aclinkServers = aclinkServers;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
