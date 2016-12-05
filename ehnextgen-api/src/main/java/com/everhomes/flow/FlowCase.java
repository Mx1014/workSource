package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.util.StringHelper;

public class FlowCase extends EhFlowCases {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4473391082820617795L;
	
	private Byte allowApplierUpdate;

	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}

	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
