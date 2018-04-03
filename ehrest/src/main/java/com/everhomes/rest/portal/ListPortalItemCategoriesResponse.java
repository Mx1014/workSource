// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalItemCategories: 分类列表，参考{@link com.everhomes.rest.portal.PortalItemCategoryDTO}</li>
 * </ul>
 */
public class ListPortalItemCategoriesResponse {

	@ItemType(PortalItemCategoryDTO.class)
	private List<PortalItemCategoryDTO> portalItemCategories;

	public ListPortalItemCategoriesResponse() {

	}

	public ListPortalItemCategoriesResponse(List<PortalItemCategoryDTO> portalItemCategories) {
		super();
		this.portalItemCategories = portalItemCategories;
	}

	public List<PortalItemCategoryDTO> getPortalItemCategories() {
		return portalItemCategories;
	}

	public void setPortalItemCategories(List<PortalItemCategoryDTO> portalItemCategories) {
		this.portalItemCategories = portalItemCategories;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
