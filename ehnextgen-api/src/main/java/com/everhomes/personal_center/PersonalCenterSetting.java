package com.everhomes.personal_center;
import com.everhomes.server.schema.tables.pojos.EhPersonalCenterSettings;
import com.everhomes.util.StringHelper;

public class PersonalCenterSetting extends EhPersonalCenterSettings {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7927304320134010496L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
