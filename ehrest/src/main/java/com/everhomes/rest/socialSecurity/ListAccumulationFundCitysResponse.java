// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>accumulationFundCitys: 社保城市列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityCityDTO}</li>
 * </ul>
 */
public class ListAccumulationFundCitysResponse {

	@ItemType(SocialSecurityCityDTO.class)
	private List<SocialSecurityCityDTO> accumulationFundCitys;

	public ListAccumulationFundCitysResponse() {

	}

	public ListAccumulationFundCitysResponse(List<SocialSecurityCityDTO> accumulationFundCitys) {
		super();
		this.accumulationFundCitys = accumulationFundCitys;
	}

	public List<SocialSecurityCityDTO> getAccumulationFundCitys() {
		return accumulationFundCitys;
	}

	public void setAccumulationFundCitys(List<SocialSecurityCityDTO> accumulationFundCitys) {
		this.accumulationFundCitys = accumulationFundCitys;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
