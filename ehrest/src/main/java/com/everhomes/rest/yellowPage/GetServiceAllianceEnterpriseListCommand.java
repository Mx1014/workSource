package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型 参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceBelongType}</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>keywords: 关键字 企业名称</li>
 *  <li>parentId: 父id</li>
 *  <li>type:类型  </li>
 *  <li>categoryId: 所属服务联盟类型id</li>
 *  <li>nextPageAnchor: 下一页锚点</li>
 *  <li>pageSize: 每页的数量</li>
 *  <li>sourceRequestType: 客户端或者web端 ,web端请务必传值。 参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceSourceRequestType}</li>
 * </ul>
 */
public class GetServiceAllianceEnterpriseListCommand {

	private Long communityId;
	
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private String keywords;

	private Long parentId;

	private Long nextPageAnchor;

	private Integer pageSize;

	private Long categoryId;
	
	private Long type;
	
	private Byte sourceRequestType;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Byte getSourceRequestType() {
		return sourceRequestType;
	}

	public void setSourceRequestType(Byte sourceRequestType) {
		this.sourceRequestType = sourceRequestType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
