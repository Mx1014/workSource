package com.everhomes.techpark.expansion;

import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateByFormIdCommand;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.everhomes.util.ConvertHelper;

import javax.validation.Valid;

@RestDoc(value = "entry controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/entry")
@Validated
public class EnterpriseApplyEntryController extends ControllerBase{

	@Autowired
	private EnterpriseApplyEntryService enterpriseApplyEntryService;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private GeneralFormService generalFormService;
	
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
			//modify by dengs,20170512,将经纬度转换成 OrganizationDetailDTO 里面的类型，不改动dto，暂时不影响客户端。后面考虑将dto的经纬度改成Double
			if(null != c.getLatitude())
				dto.setLatitude(Double.valueOf(c.getLatitude()));
			if(null != c.getLongitude())
				dto.setLongitude(Double.valueOf(c.getLongitude()));
			//end
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
	 * <b>URL: /techpark/entry/createLeasePromotionForAdmin
	 * <p>创建招租(后台管理用)
	 */
	@RequestMapping("createLeasePromotionForAdmin")
	@RestReturn(value=BuildingForRentDTO.class)
	public RestResponse createLeasePromotionForAdmin(CreateLeasePromotionCommand cmd){
		BuildingForRentDTO res = enterpriseApplyEntryService.createLeasePromotion(cmd, (byte)1);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateLeasePromotionForAdmin
	 * <p>修改招租（后台管理用）
	 */
	@RequestMapping("updateLeasePromotionForAdmin")
	@RestReturn(value=BuildingForRentDTO.class)
	public RestResponse updateLeasePromotionForAdmin(UpdateLeasePromotionCommand cmd){
		BuildingForRentDTO res = enterpriseApplyEntryService.updateLeasePromotion(cmd, (byte)1);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/createLeasePromotion
	 * <p>创建招租（app用）
	 */
	@RequestMapping("createLeasePromotion")
	@RestReturn(value=BuildingForRentDTO.class)
	public RestResponse createLeasePromotion(CreateLeasePromotionCommand cmd){
		BuildingForRentDTO res = enterpriseApplyEntryService.createLeasePromotion(cmd, (byte)2);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/updateLeasePromotion
	 * <p>修改招租（app用）
	 */
	@RequestMapping("updateLeasePromotion")
	@RestReturn(value=BuildingForRentDTO.class)
	public RestResponse updateLeasePromotion(UpdateLeasePromotionCommand cmd){
		BuildingForRentDTO res = enterpriseApplyEntryService.updateLeasePromotion(cmd, (byte)2);
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
	@RequireAuthentication(false)
	public RestResponse findLeasePromotionById(FindLeasePromotionByIdCommand cmd){
		RestResponse response = new RestResponse(enterpriseApplyEntryService.findLeasePromotionById(cmd.getId()));
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
        ApplyEntryResponse applyEntryResponse = enterpriseApplyEntryService.applyEntry(cmd);
        ApplyEntryResponse b = applyEntryResponse;
		RestResponse response = new RestResponse(b);
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
	 * <b>URL: /techpark/entry/getLeasePromotionConfig
	 * <p>获取园区入驻设置
	 */
	@RequestMapping("getLeasePromotionConfig")
	@RestReturn(value=LeasePromotionConfigDTO.class)
	public RestResponse getLeasePromotionConfig(GetLeasePromotionConfigCommand cmd){

		LeasePromotionConfigDTO dto = enterpriseApplyEntryService.getLeasePromotionConfig(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/addLeaseIssuer
	 * <p>添加出租发布者
	 */
	@RequestMapping("addLeaseIssuer")
	@RestReturn(value=String.class)
	public RestResponse addLeaseIssuer(AddLeaseIssuerCommand cmd){

		enterpriseApplyEntryService.addLeaseIssuer(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/deleteLeaseIssuer
	 * <p>删除出租发布者
	 */
	@RequestMapping("deleteLeaseIssuer")
	@RestReturn(value=String.class)
	public RestResponse deleteLeaseIssuer(DeleteLeaseIssuerCommand cmd){

		enterpriseApplyEntryService.deleteLeaseIssuer(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/listLeaseIssuers
	 * <p>获取出租发布者列表
	 */
	@RequestMapping("listLeaseIssuers")
	@RestReturn(value=ListLeaseIssuersResponse.class)
	public RestResponse listLeaseIssuers(ListLeaseIssuersCommand cmd){

		ListLeaseIssuersResponse resp = enterpriseApplyEntryService.listLeaseIssuers(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/checkIsLeaseIssuer
	 * <p>检查是否可以发布
	 */
	@RequestMapping("checkIsLeaseIssuer")
	@RestReturn(value=CheckIsLeaseIssuerDTO.class)
	public RestResponse checkIsLeaseIssuer(CheckIsLeaseIssuerCommand cmd){

		CheckIsLeaseIssuerDTO dto = enterpriseApplyEntryService.checkIsLeaseIssuer(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/listLeaseIssuerBuildings</b>
	 * <p>根据园区号查询楼栋列表</p>
	 */
	@RequestMapping("listLeaseIssuerBuildings")
	@RestReturn(value=ListLeaseIssuerBuildingsResponse.class)
	public RestResponse listLeaseIssuerBuildings(ListLeaseIssuerBuildingsCommand cmd) {

		ListLeaseIssuerBuildingsResponse buildings = enterpriseApplyEntryService.listBuildings(cmd);
		RestResponse response =  new RestResponse(buildings);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/listLeaseIssuerApartments</b>
	 * <p>根据小区Id、楼栋号和关键字查询门牌(物业)</p>
	 */
	@RequestMapping("listLeaseIssuerApartments")
	@RestReturn(value=AddressDTO.class, collection=true)
	public RestResponse listLeaseIssuerApartments(ListLeaseIssuerApartmentsCommand cmd) {
		List<AddressDTO> results = enterpriseApplyEntryService.listLeaseIssuerApartments(cmd);
		RestResponse response = new RestResponse(results);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/getFormTemplateByFormId</b>
	 * <p> 获取表单的信息 </p>
	 * @return GeneralFormDTO 表单的数据信息
	 */
	@RequestMapping("getFormTemplateByFormId")
	@RestReturn(value=GeneralFormDTO.class)
	public RestResponse getFormTemplateByFormId(@Valid GetTemplateByFormIdCommand cmd) {

		GeneralFormDTO dto = generalFormService.getTemplateByFormId(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateLeasePromotionRequestForm</b>
	 * <p> 添加租赁表单 </p>
	 */
	@RequestMapping("updateLeasePromotionRequestForm")
	@RestReturn(value=String.class)
	public RestResponse updateLeasePromotionRequestForm(@Valid UpdateLeasePromotionRequestFormCommand cmd) {

		enterpriseApplyEntryService.updateLeasePromotionRequestForm(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /techpark/entry/getLeasePromotionRequestForm</b>
	 * <p> 获取租赁表单 </p>
	 */
	@RequestMapping("getLeasePromotionRequestForm")
	@RestReturn(value=LeaseFormRequestDTO.class)
	public RestResponse getLeasePromotionRequestForm(@Valid GetLeasePromotionRequestFormCommand cmd) {

		LeaseFormRequestDTO dto = enterpriseApplyEntryService.getLeasePromotionRequestForm(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 *
	 * <b>URL: /techpark/entry/updateLeasePromotionOrder<b>
	 * <p>
	 * 更新招租顺序
	 * </p>
	 */
	@RequestMapping("updateLeasePromotionOrder")
	@RestReturn(String.class)
	public RestResponse updateLeasePromotionOrder(@Valid UpdateLeasePromotionOrderCommand cmd){
		enterpriseApplyEntryService.updateLeasePromotionOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
