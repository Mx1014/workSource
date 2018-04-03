package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerId: 标准类型所属组织等的id</li>
 *  <li>ownerType: 标准类型所属组织类型，如enterprise</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>parentId: 父节点id。全要则不填</li>
 *  <li>targetId: 项目id</li>
 * </ul>
 */
public class ListQualityCategoriesCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long parentId;

	private Long targetId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
