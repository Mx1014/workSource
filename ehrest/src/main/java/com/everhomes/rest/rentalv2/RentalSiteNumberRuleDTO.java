package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>siteNumber：场所号</li>
 * <li>siteRules：单元格列表{@link com.everhomes.rest.rentalv2.RentalSiteRulesDTO}</li> 
 * </ul>
 */
public class RentalSiteNumberRuleDTO {

	private String siteNumber;
	@ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> siteRules;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public List<RentalSiteRulesDTO> getSiteRules() {
		return siteRules;
	}

	public void setSiteRules(List<RentalSiteRulesDTO> siteRules) {
		this.siteRules = siteRules;
	}
	
}
