package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowSingleUserSelectionCommand {
	private String flowUserSelectionType;
	private Long sourceIdA;
	private String sourceTypeA;
	private Long sourceIdB;
	private String sourceTypeB;

	public String getFlowUserSelectionType() {
		return flowUserSelectionType;
	}

	public void setFlowUserSelectionType(String flowUserSelectionType) {
		this.flowUserSelectionType = flowUserSelectionType;
	}

	public Long getSourceIdA() {
		return sourceIdA;
	}

	public void setSourceIdA(Long sourceIdA) {
		this.sourceIdA = sourceIdA;
	}

	public String getSourceTypeA() {
		return sourceTypeA;
	}

	public void setSourceTypeA(String sourceTypeA) {
		this.sourceTypeA = sourceTypeA;
	}

	public Long getSourceIdB() {
		return sourceIdB;
	}

	public void setSourceIdB(Long sourceIdB) {
		this.sourceIdB = sourceIdB;
	}

	public String getSourceTypeB() {
		return sourceTypeB;
	}

	public void setSourceTypeB(String sourceTypeB) {
		this.sourceTypeB = sourceTypeB;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
