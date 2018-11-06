// @formatter:off
package com.everhomes.address;

import com.everhomes.server.schema.tables.pojos.EhAddressProperties;
import com.everhomes.util.StringHelper;

public class AddressProperties extends EhAddressProperties {
	
    private static final long serialVersionUID = 6886544399208678098L;

    public AddressProperties() {
    }
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
