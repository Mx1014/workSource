// @formatter:off
package com.everhomes.address;

import com.everhomes.server.schema.tables.pojos.EhAddressArrangement;
import com.everhomes.util.StringHelper;

public class AddressArrangement extends EhAddressArrangement{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
