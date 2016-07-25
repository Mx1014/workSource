package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>orders: 空间列表list{@link com.everhomes.rest.officecubicle.OfficeOrderDTO}</li> 
 * </ul>
 */
public class SearchSpaceOrdersResponse {

	private Long nextPageAnchor;
	
	@ItemType(OfficeOrderDTO.class)
	private List<OfficeOrderDTO> orders;

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

	public List<OfficeOrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OfficeOrderDTO> orders) {
		this.orders = orders;
	}
 
	
}
