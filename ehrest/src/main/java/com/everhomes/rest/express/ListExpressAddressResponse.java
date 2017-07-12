// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>expressAddressDTOs: 地址，参考{@link com.everhomes.rest.express.ExpressAddressDTO}</li>
 * </ul>
 */
public class ListExpressAddressResponse {

	@ItemType(ExpressAddressDTO.class)
	private List<ExpressAddressDTO> expressAddressDTOs;

	public ListExpressAddressResponse() {

	}

	public ListExpressAddressResponse(List<ExpressAddressDTO> expressAddressDTOs) {
		super();
		this.expressAddressDTOs = expressAddressDTOs;
	}

	public List<ExpressAddressDTO> getExpressAddressDTOs() {
		return expressAddressDTOs;
	}

	public void setExpressAddressDTOs(List<ExpressAddressDTO> expressAddressDTOs) {
		this.expressAddressDTOs = expressAddressDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
