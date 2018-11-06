package com.everhomes.rest.yellowPage;

/**
 * <ul>
 * <li>type: 联盟类型</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 页码</li>
 * <li>currentPMId: 当前管理公司</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 当前应用originId</li>
 * </ul>
 **/
public class GetFormListCommand extends AllianceAdminCommand {
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
