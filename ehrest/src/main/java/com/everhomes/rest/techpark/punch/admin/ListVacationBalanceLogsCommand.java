// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>detailId: 员工detailid</li>
 * <li>pageSize: 每页条数</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 */
public class ListVacationBalanceLogsCommand {

	private Long organizationId;

	private Long detailId;

	private Integer pageSize;

	private Long pageAnchor;

	public ListVacationBalanceLogsCommand() {

	}

	public ListVacationBalanceLogsCommand(Long organizationId, Long detailId, Integer pageSize, Long pageAnchor) {
		super();
		this.organizationId = organizationId;
		this.detailId = detailId;
		this.pageSize = pageSize;
		this.pageAnchor = pageAnchor;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
