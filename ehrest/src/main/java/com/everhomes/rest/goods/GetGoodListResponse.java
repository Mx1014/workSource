package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.rest.RestResponse;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>goods : 商品列表 {@link com.everhomes.rest.goods.GoodTagDTO}</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月10日
 */
public class GetGoodListResponse extends RestResponse{
	private List<GoodTagDTO> goods;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public List<GoodTagDTO> getGoods() {
		return goods;
	}


	public void setGoods(List<GoodTagDTO> goods) {
		this.goods = goods;
	}
}
