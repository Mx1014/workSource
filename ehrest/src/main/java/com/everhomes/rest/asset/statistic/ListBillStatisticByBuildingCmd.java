//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

/**
 *<ul>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 * <li>namespaceId: 与空间</li>
 * <li>ownerType: 所属项目类型</li>
 * <li>ownerId: 所属项目ID</li>
 * <li>buildingNameList: 楼宇名称列表</li>
 * <li>dateStrBegin: 账期范围</li>
 * <li>dateStrEnd: 账期范围</li>
 *</ul>
 */
public class ListBillStatisticByBuildingCmd{
	private Integer pageSize;
    private Long pageAnchor;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private List<String> buildingNameList;
	private String dateStrBegin;
	private String dateStrEnd;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
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
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public List<String> getBuildingNameList() {
		return buildingNameList;
	}
	public void setBuildingNameList(List<String> buildingNameList) {
		this.buildingNameList = buildingNameList;
	}
	
}
