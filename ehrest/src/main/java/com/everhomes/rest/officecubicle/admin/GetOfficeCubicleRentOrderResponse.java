package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
 * <li>orders: 空间列表list{@link com.everhomes.rest.officecubicle.OfficeOrderDTO}</li> 
 * </ul>
 */
public class GetOfficeCubicleRentOrderResponse {

	
	@ItemType(OfficeRentOrderDTO.class)
	private OfficeRentOrderDTO orders;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public OfficeRentOrderDTO getOrders() {
		return orders;
	}

	public void setOrders(OfficeRentOrderDTO orders) {
		this.orders = orders;
	}



}
