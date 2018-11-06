package com.everhomes.rest.yellowPage;

public class GetWorkFlowListCommand extends AllianceAdminCommand {

	private Long type;
	private Long pageAnchor;
	private Integer pageSize;

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
