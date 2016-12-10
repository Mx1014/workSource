package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class CreateFlowCaseCommand {
	private Long flowMainId;
	private Integer flowVersion;
	private Long     applyUserId;
	private Long     referId;
	private String     referType;
	private Long projectId;
	private String projectType;
	private String content;
	public Long getFlowMainId() {
		return flowMainId;
	}
	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}
	public Integer getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}
	public Long getApplyUserId() {
		return applyUserId;
	}
	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}
	public Long getReferId() {
		return referId;
	}
	public void setReferId(Long referId) {
		this.referId = referId;
	}
	public String getReferType() {
		return referType;
	}
	public void setReferType(String referType) {
		this.referType = referType;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
