package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：EhOrganizations EhCommunities</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>categoryId: 服务联盟大类id</li>
 *  <li>startDay: 开始时间</li>
 *  <li>endDay：结束时间</li>
 *  <li>keyword：关键字（创建请求的用户姓名和机构名称）</li>
 *  <li>templateType：申请类型 ServiceAlliance/Settle</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class SearchRequestInfoCommand {

	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private Long categoryId;
	
	private Long startDay;
	
	private Long endDay;
	
	private String keyword;
	
	private String templateType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getStartDay() {
		return startDay;
	}

	public void setStartDay(Long startDay) {
		this.startDay = startDay;
	}

	public Long getEndDay() {
		return endDay;
	}

	public void setEndDay(Long endDay) {
		this.endDay = endDay;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
