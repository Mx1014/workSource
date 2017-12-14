// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>socialSecurityCitys: 社保城市列表</li>
 * </ul>
 */
public class ListSocialSecurityCitysResponse {

	@ItemType(SocialSecurityCityDTO.class)
	private List<SocialSecurityCityDTO> socialSecurityCitys;

	public ListSocialSecurityCitysResponse() {

	}

	public ListSocialSecurityCitysResponse(List<SocialSecurityCityDTO> socialSecurityCitys) {
		super();
		this.socialSecurityCitys = socialSecurityCitys;
	}

	public List<SocialSecurityCityDTO> getSocialSecurityCitys() {
		return socialSecurityCitys;
	}

	public void setSocialSecurityCitys(List<SocialSecurityCityDTO> socialSecurityCitys) {
		this.socialSecurityCitys = socialSecurityCitys;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
