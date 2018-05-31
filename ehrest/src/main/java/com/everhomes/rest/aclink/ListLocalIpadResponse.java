// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.aclink.AclinkIPadDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取ipad列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> aclinkIpads: ipad列表，参考{@link com.everhomes.rest.aclink.AclinkIPadDTO}</li>
 * </ul>
 */
public class ListLocalIpadResponse {
private Long nextPageAnchor;
	
	@ItemType(AclinkIPadDTO.class)
	private List<AclinkIPadDTO> aclinkIpads;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AclinkIPadDTO> getAclinkIpads() {
		return aclinkIpads;
	}

	public void setAclinkIpads(List<AclinkIPadDTO> aclinkIpads) {
		this.aclinkIpads = aclinkIpads;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
