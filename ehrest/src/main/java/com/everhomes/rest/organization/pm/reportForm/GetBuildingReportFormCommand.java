package com.everhomes.rest.organization.pm.reportForm;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>namespaceId</li>
 *	<li>communityId</li>
 *  <li>buildingIds：buildingId的数组</li>
 *	<li>dateStr : 时间（传年份例子：2018，传月份例子：2018-07）</li>
 *	<li>pageSize</li>
 *	<li>pageAnchor</li>
 *</ul>
 */
public class GetBuildingReportFormCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private List<Long> buildingIds;
	private String dateStr;
	private Integer pageSize;
	private Long pageAnchor;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public List<Long> getBuildingIds() {
		return buildingIds;
	}
	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
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
