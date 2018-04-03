package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.business.ShopDTO;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>dtos: 查询的帖子的结果列表，{@link com.everhomes.rest.ui.user.ContentBriefDTO}</li>
 * <li>launchPadItemDtos: 查询应用的结果列表，{@link com.everhomes.rest.launchpad.LaunchPadItemDTO}</li>
 * <li>shopDTOs: 查询商家的结果列表，{@link com.everhomes.rest.business.ShopDTO}</li>
 * <li>nextPageAnchor: 下一页的锚点</li>
 *</ul>
 */
public class SearchContentsBySceneReponse {
	@ItemType(ContentBriefDTO.class)
	private List<ContentBriefDTO> dtos;
	
	@ItemType(LaunchPadItemDTO.class)
	private List<LaunchPadItemDTO> launchPadItemDtos;
	
	@ItemType(ShopDTO.class)
	private List<ShopDTO> shopDTOs;
	
	
	
	private Long nextPageAnchor;

	public List<ContentBriefDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ContentBriefDTO> dtos) {
		this.dtos = dtos;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<LaunchPadItemDTO> getLaunchPadItemDtos() {
		return launchPadItemDtos;
	}

	public void setLaunchPadItemDtos(List<LaunchPadItemDTO> launchPadItemDtos) {
		this.launchPadItemDtos = launchPadItemDtos;
	}

	public List<ShopDTO> getShopDTOs() {
		return shopDTOs;
	}

	public void setShopDTOs(List<ShopDTO> shopDTOs) {
		this.shopDTOs = shopDTOs;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
