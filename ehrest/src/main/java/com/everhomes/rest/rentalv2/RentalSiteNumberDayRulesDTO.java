package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalDate：Long 日期</li>
 * <li>siteNumbers：资源编号列表{@link com.everhomes.rest.rentalv2.RentalSiteNumberRuleDTO}</li> 
 * </ul>
 */
public class RentalSiteNumberDayRulesDTO {
	private Long rentalDate;
	@ItemType(RentalSiteNumberRuleDTO.class)
	private List<RentalSiteNumberRuleDTO> siteNumbers;
	@ItemType(RentalSitePackagesDTO.class)
	private List<RentalSitePackagesDTO> sitePackages;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 

	public Long getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Long rentalDate) {
		this.rentalDate = rentalDate;
	}


	public List<RentalSiteNumberRuleDTO> getSiteNumbers() {
		return siteNumbers;
	}


	public void setSiteNumbers(List<RentalSiteNumberRuleDTO> siteNumbers) {
		this.siteNumbers = siteNumbers;
	}

	public List<RentalSitePackagesDTO> getSitePackages() {
		return sitePackages;
	}

	public void setSitePackages(List<RentalSitePackagesDTO> sitePackages) {
		this.sitePackages = sitePackages;
	}
}
