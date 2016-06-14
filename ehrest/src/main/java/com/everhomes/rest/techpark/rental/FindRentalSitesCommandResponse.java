package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 场所
 * <li>rentalSites：场所列表</li>
 * <li>nextPageAnchor：下页的anchor</li>
 * </ul>
 */
public class FindRentalSitesCommandResponse {

    private Long nextPageAnchor;
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
 

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
 
}
