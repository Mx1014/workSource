package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>siteNumber: 资源编号</li>
 * <li>siteNumberGroup: 关联性分组(同一个分组号码的会成为一组)</li> 
 * </ul>
 */
public class SiteNumberDTO {

	private String siteNumber ;
	private Integer siteNumberGroup;
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
	public Integer getSiteNumberGroup() {
		return siteNumberGroup;
	}
	public void setSiteNumberGroup(Integer siteNumberGroup) {
		this.siteNumberGroup = siteNumberGroup;
	}

}
