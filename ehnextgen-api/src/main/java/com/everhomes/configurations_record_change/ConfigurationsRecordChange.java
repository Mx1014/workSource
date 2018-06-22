// @formatter:off
package com.everhomes.configurations_record_change;

import com.everhomes.server.schema.tables.pojos.EhConfigurationsRecordChange;
import com.everhomes.util.StringHelper;

/**
 * 
 * @author huanglm
 *
 */
public class ConfigurationsRecordChange extends EhConfigurationsRecordChange {
    
	private static final long serialVersionUID = -7257826421901751503L;
	public ConfigurationsRecordChange() {
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
