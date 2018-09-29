package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parents : 营销所需的运营商列表 {@link com.everhomes.rest.goods.GoodDTO}</li>
 * <li>goods : 商品列表 {@link com.everhomes.rest.goods.GoodDTO}</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月29日
 */
public class GetGoodListResponse {
	
	private List<GoodDTO> parents;
	private List<GoodDTO> goods;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public List<GoodDTO> getParents() {
		return parents;
	}
	public void setParents(List<GoodDTO> parents) {
		this.parents = parents;
	}
	public List<GoodDTO> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodDTO> goods) {
		this.goods = goods;
	}

}
