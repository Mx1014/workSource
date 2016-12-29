package com.everhomes.general_approval;

import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalVals;
import com.everhomes.util.StringHelper;

public class GeneralApprovalVal extends EhGeneralApprovalVals {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6018687550982649896L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
