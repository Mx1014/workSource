package com.everhomes.rentalv2;

import com.everhomes.rest.rentalv2.admin.SiteNumberDTO;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.util.StringHelper;

import java.util.List;

public class RentalResource extends EhRentalv2Resources {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936073467928653292L;

	private List<SiteNumberDTO> siteNumbers;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<SiteNumberDTO> getSiteNumbers() {
		return siteNumbers;
	}

	public void setSiteNumbers(List<SiteNumberDTO> siteNumbers) {
		this.siteNumbers = siteNumbers;
	}
}
