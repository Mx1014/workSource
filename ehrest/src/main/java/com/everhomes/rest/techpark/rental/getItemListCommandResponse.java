package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>siteItems：场所商品{@link com.everhomes.rest.techpark.rental.SiteItemDTO}}</li>
 * <li>nextPageAnchor：下页锚点</li>
 * </ul>
 */
public class getItemListCommandResponse {

    @ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
	private Long nextPageAnchor;
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
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
