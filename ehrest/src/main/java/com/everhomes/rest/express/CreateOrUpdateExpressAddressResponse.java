// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>expressAddressDTO: 地址，参考{@link com.everhomes.rest.express.ExpressAddressDTO}</li>
 * </ul>
 */
public class CreateOrUpdateExpressAddressResponse {

	private ExpressAddressDTO expressAddressDTO;

	public CreateOrUpdateExpressAddressResponse() {

	}

	public CreateOrUpdateExpressAddressResponse(ExpressAddressDTO expressAddressDTO) {
		super();
		this.expressAddressDTO = expressAddressDTO;
	}

	public ExpressAddressDTO getExpressAddressDTO() {
		return expressAddressDTO;
	}

	public void setExpressAddressDTO(ExpressAddressDTO expressAddressDTO) {
		this.expressAddressDTO = expressAddressDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
