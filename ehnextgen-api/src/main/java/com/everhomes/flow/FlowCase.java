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

    public void setProjectTypeA(String projectTypeA) {
        FlowCaseCustomField.PROJECT_TYPE_A.setStringValue(this, projectTypeA);
    }

    public String getProjectTypeA() {
        return FlowCaseCustomField.PROJECT_TYPE_A.getStringValue(this);
    }

    public void setProjectIdA(Long projectIdA) {
        FlowCaseCustomField.PROJECT_ID_A.setIntegralValue(this, projectIdA);
    }

    public Long getProjectIdA() {
        return FlowCaseCustomField.PROJECT_TYPE_A.getIntegralValue(this);
    }

	public void incrStepCount() {
	    this.setStepCount(this.getStepCount() + 1);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
