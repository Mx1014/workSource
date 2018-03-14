package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Orders;
import com.everhomes.util.StringHelper;

public class RentalOrder extends EhRentalv2Orders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 276859795301911837L;

	private String tip;

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
