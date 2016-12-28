package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.util.StringHelper;

public class FlowCase extends EhFlowCases {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4473391082820617795L;
	
	private Byte allowApplierUpdate;
	private String customObject;

	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}

	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}

	public String getCustomObject() {
		return customObject;
	}

	public void setCustomObject(String customObject) {
		this.customObject = customObject;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
