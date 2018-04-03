package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;
import com.everhomes.server.schema.tables.pojos.EhGeneralForms;
import com.everhomes.util.StringHelper;

public class GeneralFormVal extends EhGeneralFormVals {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3112464829225190611L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
