package com.everhomes.organization.pmsy;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhPmsyOrders;
import com.everhomes.util.StringHelper;

public class PmsyOrder extends EhPmsyOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient List<PmsyOrderItem> items;
	
	private transient Long billDate;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<PmsyOrderItem> getItems() {
		return items;
	}

	public void setItems(List<PmsyOrderItem> items) {
		this.items = items;
	}

	public Long getBillDate() {
		return billDate;
	}

	public void setBillDate(Long billDate) {
		this.billDate = billDate;
	}

}
