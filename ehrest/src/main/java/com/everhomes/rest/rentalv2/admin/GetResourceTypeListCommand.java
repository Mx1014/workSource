package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询资源列表
 * <li>communityId: 园区id-暂时不做</li>
 * <li>namespaceId: 域空间</li>
 * <li>status: 菜单dataType 后面添加一个status值 CUSTOM(3)表示是内部会议室 {@link com.everhomes.rest.rentalv2.admin.ResourceTypeStatus}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize:每页数量 </li>
 * </ul>
 */
public class GetResourceTypeListCommand {

	private Integer namespaceId;
	private Long communityId;

	private Byte status;

	private Long pageAnchor;
    
	private Integer pageSize;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
