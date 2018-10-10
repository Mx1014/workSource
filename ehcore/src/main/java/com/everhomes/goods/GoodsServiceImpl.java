package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.print.SiyinPrintPrinter;
import com.everhomes.print.SiyinPrintPrinterProvider;
import com.everhomes.rest.goods.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class GoodsServiceImpl implements GoodsService{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	
	@Override
	public List<GoodDTO> getGoodList(GetGoodListCommand cmd) {
		ServiceModuleAppDTO appDto = getAppInfoByAppOriginId(cmd.getAppOriginId());
		if(null == appDto) {
			LOGGER.error("appDto not exist GetGoodListCommand = {}", cmd.toString());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "module app not exist");
		}
		
		GoodsPromotionHandler handler = getGoodsPromotionHandler(appDto.getModuleId());
		
		//传递应用信息
		cmd.setModuleAppDTO(appDto);
		return handler.getGoodList(cmd);
	}
	
	@Override
	public GoodsPromotionHandler getGoodsPromotionHandler(Long moduleId) {
		
		GoodsPromotionHandler handler = null;

		if(moduleId != null && moduleId.longValue() > 0) {
			String handlerPrefix = GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX;
			try {
				handler = PlatformContextNoWarnning.getComponent(handlerPrefix + moduleId);
			}catch (Exception ex){
				LOGGER.error("getGoodsPromotionHandler not exist moduleId = {}", moduleId);
			}
		}

		return handler;
	}

	@Override 
	public GetServiceGoodResponse getServiceGoodList(GetServiceGoodCommand cmd) {
		GetServiceGoodResponse response = new GetServiceGoodResponse();
		GoodScopeDTO goodScopeDTO = getServiceGoodsScopes(cmd.getServiceType(),cmd.getNamespaceId());
		response.setGoodScopeDTO(goodScopeDTO);
		return response;
	}
	
	private GoodScopeDTO getServiceGoodsScopes(Byte serviceType, Integer namespaceId){
		GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
		List<Community> communities = communityProvider.listNamespaceCommunities(namespaceId);
		List<String> communitiesList = new ArrayList();

		
		switch (serviceType) {
		//停车
		case 2 :
			for (Community community : communities){
				String communitiy;
				JSONObject communitityJson=new JSONObject();
				communitityJson.put("id", community.getId());
				communitityJson.put("name", community.getName());
				List<String> parkingLotList = new ArrayList();
				String ownerType = "communities";
				List<ParkingLot> parkinglots = parkingProvider.listParkingLots(ownerType, community.getId());
				for(ParkingLot parkingLot : parkinglots){
					String parking;
					JSONObject parkingJson=new JSONObject();
					parkingJson.put("id", parkingLot.getId().toString());
					parkingJson.put("name", parkingLot.getName());
					parking = parkingJson.toString();
					parkingLotList.add(parking);
				}
				communitityJson.put("tag", parkingLotList);
				communitiy = communitityJson.toString();
				communitiesList.add(communitiy);
			}
			goodScopeDTO.setTagList(communitiesList);
			break;
			//云打印
		case 3 : 
			for (Community community : communities){
				String communitiy;
				JSONObject communitityJson=new JSONObject();
				communitityJson.put("id", community.getId().toString());
				communitityJson.put("name", community.getName());
				List<String> printerList = new ArrayList();
				List<SiyinPrintPrinter> SiyinPrintPrinters = siyinPrintPrinterProvider.findSiyinPrintPrinterByOwnerId(community.getId());
				for(SiyinPrintPrinter siyinPrintPrinter : SiyinPrintPrinters){
					String printer;
					JSONObject printerJson=new JSONObject();
					printerJson.put("id", siyinPrintPrinter.getId());
					printerJson.put("name", siyinPrintPrinter.getReaderName());
					printer = printerJson.toString();
					printerList.add(printer);
				}
				communitityJson.put("tag", printerList);
				communitiy = communitityJson.toString();
				communitiesList.add(communitiy);
			}
			goodScopeDTO.setTagList(communitiesList);
			break;
		}
		return goodScopeDTO;
	}
	
	private ServiceModuleAppDTO getAppInfoByAppOriginId(Long appOriginId) {

		ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(appOriginId);
		if (null == app) {
			return null;
		}

		return ConvertHelper.convert(app, ServiceModuleAppDTO.class);
	}
}
