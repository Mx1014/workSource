package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>contactType: 推送类型</li>
 *  <li>categoryId: 服务联盟大类id</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNotifyTargetsCommand {

	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private Long categoryId;
	
	private Byte contactType;
	
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

	public Byte getContactType() {
		return contactType;
	}

	public void setContactType(Byte contactType) {
		this.contactType = contactType;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
