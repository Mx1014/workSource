package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>goodScopeDTO : 商品结构DTO</li>
 * <li>goodCategoryList : 项目id</li>
 * <li>goodDTOList : 应用originId </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class GetServiceGoodResponse {
	
	private GoodScopeDTO goodScopeDTO;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	
	public GoodScopeDTO getGoodScopeDTO() {
		return goodScopeDTO;
	}

	public void setGoodScopeDTO(GoodScopeDTO goodScopeDTO) {
		this.goodScopeDTO = goodScopeDTO;
	}

//	public List<GoodCategoryDTO> getGoodCategoryList() {
//		return goodCategoryList;
//	}
//
//	public void setGoodCategoryList(List<GoodCategoryDTO> goodCategoryList) {
//		this.goodCategoryList = goodCategoryList;
//	}
//
//	public List<GoodDTO> getGoodDTOList() {
//		return goodDTOList;
//	}
//
//	public void setGoodDTOList(List<GoodDTO> goodDTOList) {
//		this.goodDTOList = goodDTOList;
//	}
	
	
}
