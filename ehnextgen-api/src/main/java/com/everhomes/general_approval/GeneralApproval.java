package com.everhomes.general_approval;

import com.everhomes.server.schema.tables.pojos.EhGeneralApprovals;
import com.everhomes.util.StringHelper;

public class GeneralApproval extends EhGeneralApprovals {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3510659547773984154L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
