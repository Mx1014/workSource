package com.everhomes.flow;
import com.everhomes.rest.flow.FlowCaseStatus;
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

    public String getCurrentLane() {
        return FlowCaseCustomField.CURRENT_LANE.getStringValue(this);
    }

    public void setCurrentLane(String currentLane) {
        FlowCaseCustomField.CURRENT_LANE.setStringValue(this, currentLane);
    }

    public String getCurrentNode() {
        return FlowCaseCustomField.CURRENT_NODE.getStringValue(this);
    }

    public void setCurrentNode(String currentNode) {
        FlowCaseCustomField.CURRENT_NODE.setStringValue(this, currentNode);
    }

    public String getProjectTypeA() {
        return FlowCaseCustomField.PROJECT_TYPE_A.getStringValue(this);
    }

    public void setProjectIdA(Long projectIdA) {
        FlowCaseCustomField.PROJECT_ID_A.setIntegralValue(this, projectIdA);
    }

    public Long getProjectIdA() {
        return FlowCaseCustomField.PROJECT_ID_A.getIntegralValue(this);
    }

	public void incrStepCount() {
	    this.setStepCount(this.getStepCount() + 1);
    }

    public void addPath(Long flowCaseId) {
        if (this.getPath() == null) {
            this.setPath(String.valueOf(flowCaseId));
        } else {
            this.setPath(String.format("%s/%s", this.getPath(), flowCaseId));
        }
    }

    public Long getRootFlowCaseId() {
        if (this.getPath() == null) {
            return this.getId();
        }
        return Long.valueOf(this.getPath().split("/")[0]);
    }

    public void flushCurrentNode(FlowNode currentNode) {
        this.setCurrentNodeId(currentNode.getId());
        this.setCurrentNode(currentNode.getNodeName());
    }

    public void flushCurrentLane(FlowLane currentLane) {
        this.setCurrentLaneId(currentLane.getId());
        this.setCurrentLane(currentLane.getDisplayName());
        if (FlowCaseStatus.fromCode(this.getStatus()) == FlowCaseStatus.ABSORTED) {
            this.setCurrentLane(currentLane.getDisplayNameAbsort());
        }
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
