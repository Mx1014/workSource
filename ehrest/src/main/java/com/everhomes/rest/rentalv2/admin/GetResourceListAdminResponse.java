package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.rentalv2.RentalSiteDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>rentalSites: 资源列表list{@link com.everhomes.rest.rentalv2.RentalSiteDTO}</li> 
 * </ul>
 */
public class GetResourceListAdminResponse {
	private Long nextPageAnchor;
	
	@ItemType(RentalSiteDTO.class)
	private List<RentalSiteDTO> rentalSites;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<RentalSiteDTO> getRentalSites() {
		return rentalSites;
	}

	public void setRentalSites(List<RentalSiteDTO> rentalSites) {
		this.rentalSites = rentalSites;
	}

	
	
}