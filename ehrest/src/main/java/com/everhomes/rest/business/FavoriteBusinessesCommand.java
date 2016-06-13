package com.everhomes.rest.business;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bizs: 店铺是否收藏对象列表，{id:business's id,favoriteFlag:0-取消收藏,1-收藏},详情:{@link com.everhomes.rest.business.FavoriteBusinessDTO}</li>
 * </ul>
 */
public class FavoriteBusinessesCommand {
	
	@ItemType(FavoriteBusinessDTO.class)
    private List<FavoriteBusinessDTO> bizs;

	public List<FavoriteBusinessDTO> getBizs() {
		return bizs;
	}

	public void setBizs(List<FavoriteBusinessDTO> bizs) {
		this.bizs = bizs;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	

}
