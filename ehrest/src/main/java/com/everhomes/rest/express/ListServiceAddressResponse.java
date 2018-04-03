// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>serviceAddressDTOs: 服务地址列表，参考{@link com.everhomes.rest.express.ExpressServiceAddressDTO}</li>
 * </ul>
 */
public class ListServiceAddressResponse {

	@ItemType(ExpressServiceAddressDTO.class)
	private List<ExpressServiceAddressDTO> serviceAddressDTOs;

	public ListServiceAddressResponse() {

	}

	public ListServiceAddressResponse(List<ExpressServiceAddressDTO> serviceAddressDTOs) {
		super();
		this.serviceAddressDTOs = serviceAddressDTOs;
	}

	public List<ExpressServiceAddressDTO> getServiceAddressDTOs() {
		return serviceAddressDTOs;
	}

	public void setServiceAddressDTOs(List<ExpressServiceAddressDTO> serviceAddressDTOs) {
		this.serviceAddressDTOs = serviceAddressDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
