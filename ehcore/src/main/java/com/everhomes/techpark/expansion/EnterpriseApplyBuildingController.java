package com.everhomes.techpark.expansion;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateByFormIdCommand;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestDoc(value = "entry controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/entry")
public class EnterpriseApplyBuildingController extends ControllerBase{

	@Autowired
	private EnterpriseApplyBuildingService enterpriseApplyBuildingService;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private GeneralFormService generalFormService;
	
	/**
	 * <b>URL: /techpark/entry/listLeaseBuildings
	 * <p>项目介绍列表
	 */
	@RequestMapping("listLeaseBuildings")
	@RestReturn(value=ListLeaseBuildingsResponse.class)
	public RestResponse listLeaseBuildings(ListLeaseBuildingsCommand cmd){

		RestResponse response = new RestResponse(enterpriseApplyBuildingService.listLeaseBuildings(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/createLeaseBuilding
	 * <p>创建项目介绍
	 */
	@RequestMapping("createLeaseBuilding")
	@RestReturn(value=LeaseBuildingDTO.class)
	public RestResponse createLeaseBuilding(CreateLeaseBuildingCommand cmd){

		RestResponse response = new RestResponse(enterpriseApplyBuildingService.createLeaseBuilding(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/updateLeaseBuilding
	 * <p>修改项目介绍
	 */
	@RequestMapping("updateLeaseBuilding")
	@RestReturn(value=LeaseBuildingDTO.class)
	public RestResponse updateLeaseBuilding(UpdateLeaseBuildingCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyBuildingService.updateLeaseBuilding(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/getLeaseBuildingById
	 * <p>根据id查询项目介绍
	 */
	@RequestMapping("getLeaseBuildingById")
	@RestReturn(value=LeaseBuildingDTO.class)
	@RequireAuthentication(false)
	public RestResponse getLeaseBuildingById(GetLeaseBuildingByIdCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyBuildingService.getLeaseBuildingById(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/deleteLeaseBuilding
	 * <p>删除项目介绍
	 */
	@RequestMapping("deleteLeaseBuilding")
	@RestReturn(value=String.class)
	public RestResponse deleteLeaseBuilding(DeleteLeaseBuildingCommand cmd){

		enterpriseApplyBuildingService.deleteLeaseBuilding(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateLeaseBuildingOrder
	 * <p>修改项目介绍排序
	 */
	@RequestMapping("updateLeaseBuildingOrder")
	@RestReturn(value=String.class)
	public RestResponse updateLeaseBuildingOrder(UpdateLeaseBuildingOrderCommand cmd){

		enterpriseApplyBuildingService.updateLeaseBuildingOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
