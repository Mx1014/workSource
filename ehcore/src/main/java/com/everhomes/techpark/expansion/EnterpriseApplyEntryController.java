package com.everhomes.techpark.expansion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.BuildingForRentDTO;
import com.everhomes.rest.techpark.expansion.CreateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.DeleteApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.DeleteLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseDetailDTO;
import com.everhomes.rest.techpark.expansion.FindLeasePromotionByIdCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdResponse;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentCommand;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailResponse;
import com.everhomes.rest.techpark.expansion.UpdateApplyEntryStatusCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionStatusCommand;
import com.everhomes.util.ConvertHelper;

@RestDoc(value = "entry controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/entry")
public class EnterpriseApplyEntryController extends ControllerBase{

	@Autowired
	private EnterpriseApplyEntryService enterpriseApplyEntryService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * <b>URL: /techpark/entry/listEnterpriseDetails
	 * <p>企业列表
	 */
	@RequestMapping("listEnterpriseDetails")
	@RestReturn(value=ListEnterpriseDetailResponse.class)
	public RestResponse listEnterpriseDetails(ListEnterpriseDetailCommand cmd){
		ListEnterprisesCommand command = ConvertHelper.convert(cmd, ListEnterprisesCommand.class);
		ListEnterprisesCommandResponse r = organizationService.listEnterprises(command);
		List<OrganizationDetailDTO> dtos = r.getDtos();
		
		ListEnterpriseDetailResponse res = new ListEnterpriseDetailResponse();
		res.setDetails(dtos.stream().map((c) ->{
			EnterpriseDetailDTO dto = ConvertHelper.convert(c, EnterpriseDetailDTO.class);
			dto.setEnterpriseId(c.getOrganizationId());
			dto.setEnterpriseName(c.getDisplayName());
			if(dto.getEnterpriseName() == null)
				dto.setEnterpriseName(c.getName());
			dto.setContactPhone(c.getAccountPhone());
			return dto;
		}).collect(Collectors.toList()));
		res.setNextPageAnchor(r.getNextPageAnchor());
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
	 * <p>入住信息列表
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
	 * <b>URL: /techpark/entry/applyEntry
	 * <p>申请入住
	 */
	@RequestMapping("applyEntry")
	@RestReturn(value=ApplyEntryResponse.class)
	public RestResponse applyEntry(EnterpriseApplyEntryCommand cmd){
		ApplyEntryResponse b = enterpriseApplyEntryService.applyEntry(cmd);
		RestResponse response = new RestResponse(b);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	续租也用上面那个 applyEntry
//	 * <b>URL: /techpark/entry/applyRenew
//	 * <p>
//	 */
//	@RequestMapping("applyRenew")
//	@RestReturn(value=String.class)
//	public RestResponse applyRenew(EnterpriseApplyRenewCommand cmd){
//		boolean b = enterpriseApplyEntryService.applyRenew(cmd);
//		RestResponse response = new RestResponse(b);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /techpark/entry/listForRents
	 * <p>招租列表
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
	
	/**
	 * <b>URL: /techpark/entry/createLeasePromotion
	 * <p>创建招租
	 */
	@RequestMapping("createLeasePromotion")
	@RestReturn(value=String.class)
	public RestResponse createLeasePromotion(CreateLeasePromotionCommand cmd){
		boolean res = enterpriseApplyEntryService.createLeasePromotion(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/updateLeasePromotion
	 * <p>修改招租
	 */
	@RequestMapping("updateLeasePromotion")
	@RestReturn(value=String.class)
	public RestResponse updateLeasePromotion(UpdateLeasePromotionCommand cmd){
		boolean res = enterpriseApplyEntryService.updateLeasePromotion(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/findLeasePromotionById
	 * <p>根据id查询招租
	 */
	@RequestMapping("findLeasePromotionById")
	@RestReturn(value=BuildingForRentDTO.class)
	public RestResponse findLeasePromotionById(FindLeasePromotionByIdCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.findLeasePromotionById(cmd.getId()));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/updateApplyEntryStatus
	 * <p>修改入住状态
	 */
	@RequestMapping("updateApplyEntryStatus")
	@RestReturn(value=String.class)
	public RestResponse updateApplyEntryStatus(UpdateApplyEntryStatusCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.updateApplyEntryStatus(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/updateLeasePromotionStatus
	 * <p>修改招租状态
	 */
	@RequestMapping("updateLeasePromotionStatus")
	@RestReturn(value=String.class)
	public RestResponse updateLeasePromotionStatus(UpdateLeasePromotionStatusCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.updateLeasePromotionStatus(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/deleteApplyEntry
	 * <p>删除申请
	 */
	@RequestMapping("deleteApplyEntry")
	@RestReturn(value=String.class)
	public RestResponse deleteApplyEntry(DeleteApplyEntryCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.deleteApplyEntry(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/deleteLeasePromotion
	 * <p>删除招租
	 */
	@RequestMapping("deleteLeasePromotion")
	@RestReturn(value=String.class)
	public RestResponse deleteLeasePromotion(DeleteLeasePromotionCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.deleteLeasePromotion(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
}
