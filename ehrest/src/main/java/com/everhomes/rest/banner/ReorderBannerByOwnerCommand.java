// @formatter:off
package com.everhomes.rest.banner;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.banner.admin.UpdateBannerAdminCommand;

/**
 * <ul>
 *   <li>ownerType: 归属者类型 {@link com.everhomes.rest.banner.BannerOwnerType}</li>
 *   <li>ownerId: 归属者id</li>
 * 	 <li>banners: 批量更新的banner列表{@link com.everhomes.rest.banner.UpdateBannerByOwnerCommand}</li>
 * </ul>
 */
public class ReorderBannerByOwnerCommand {
	
	private String   ownerType;
	private Long     ownerId;
	@ItemType(UpdateBannerAdminCommand.class)
	private List<UpdateBannerByOwnerCommand> banners;

	public List<UpdateBannerByOwnerCommand> getBanners() {
		return banners;
	}

	public void setBanners(List<UpdateBannerByOwnerCommand> banners) {
		this.banners = banners;
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
	
}
