// @formatter:off
package com.everhomes.configurations_record_change;

import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.util.StringHelper;

public class ConfigurationsRecordChange extends EhConfigurations {
    
	private static final long serialVersionUID = -5213556622287112960L;

	public ConfigurationsRecordChange() {
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
