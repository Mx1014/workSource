// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalNavigationBars: 门户导航栏列表，参考{@link com.everhomes.rest.portal.PortalNavigationBarDTO}</li>
 * </ul>
 */
public class ListPortalNavigationBarsResponse {

	@ItemType(PortalNavigationBarDTO.class)
	private List<PortalNavigationBarDTO> portalNavigationBars;

	public ListPortalNavigationBarsResponse() {

	}

	public ListPortalNavigationBarsResponse(List<PortalNavigationBarDTO> portalNavigationBars) {
		super();
		this.portalNavigationBars = portalNavigationBars;
	}

	public List<PortalNavigationBarDTO> getPortalNavigationBars() {
		return portalNavigationBars;
	}

	public void setPortalNavigationBars(List<PortalNavigationBarDTO> portalNavigationBars) {
		this.portalNavigationBars = portalNavigationBars;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
