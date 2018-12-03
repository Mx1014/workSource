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
import com.everhomes.rest.goods.GoodTagInfo;
import com.everhomes.rest.goods.TagDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.YellowPageServiceImpl;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class PrintGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintGoodsPromotionHandlerImpl.class);
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	private static String SCOPE = "范围";
	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		
		if (!isValidGoodListCommand(cmd)) {
			LOGGER.info("not valid good commd :"+(null == cmd ? null : StringHelper.toJsonString(cmd)));
			return new GetGoodListResponse();
		}
		
		// 打印机所有域空间的商品都是一样的 这里直接使用meiju
		GoodBizEnum[] goodBizEnums = GoodBizEnum.values();
		List<GoodTagInfo> goods = new ArrayList<>();
		for (int i = 0; i < goodBizEnums.length; i++) {
			if (GoodBizEnum.TYPE_SIYIN_PRINT.equals(goodBizEnums[i].getType())) {
				GoodTagInfo good = ConvertHelper.convert(cmd.getGoodTagInfo(), GoodTagInfo.class);
				good.setGoodsTag(goodBizEnums[i].getIdentity());
				good.setGoodsName(goodBizEnums[i].getName());
				goods.add(good);
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
				|| null == cmd.getGoodTagInfo().getTag2Key()
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
			List<TagDTO> printerList = new ArrayList<>();
			List<SiyinPrintPrinter> SiyinPrintPrinters = siyinPrintPrinterProvider.findSiyinPrintPrinterByNamespaceId(cmd.getNamespaceId());
			for(SiyinPrintPrinter siyinPrintPrinter : SiyinPrintPrinters){
				TagDTO printer = new TagDTO();
				printer.setId(siyinPrintPrinter.getPrinterName());
				printer.setName(siyinPrintPrinter.getPrinterName());
				printerList.add(printer);
			}
			
			TagDTO communitiy = new TagDTO();
			communitiy.setId(community.getId().toString());
			communitiy.setName(community.getName());
			communitiy.setTags(printerList);
			communitiesList.add(communitiy);
		}
		goodScopeDTO.setTagList(communitiesList);
		response.setGoodScopeDTO(goodScopeDTO);
		return response;
	}
}
