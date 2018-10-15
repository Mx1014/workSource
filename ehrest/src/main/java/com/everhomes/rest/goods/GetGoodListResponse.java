package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.rest.RestResponse;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>goods : 商品列表 {@link com.everhomes.rest.goods.GoodTagInfo}</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月10日
 */
public class GetGoodListResponse{
	private List<GoodTagInfo> goods;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public List<GoodTagInfo> getGoods() {
		return goods;
	}


	public void setGoods(List<GoodTagInfo> goods) {
		this.goods = goods;
	}
}
