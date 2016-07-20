package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class RentalSiteDayRulesDTO {
	private Long rentalDate;
	@ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> siteRules;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<RentalSiteRulesDTO> getSiteRules() {
		return siteRules;
	}

	public void setSiteRules(List<RentalSiteRulesDTO> siteRules) {
		this.siteRules = siteRules;
	}

	public Long getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Long rentalDate) {
		this.rentalDate = rentalDate;
	}
 
 
}
