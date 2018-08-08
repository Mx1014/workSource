// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>查询服务器关联设备
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li>listAclinkServerRels：服务器关联设备列表{@link com.everhomes.rest.aclink.AclinkServerRelDTO}</li>
 * </ul>
 *
 */
public class QueryServerRelationsResponse {
	private Long nextPageAnchor;
	
	@ItemType(AclinkServerRelDTO.class)
	List<AclinkServerRelDTO> listAclinkServerRels;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AclinkServerRelDTO> getListAclinkServerRels() {
		return listAclinkServerRels;
	}

	public void setListAclinkServerRels(List<AclinkServerRelDTO> listAclinkServerRels) {
		this.listAclinkServerRels = listAclinkServerRels;
	}
	
}
