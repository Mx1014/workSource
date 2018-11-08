//@formatter:off
package com.everhomes.rest.asset.bill;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>communityId:项目ID(左邻的项目id),如何不传，可以跨园区获取数据，否则获取的是该园区下面的数据信息</li>
 * <li>pageAnchor:锚点，获取从该点pageSize条数据</li>
 * <li>pageSize:每页大小(最大值为1000)，每次请求获取的数据条数</li>
 * <li>updateTime:更新时间，为空全量同步数据，不为空是增量同步（该时间点以后的数据信息），使用2018-11-08 16:30:00</li>
 *</ul>
 */
public class ListOpenBillsCommand {
	private Integer namespaceId;
    private Long communityId;
    private Long pageAnchor;
    private Integer pageSize;
    private String updateTime;
    
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
