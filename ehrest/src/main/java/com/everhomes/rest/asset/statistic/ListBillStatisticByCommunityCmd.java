//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

/**
 *<ul>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 * <li>namespaceId: 与空间</li>
 * <li>ownerType: 所属项目类型</li>
 * <li>ownerIdList: 所属项目id列表</li>
 * <li>dateStrBegin: 账期范围</li>
 * <li>dateStrEnd: 账期范围</li>
 *</ul>
 */
public class ListBillStatisticByCommunityCmd{
	private Integer pageSize;
    private Long pageAnchor;
	private Integer namespaceId;
	private String ownerType;
	private List<Long> ownerIdList;
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
	public List<Long> getOwnerIdList() {
		return ownerIdList;
	}
	public void setOwnerIdList(List<Long> ownerIdList) {
		this.ownerIdList = ownerIdList;
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
	
}
