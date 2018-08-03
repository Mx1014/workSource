package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>buildingId: 楼栋id</li>
 *     <li>keyWords: 搜索关键字</li>
 *     <li>nextPageAnchor: 下页的锚点</li>
 *     <li>pageSize: 每页显示的数量</li>
 * </ul>
 */
public class ListBuildingsByKeywordsCommand {
	
	private Long communityId;
	private Long buildingId;
	private String keyWords;
	private Long nextPageAnchor;
	private Integer pageSize;
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
