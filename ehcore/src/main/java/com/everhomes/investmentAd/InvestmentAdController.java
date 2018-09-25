package com.everhomes.investmentAd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.investmentAd.InvestmentAdService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;
import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.ChangeInvestmentStatusCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetRelatedAssetsCommand;
import com.everhomes.rest.investmentAd.IntentionCustomerCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.RelatedAssetDTO;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;


@RestDoc(value="InvestmentAd Controller", site="core")
@RestController
@RequestMapping("/investmentAd")
public class InvestmentAdController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdController.class);
	
	@Autowired
	private InvestmentAdService investmentAdService;
	
	/**
	 * <b>URL: /investmentAd/createInvestmentAd</b>
	 * <p>发布招商广告</p>
	 */
	@RequestMapping("createInvestmentAd")
	@RestReturn(value=String.class)
	public RestResponse createInvestmentAd(CreateInvestmentAdCommand cmd){
		investmentAdService.createInvestmentAd(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/deleteInvestmentAd</b>
	 * <p>删除招商广告</p>
	 */
	@RequestMapping("deleteInvestmentAd")
	@RestReturn(value=String.class)
	public RestResponse deleteInvestmentAd(DeleteInvestmentAdCommand cmd){
		investmentAdService.deleteInvestmentAd(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/updateInvestmentAd</b>
	 * <p>修改招商广告</p>
	 */
	@RequestMapping("updateInvestmentAd")
	@RestReturn(value=String.class)
	public RestResponse updateInvestmentAd(UpdateInvestmentAdCommand cmd){
		investmentAdService.updateInvestmentAd(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/listInvestmentAds</b>
	 * <p>查找招商广告列表</p>
	 */
	@RequestMapping("listInvestmentAds")
	@RestReturn(value=ListInvestmentAdResponse.class)
	public RestResponse listInvestmentAds(ListInvestmentAdCommand cmd){
		ListInvestmentAdResponse result = investmentAdService.listInvestmentAds(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/getInvestmentAd</b>
	 * <p>查找招商广告</p>
	 */
	@RequestMapping("getInvestmentAd")
	@RestReturn(value=InvestmentAdDetailDTO.class)
	public RestResponse getInvestmentAd(GetInvestmentAdCommand cmd){
		InvestmentAdDetailDTO result = investmentAdService.getInvestmentAd(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/changeInvestmentAdOrder</b>
	 * <p>招商广告排序</p>
	 */
	@RequestMapping("changeInvestmentAdOrder")
	@RestReturn(value=String.class)
	public RestResponse changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd){
		investmentAdService.changeInvestmentAdOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /investmentAd/exportInvestmentAds</b>
	 * <p>导出招商广告</p>
	 */
	@RequestMapping("exportInvestmentAds")
	@RestReturn(value=String.class)
	public RestResponse exportInvestmentAds(ListInvestmentAdCommand cmd){
		investmentAdService.exportInvestmentAds(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/changeInvestmentStatus</b>
	 * <p>更改招商状态</p>
	 */
	@RequestMapping("changeInvestmentStatus")
	@RestReturn(value=String.class)
	public RestResponse changeInvestmentStatus(ChangeInvestmentStatusCommand cmd){
		investmentAdService.changeInvestmentStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/transformToCustomer</b>
	 * <p>转为意向客户（房源招商）</p>
	 */
	@RequestMapping("transformToCustomer")
	@RestReturn(value=Long.class,collection=true)
	public RestResponse transformToCustomer(IntentionCustomerCommand cmd){
		List<Long> result = investmentAdService.transformToCustomer(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /investmentAd/getRelatedAssets</b>
	 * <p>转为意向客户（房源招商）</p>
	 */
	@RequestMapping("getRelatedAssets")
	@RestReturn(value=RelatedAssetDTO.class,collection=true)
	public RestResponse getRelatedAssets(GetRelatedAssetsCommand cmd){
		List<RelatedAssetDTO> result = investmentAdService.getRelatedAssets(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
