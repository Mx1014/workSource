package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FindRentalSiteDayStatusCommandResponse {
    @ItemType(RentalSiteDTO.class)
	private List<RentalSiteDTO> sites;
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public List<RentalSiteDTO> getSites() {
		return sites;
	}


	public void setSites(List<RentalSiteDTO> sites) {
		this.sites = sites;
	}

 
}
