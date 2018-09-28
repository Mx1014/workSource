// @formatter:off
package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 	<li>nextPageAnchor: 下页的锚点 </li>
 * 	<li>authorizePricesList: 房源授权价记录列表，参考{@link com.everhomes.rest.address.AuthorizePriceDTO}</li>
 * </ul>
 */
public class ListAuthorizePricesResponse {
	private Long nextPageAnchor;

	@ItemType(AuthorizePriceDTO.class)
	private List<AuthorizePriceDTO> authorizePricesList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AuthorizePriceDTO> getAuthorizePricesList() {
		return authorizePricesList;
	}

	public void setAuthorizePricesList(List<AuthorizePriceDTO> authorizePricesList) {
		this.authorizePricesList = authorizePricesList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
