// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * <li>ownerType: 归属者类型 {@link com.everhomes.rest.banner.BannerOwnerType}</li>
 * <li>ownerId: 归属者id</li>
 * <li>scope: 可见范围</li>
 * </ul>
 */
public class DeleteBannerByOwnerCommand {
	@NotNull
    private Long    id;
	@NotNull
    private String  ownerType;
	@NotNull
	private Long    ownerId;
	@NotNull
	private BannerScope scope;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
