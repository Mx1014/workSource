package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

		switch (cmd.getCategoryId()) {
		case "ALL" :
			GoodScopeDTO goodScopeDTO = getServiceGoodsScopes(cmd.getServiceType(),cmd.getNamespaceId());
			response.setGoodScopeDTO(goodScopeDTO);
			break;
//		case "CATEGROY" :
//			getServiceGoodsCategories();
//			break;
//		case "GOODS" :
//			getServiceGoodsList();
//			break;
		default :
			break;
		}
		return response;
	}
	
	private GoodScopeDTO getServiceGoodsScopes(String serviceType, Integer namespaceId){
		GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
		List<Community> communities = communityProvider.listNamespaceCommunities(namespaceId);
		List<TagDTO> communitiesList = new ArrayList();
		switch (serviceType) {
		case "停车缴费" :
			for (Community community : communities){
				TagDTO communitiyDTO = new TagDTO();
				communitiyDTO.setId(community.getId());
				communitiyDTO.setName(community.getName());
				List<TagDTO> parkingLotList = new ArrayList();
				String ownerType = "communities";
				List<ParkingLot> parkinglots = parkingProvider.listParkingLots(ownerType, community.getId());
				for(ParkingLot parkingLot : parkinglots){
					TagDTO parkingDTO = new TagDTO();
					parkingDTO.setId(parkingLot.getId());
					parkingDTO.setName(parkingLot.getName());
					parkingLotList.add(parkingDTO);
				}
				communitiyDTO.setTagList(parkingLotList);
				communitiesList.add(communitiyDTO);
			}
			goodScopeDTO.setTagList(communitiesList);
			break;
		case "云打印" : 
			for (Community community : communities){
				TagDTO communitiyDTO = new TagDTO();
				communitiyDTO.setId(community.getId());
				communitiyDTO.setName(community.getName());
				List<TagDTO> printerList = new ArrayList();
				List<SiyinPrintPrinter> SiyinPrintPrinters = siyinPrintPrinterProvider.findSiyinPrintPrinterByOwnerId(community.getId());
				for(SiyinPrintPrinter siyinPrintPrinter : SiyinPrintPrinters){
					TagDTO printerDTO = new TagDTO();
					printerDTO.setId(siyinPrintPrinter.getId());
					printerDTO.setName(siyinPrintPrinter.getReaderName());
					printerList.add(printerDTO);
				}
				communitiyDTO.setTagList(printerList);
				communitiesList.add(communitiyDTO);
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
