package com.everhomes.rest.ui.launchpad;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.business.FavoriteBusinessDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bizs: 店铺是否收藏对象列表，{id:business's id,favoriteFlag:0-取消收藏,1-收藏},详情:{@link com.everhomes.rest.business.FavoriteBusinessDTO}</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class FavoriteBusinessesBySceneCommand {
	
	private String sceneToken;
	
	@ItemType(FavoriteBusinessDTO.class)
    private List<FavoriteBusinessDTO> bizs;
	
	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

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
