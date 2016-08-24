// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: 归属者类型 {@link com.everhomes.rest.banner.BannerOwnerType}</li>
 * <li>ownerId: 归属者id</li>
 * <li>scope: 可见范围 {@link com.everhomes.rest.banner.BannerScope}</li>
 * <li>pageAnchor: 下一页开始的锚点</li>
 * <li>pageSize: 每页大小</li>
 * </li>
 */
public class ListBannersByOwnerCommand {
	@NotNull
	private String  ownerType;
	@NotNull
	private Long    ownerId;
	@NotNull
	private BannerScope scope;
	private Long    pageAnchor;
	private Integer pageSize;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public BannerScope getScope() {
		return scope;
	}

	public void setScope(BannerScope scope) {
		this.scope = scope;
	}

}
