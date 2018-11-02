package com.everhomes.rest.paymentauths;
/**
 * <ul>
 *     <li>flowUserSelectionType: 用户类型（person,deparment）</li>
 *     <li>selectionName: 用户名称</li>
 *     <li>sourceIdA: 用户ID</li>
 *     <li>sourceTypeA: "source_user","source_department"</li>
 * </ul>
 */

public class EnterpriesAuthDTO {

	private String flowUserSelectionType;
	private String selectionName;
	private Long sourceIdA;
	private String sourceTypeA;
	

	public String getFlowUserSelectionType() {
		return flowUserSelectionType;
	}

	public void setFlowUserSelectionType(String flowUserSelectionType) {
		this.flowUserSelectionType = flowUserSelectionType;
	}

	public String getSelectionName() {
		return selectionName;
	}
	
	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
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
	
	
}
