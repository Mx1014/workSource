package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>rentalSiteRules：  参考{@link com.everhomes.rest.techpark.rental.RentalSiteRulesDTO} </li>
 * </ul>
 */
public class FindRentalSiteRulesCommandResponse {
    @ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> rentalSiteRules;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<RentalSiteRulesDTO> getRentalSiteRules() {
		return rentalSiteRules;
	}

	public void setRentalSiteRules(List<RentalSiteRulesDTO> rentalSiteRules) {
		this.rentalSiteRules = rentalSiteRules;
	}
}
