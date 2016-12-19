package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowUserSelectionType: department, position, manager, variable</li>
 * <li>flowUserSourceType: source_user, source_department, source_position</li>
 * <li>sourceIdA: 具体的 ID 值 </li>
 * </ul>
 * @author janson
 *
 */
public class FlowSingleUserSelectionCommand {
	private String flowUserSelectionType;
	private Long sourceIdA;
	private String sourceTypeA;
	private Long sourceIdB;
	private String sourceTypeB;
	private String params;
	private String selectionName;

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

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
