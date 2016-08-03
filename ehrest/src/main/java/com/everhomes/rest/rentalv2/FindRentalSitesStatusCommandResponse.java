package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FindRentalSitesStatusCommandResponse {
    @ItemType(RentalSiteDTO.class)
	private List<RentalSiteDTO> sites;
	private java.lang.String   contactNum;
	
	
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


	public java.lang.String getContactNum() {
		return contactNum;
	}


	public void setContactNum(java.lang.String contactNum) {
		this.contactNum = contactNum;
	}

 
}
