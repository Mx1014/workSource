package com.everhomes.organization.pm;

import com.everhomes.server.schema.tables.pojos.EhAddressEvents;
import com.everhomes.util.StringHelper;

public class AddressEvent extends EhAddressEvents{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
