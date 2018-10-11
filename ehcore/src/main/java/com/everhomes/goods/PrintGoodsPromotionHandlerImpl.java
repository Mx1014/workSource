package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.print.SiyinPrintPrinter;
import com.everhomes.print.SiyinPrintPrinterProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GetServiceGoodCommand;
import com.everhomes.rest.goods.GetServiceGoodResponse;
import com.everhomes.rest.goods.GoodBizEnum;
import com.everhomes.rest.goods.GoodScopeDTO;
import com.everhomes.rest.goods.GoodTagDTO;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class PrintGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	private static String SCOPE = "范围";
	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		//打印机所有域空间的商品都是一样的 这里直接使用meiju
		GoodBizEnum[] goodBizEnums = GoodBizEnum.values();
		List<GoodTagDTO> goods = new ArrayList<>();
		for (int i = 0 ; i < goodBizEnums.length; i++) {
			if (GoodBizEnum.TYPE_SIYIN_PRINT.equals(goodBizEnums[i].getType())) {
				GoodTagDTO good = new GoodTagDTO();
				good.setGoodTagKey(goodBizEnums[i].getIdentity());
				good.setGoodTagValue(goodBizEnums[i].getName());
				goods.add(good);
			}
		}
		GetGoodListResponse resp = new GetGoodListResponse();
		resp .setGoods(goods);
		return resp;
	}	
	
	@Override
	public GetServiceGoodResponse getServiceGoodsScopes(GetServiceGoodCommand cmd){
		GetServiceGoodResponse response = new GetServiceGoodResponse();
		GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
		List<Community> communities = communityProvider.listNamespaceCommunities(cmd.getNamespaceId());
		List<String> communitiesList = new ArrayList();
		goodScopeDTO.setTitle(SCOPE);
		
//		switch (c,d) {
//		//停车
//		case 2 :
//			for (Community community : communities){
//				String communitiy;
//				JSONObject communitityJson=new JSONObject();
//				communitityJson.put("id", community.getId());
//				communitityJson.put("name", community.getName());
//				communitityJson.put("title", SCOPE);
//				List<String> parkingLotList = new ArrayList();
//				String ownerType = "communities";
//				List<ParkingLot> parkinglots = parkingProvider.listParkingLots(ownerType, community.getId());
//				for(ParkingLot parkingLot : parkinglots){
//					String parking;
//					JSONObject parkingJson=new JSONObject();
//					parkingJson.put("id", parkingLot.getId().toString());
//					parkingJson.put("name", parkingLot.getName());
//					communitityJson.put("title", SCOPE);
//					parking = parkingJson.toString();
//					parkingLotList.add(parking);
//				}
//				communitityJson.put("tag", parkingLotList);
//				communitiy = communitityJson.toString();
//				communitiesList.add(communitiy);
//			}
//			goodScopeDTO.setTagList(communitiesList);
//			break;
//			//云打印
//		case 3 : 
		for (Community community : communities){
			String communitiy;
			JSONObject communitityJson=new JSONObject();
			communitityJson.put("id", community.getId().toString());
			communitityJson.put("name", community.getName());
			List<String> printerList = new ArrayList();
			List<SiyinPrintPrinter> SiyinPrintPrinters = siyinPrintPrinterProvider.findSiyinPrintPrinterByNamespaceId(cmd.getNamespaceId());
			for(SiyinPrintPrinter siyinPrintPrinter : SiyinPrintPrinters){
				String printer;
				JSONObject printerJson=new JSONObject();
				printerJson.put("id", siyinPrintPrinter.getId());
				printerJson.put("serveApplyName", community.getName()+ "-" + siyinPrintPrinter.getPrinterName());
				printer = printerJson.toString();
				printerList.add(printer);
			}
			communitityJson.put("tag", printerList);
			communitiy = communitityJson.toString();
			communitiesList.add(communitiy);
		}
		goodScopeDTO.setTagList(communitiesList);
		response.setGoodScopeDTO(goodScopeDTO);
		return response;
	}
}
