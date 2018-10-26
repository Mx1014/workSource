package com.everhomes.rest.organization.pm.reportForm;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>namespaceId</li>
 *	<li>communityId</li>
 *  <li>buildingIds：buildingId的数组</li>
 *	<li>dateStr : 时间（传年份例子：2018，传月份例子：2018-07）</li>
 *</ul>
 */
public class GetTotalBuildingStaticsCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private List<Long> buildingIds;
	private String dateStr;
	
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
