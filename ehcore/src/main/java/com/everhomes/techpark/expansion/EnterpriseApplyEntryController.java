package com.everhomes.techpark.expansion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value = "entry controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/entry")
public class EnterpriseApplyEntryController extends ControllerBase{

	@Autowired
	private EnterpriseApplyEntryService enterpriseApplyEntryService;
	
	/**
	 * <b>URL: /techpark/entry/listEnterpriseExpansions
	 * <p>企业列表
	 */
	@RequestMapping("listEnterpriseDetails")
	@RestReturn(value=ListEnterpriseDetailResponse.class)
	public RestResponse listEnterpriseDetails(ListEnterpriseDetailCommand cmd){
		ListEnterpriseDetailResponse res = enterpriseApplyEntryService.listEnterpriseDetails(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/getEnterpriseExpansionById
	 * <p>获取企业信息
	 */
	@RequestMapping("getEnterpriseDetailById")
	@RestReturn(value=GetEnterpriseDetailByIdResponse.class)
	public RestResponse getEnterpriseDetailById(GetEnterpriseDetailByIdCommand cmd){
		GetEnterpriseDetailByIdResponse res = enterpriseApplyEntryService.getEnterpriseDetailById(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/listApplyEntrys
	 * <p>
	 */
	@RequestMapping("listApplyEntrys")
	@RestReturn(value=EnterpriseApplyEntryResponse.class)
	public RestResponse listApplyEntrys(ListEnterpriseApplyEntryCommand cmd){
		ListEnterpriseApplyEntryResponse res = enterpriseApplyEntryService.listApplyEntrys(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/parkApplyEntry
	 * <p>
	 */
	@RequestMapping("applyEntry")
	@RestReturn(value=String.class)
	public RestResponse applyEntry(EnterpriseApplyEntryCommand cmd){
		boolean b = enterpriseApplyEntryService.applyEntry(cmd);
		RestResponse response = new RestResponse(b);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/applyRenew
	 * <p>
	 */
	@RequestMapping("applyRenew")
	@RestReturn(value=String.class)
	public RestResponse applyRenew(EnterpriseApplyRenewCommand cmd){
		boolean b = enterpriseApplyEntryService.applyRenew(cmd);
		RestResponse response = new RestResponse(b);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/listForRents
	 * <p>
	 */
	@RequestMapping("listForRents")
	@RestReturn(value=ListBuildingForRentResponse.class)
	public RestResponse listForRents(ListBuildingForRentCommand cmd){
		ListBuildingForRentResponse res = enterpriseApplyEntryService.listLeasePromotions(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
}
