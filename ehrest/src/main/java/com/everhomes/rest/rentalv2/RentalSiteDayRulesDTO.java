package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalDate：Long 日期</li>
 * <li>siteRules：单元格列表{@link com.everhomes.rest.rentalv2.RentalSiteRulesDTO}</li> 
 * </ul>
 */
public class RentalSiteDayRulesDTO {
	private Long rentalDate;
	@ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> siteRules;
	@ItemType(RentalSitePackagesDTO.class)
	private List<RentalSitePackagesDTO> sitePackages;
	
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

	public List<RentalSitePackagesDTO> getSitePackages() {
		return sitePackages;
	}

	public void setSitePackages(List<RentalSitePackagesDTO> sitePackages) {
		this.sitePackages = sitePackages;
	}
}
