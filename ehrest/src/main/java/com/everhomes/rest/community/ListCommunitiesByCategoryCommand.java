package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * 
 * <p>
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>keywords: 小区关键字</li>
 * <li>cityId: 城市id</li>
 * <li>areaId: 区县id</li>
 * <li>categoryId: 分类id</li>
 * </ul>
 *
 */
public class ListCommunitiesByCategoryCommand {

	private String ownerType;

	private String ownerId;

	private Long pageAnchor;
	
	private Integer pageSize;

	private Long cityId;

	private Long areaId;

	private Long categoryId;

	private String keywords;

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

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
