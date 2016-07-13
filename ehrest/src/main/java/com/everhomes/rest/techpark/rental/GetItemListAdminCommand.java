package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize:每页数量 </li>
 * </ul>
 */
public class GetItemListAdminCommand {

	private Long rentalSiteId;

	private Long pageAnchor;
    
	private Integer pageSize;
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
 
 
}
