package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GetServiceGoodCommand;
import com.everhomes.rest.goods.GetServiceGoodResponse;
import com.everhomes.rest.goods.GoodScopeDTO;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.PARKING_MODULE)
public class ParkingGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private ParkingProvider parkingProvider;
	private static String SCOPE = "范围";
	
	@Override
	public GetServiceGoodResponse getServiceGoodsScopes(GetServiceGoodCommand cmd){
		GetServiceGoodResponse response = new GetServiceGoodResponse();
		GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
		List<Community> communities = communityProvider.listNamespaceCommunities(cmd.getNamespaceId());
		List<String> communitiesList = new ArrayList();
		goodScopeDTO.setTitle(SCOPE);
		for (Community community : communities){
			String communitiy;
			JSONObject communitityJson=new JSONObject();
			communitityJson.put("id", community.getId());
			communitityJson.put("name", community.getName());
			communitityJson.put("title", SCOPE);
			List<String> parkingLotList = new ArrayList();
			String ownerType = "communities";
			List<ParkingLot> parkinglots = parkingProvider.listParkingLots(ownerType, community.getId());
			for(ParkingLot parkingLot : parkinglots){
				String parking;
				JSONObject parkingJson=new JSONObject();
				parkingJson.put("id", parkingLot.getId().toString());
				parkingJson.put("name", parkingLot.getName());
				parkingJson.put("serveApplyName", community.getName()+ "-" + parkingLot.getName());
				communitityJson.put("title", SCOPE);
				parking = parkingJson.toString();
				parkingLotList.add(parking);
			}
			communitityJson.put("tag", parkingLotList);
			communitiy = communitityJson.toString();
			communitiesList.add(communitiy);
		}
		goodScopeDTO.setTagList(communitiesList);
		return response;
	}

	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
