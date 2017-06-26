// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalItems: 门户Item列表，参考{@link com.everhomes.rest.portal.PortalItemDTO}</li>
 * </ul>
 */
public class ListPortalItemsResponse {

	@ItemType(PortalItemDTO.class)
	private List<PortalItemDTO> portalItems;

	public ListPortalItemsResponse() {

	}

	public ListPortalItemsResponse(List<PortalItemDTO> portalItems) {
		super();
		this.portalItems = portalItems;
	}

	public List<PortalItemDTO> getPortalItems() {
		return portalItems;
	}

	public void setPortalItems(List<PortalItemDTO> portalItems) {
		this.portalItems = portalItems;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
