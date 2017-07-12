// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>expressCompanyDTOs: 快递公司列表，参考{@link com.everhomes.rest.express.ExpressCompanyDTO}</li>
 * </ul>
 */
public class ListExpressCompanyResponse {

	@ItemType(ExpressCompanyDTO.class)
	private List<ExpressCompanyDTO> expressCompanyDTOs;

	public ListExpressCompanyResponse() {

	}

	public ListExpressCompanyResponse(List<ExpressCompanyDTO> expressCompanyDTOs) {
		super();
		this.expressCompanyDTOs = expressCompanyDTOs;
	}

	public List<ExpressCompanyDTO> getExpressCompanyDTOs() {
		return expressCompanyDTOs;
	}

	public void setExpressCompanyDTOs(List<ExpressCompanyDTO> expressCompanyDTOs) {
		this.expressCompanyDTOs = expressCompanyDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
