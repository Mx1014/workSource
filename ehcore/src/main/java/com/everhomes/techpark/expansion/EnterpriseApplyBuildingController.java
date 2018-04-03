package com.everhomes.techpark.expansion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.expansion.BriefLeaseProjectDTO;
import com.everhomes.rest.techpark.expansion.CreateLeaseBuildingCommand;
import com.everhomes.rest.techpark.expansion.DeleteLeaseBuildingCommand;
import com.everhomes.rest.techpark.expansion.GetLeaseBuildingByIdCommand;
import com.everhomes.rest.techpark.expansion.GetLeaseProjectByIdCommand;
import com.everhomes.rest.techpark.expansion.LeaseBuildingDTO;
import com.everhomes.rest.techpark.expansion.LeaseProjectDTO;
import com.everhomes.rest.techpark.expansion.ListAllLeaseProjectsCommand;
import com.everhomes.rest.techpark.expansion.ListLeaseBuildingsCommand;
import com.everhomes.rest.techpark.expansion.ListLeaseBuildingsResponse;
import com.everhomes.rest.techpark.expansion.ListLeaseProjectsCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeaseBuildingCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeaseBuildingOrderCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeaseProjectCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeaseProjectOrderCommand;
import com.everhomes.rest.techpark.expansion.listLeaseProjectsResponse;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value = "entry controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/entry")
public class EnterpriseApplyBuildingController extends ControllerBase{

	@Autowired
	private EnterpriseApplyBuildingService enterpriseApplyBuildingService;

	/**
	 * <b>URL: /techpark/entry/listAllLeaseProjects</b>
	 * <p>项目介绍列表</p>
	 */
	@RequestMapping("listAllLeaseProjects")
	@RestReturn(value=BriefLeaseProjectDTO.class, collection = true)
	public RestResponse listAllLeaseProjects(ListAllLeaseProjectsCommand cmd){

		RestResponse response = new RestResponse(enterpriseApplyBuildingService.listAllLeaseProjects(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/listLeaseProjects</b>
	 * <p>项目介绍列表</p>
	 */
	@RequestMapping("listLeaseProjects")
	@RestReturn(value=listLeaseProjectsResponse.class)
	public RestResponse listLeaseProjects(ListLeaseProjectsCommand cmd){

		RestResponse response = new RestResponse(enterpriseApplyBuildingService.listLeaseProjects(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateLeaseProject</b>
	 * <p>修改项目介绍</p>
	 */
	@RequestMapping("updateLeaseProject")
	@RestReturn(value=LeaseProjectDTO.class)
	public RestResponse updateLeaseProject(UpdateLeaseProjectCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyBuildingService.updateLeaseProject(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/getLeaseProjectById</b>
	 * <p>根据id查询项目介绍</p>
	 */
	@RequestMapping("getLeaseProjectById")
	@RestReturn(value=LeaseProjectDTO.class)
	@RequireAuthentication(false)
	public RestResponse getLeaseProjectById(GetLeaseProjectByIdCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyBuildingService.getLeaseProjectById(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateLeaseProjectOrder</b>
	 * <p>修改项目介绍排序</p>
	 */
	@RequestMapping("updateLeaseProjectOrder")
	@RestReturn(value=String.class)
	public RestResponse updateLeaseProjectOrder(UpdateLeaseProjectOrderCommand cmd){

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/listLeaseBuildings</b>
	 * <p>楼栋介绍列表</p>
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
	 * <b>URL: /techpark/entry/createLeaseBuilding</b>
	 * <p>创建楼栋介绍</p>
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
	 * <b>URL: /techpark/entry/updateLeaseBuilding</b>
	 * <p>修改楼栋介绍</p>
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
	 * <b>URL: /techpark/entry/getLeaseBuildingById</b>
	 * <p>根据id查询楼栋介绍</p>
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
	 * <b>URL: /techpark/entry/deleteLeaseBuilding</b>
	 * <p>删除楼栋介绍</p>
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
	 * <b>URL: /techpark/entry/updateLeaseBuildingOrder</b>
	 * <p>修改楼栋介绍排序</p>
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

	/**
	 * <b>URL: /techpark/entry/syncLeaseBuildings</b>
	 * <p>同步楼栋 产品定义，有域空间要用招租管理时，同步项目管理楼栋到招租管理的项目介绍</p>
	 */
	@RequestMapping("syncLeaseBuildings")
	@RestReturn(value=String.class)
	public RestResponse syncLeaseBuildings(ListLeaseBuildingsCommand cmd){

		enterpriseApplyBuildingService.syncLeaseBuildings(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
