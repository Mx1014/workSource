package com.everhomes.advertisement;

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
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.advertisement.ChangeAdvertisementOrderCommand;
import com.everhomes.rest.advertisement.CreateAdvertisementCommand;
import com.everhomes.rest.advertisement.DeleteAdvertisementCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsResponse;
import com.everhomes.rest.advertisement.UpdateAdvertisementCommand;
import com.everhomes.rest.techpark.expansion.BuildingForRentDTO;
import com.everhomes.rest.techpark.expansion.FindLeasePromotionByIdCommand;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value="Advertisement controller", site="core")
@RestController
@RequestMapping("/advertisement")
public class AdvertisementController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementController.class);
	
	@Autowired
	private AdvertisementService advertisementService;
	
	/**
	 * <b>URL: /advertisement/createAdvertisement</b>
	 * <p>发布招商广告</p>
	 */
	@RequestMapping("createAdvertisement")
	@RestReturn(value=String.class)
	public RestResponse createAdvertisement(CreateAdvertisementCommand cmd){
		advertisementService.createAdvertisement(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /advertisement/deleteAdvertisement</b>
	 * <p>删除招商广告</p>
	 */
	@RequestMapping("deleteAdvertisement")
	@RestReturn(value=String.class)
	public RestResponse deleteAdvertisement(DeleteAdvertisementCommand cmd){
		advertisementService.deleteAdvertisement(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /advertisement/updateAdvertisement</b>
	 * <p>修改招商广告</p>
	 */
	@RequestMapping("updateAdvertisement")
	@RestReturn(value=String.class)
	public RestResponse updateAdvertisement(UpdateAdvertisementCommand cmd){
		advertisementService.updateAdvertisement(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /advertisement/listAdvertisements</b>
	 * <p>查找招商广告</p>
	 */
	@RequestMapping("listAdvertisements")
	@RestReturn(value=ListAdvertisementsResponse.class)
	public RestResponse listAdvertisements(ListAdvertisementsCommand cmd){
		ListAdvertisementsResponse result = advertisementService.listAdvertisements(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /advertisement/changeAdvertisementOrder</b>
	 * <p>查找招商广告</p>
	 */
	@RequestMapping("changeAdvertisementOrder")
	@RestReturn(value=String.class)
	public RestResponse changeAdvertisementOrder(ChangeAdvertisementOrderCommand cmd){
		advertisementService.changeAdvertisementOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

}
