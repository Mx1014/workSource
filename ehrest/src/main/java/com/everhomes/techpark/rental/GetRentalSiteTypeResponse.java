package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 * <ul>
*场所类型
 * <li>siteType：场所类型</li>
 * </ul>
 */
public class GetRentalSiteTypeResponse {
	
	private List<String> siteTypes;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public List<String> getSiteTypes() {
		return siteTypes;
	}
	public void setSiteTypes(List<String> siteTypes) {
		this.siteTypes = siteTypes;
	} 
}
