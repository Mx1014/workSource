package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>siteItems：场所商品</li>
 * </ul>
 */
public class FindRentalSiteItemsCommandResponse {

	private List<SiteItemDTO> siteItems;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}
	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
	}
}
