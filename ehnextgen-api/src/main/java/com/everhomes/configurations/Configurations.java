// @formatter:off
package com.everhomes.configurations;

import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.util.StringHelper;

public class Configurations extends EhConfigurations {
    
	private static final long serialVersionUID = -5213556622287112960L;

	public Configurations() {
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
