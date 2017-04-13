// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>serviceAddressDTOs: 服务地址列表，参考{@link com.everhomes.rest.express.ServiceAddressDTO}</li>
 * </ul>
 */
public class ListServiceAddressResponse {

	@ItemType(ServiceAddressDTO.class)
	private List<ServiceAddressDTO> serviceAddressDTOs;

	public ListServiceAddressResponse() {

	}

	public ListServiceAddressResponse(List<ServiceAddressDTO> serviceAddressDTOs) {
		super();
		this.serviceAddressDTOs = serviceAddressDTOs;
	}

	public List<ServiceAddressDTO> getServiceAddressDTOs() {
		return serviceAddressDTOs;
	}

	public void setServiceAddressDTOs(List<ServiceAddressDTO> serviceAddressDTOs) {
		this.serviceAddressDTOs = serviceAddressDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
