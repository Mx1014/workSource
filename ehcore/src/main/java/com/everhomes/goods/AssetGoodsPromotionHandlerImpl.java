package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.chargingitem.AssetChargingItemProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.print.SiyinPrintPrinter;
import com.everhomes.print.SiyinPrintPrinterProvider;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GetServiceGoodCommand;
import com.everhomes.rest.goods.GetServiceGoodResponse;
import com.everhomes.rest.goods.GoodBizEnum;
import com.everhomes.rest.goods.GoodScopeDTO;
import com.everhomes.rest.goods.GoodTagInfo;
import com.everhomes.rest.goods.TagDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.ASSET_MODULE)
public class AssetGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetGoodsPromotionHandlerImpl.class);
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AssetChargingItemProvider assetChargingItemProvider;
	private static String SCOPE = "范围";
	
	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		if (!isValidGoodListCommand(cmd)) {
			LOGGER.info("not valid good commd :"+(null == cmd ? null : StringHelper.toJsonString(cmd)));
			return new GetGoodListResponse();
		}
		List<GoodTagInfo> goods = new ArrayList<>();
		GoodTagInfo goodTagInfo = cmd.getGoodTagInfo();
		if(goodTagInfo != null) {
			try {
				List<ListChargingItemsDTO> listChargingItemsDTOs = assetChargingItemProvider.listChargingItems(
						"community", Long.parseLong(goodTagInfo.getTag1Key()), null, null, false);
				for(ListChargingItemsDTO chargingItemsDTO : listChargingItemsDTOs){
					if(chargingItemsDTO.getIsSelected().equals((byte)1)){
						GoodTagInfo good = ConvertHelper.convert(cmd.getGoodTagInfo(), GoodTagInfo.class);
						good.setGoodsTag(String.valueOf(chargingItemsDTO.getChargingItemId()));
						good.setGoodsName(chargingItemsDTO.getProjectChargingItemName());
						goods.add(good);
					}
				}
			} catch (Exception e) {
				LOGGER.info("not valid good commd :"+(null == cmd ? null : StringHelper.toJsonString(cmd)));
			}
		}
		GetGoodListResponse resp = new GetGoodListResponse();
		resp.setGoods(goods);
		return resp;
	}	
	
	
	private boolean isValidGoodListCommand(GetGoodListCommand cmd) {
		if (null == cmd 
				|| null == cmd.getNamespaceId() 
				|| null == cmd.getGoodTagInfo() 
				|| null == cmd.getGoodTagInfo().getTag1Key()
				) {
			return false;
		}
		return true;
	}


	@Override
	public GetServiceGoodResponse getServiceGoodsScopes(GetServiceGoodCommand cmd){
		GetServiceGoodResponse response = new GetServiceGoodResponse();
		GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
		List<Community> communities = communityProvider.listNamespaceCommunities(cmd.getNamespaceId());
		List<TagDTO> communitiesList = new ArrayList<>();
		goodScopeDTO.setTitle(SCOPE);
		for (Community community : communities){
			TagDTO communitiy = new TagDTO();
			communitiy.setId(community.getId().toString());
			communitiy.setName(community.getName());
			communitiesList.add(communitiy);
		}
		goodScopeDTO.setTagList(communitiesList);
		response.setGoodScopeDTO(goodScopeDTO);
		return response;
	}
}
