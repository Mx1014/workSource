// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>查询服务器关联门禁
 * <li>nextPageAnchor: 下一页锚点 </li>
 * <li>listDoorAccess:门禁列表 {@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * </ul>
 */
public class QueryDoorAccessByServerResponse {
	private Long nextPageAnchor;
	
	@ItemType(DoorAccessDTO.class)
	private List<DoorAccessDTO> listDoorAccess;

	public List<DoorAccessDTO> getListDoorAccess() {
		return listDoorAccess;
	}

	public void setListDoorAccess(List<DoorAccessDTO> listDoorAccess) {
		this.listDoorAccess = listDoorAccess;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
}
