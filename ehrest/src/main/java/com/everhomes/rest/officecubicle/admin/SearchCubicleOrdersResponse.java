package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>orders: 空间列表list{@link com.everhomes.rest.officecubicle.OfficeOrderDTO}</li> 
 * </ul>
 */
public class SearchCubicleOrdersResponse {

	private Long nextPageAnchor;
	
	@ItemType(OfficeRentOrderDTO.class)
	private List<OfficeRentOrderDTO> orders;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<OfficeRentOrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OfficeRentOrderDTO> orders) {
		this.orders = orders;
	}

}
