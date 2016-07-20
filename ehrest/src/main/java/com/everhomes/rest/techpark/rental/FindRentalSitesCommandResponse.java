package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 场所
 * <li>rentalSites：场所列表</li>
 * </ul>
 */
public class FindRentalSitesCommandResponse {
	private Integer nextPageOffset;
    @ItemType(RentalSiteDTO.class)
	private List<RentalSiteDTO> rentalSites;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<RentalSiteDTO> getRentalSites() {
		return rentalSites;
	}

	public void setRentalSites(List<RentalSiteDTO> rentalSites) {
		this.rentalSites = rentalSites;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
 
}
