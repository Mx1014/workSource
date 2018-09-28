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
import com.everhomes.rest.organization.GetOrganizationDetailByIdCommand;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.techpark.expansion.OpenCustomRequestFormCommand;
import com.everhomes.rest.techpark.expansion.AddLeaseIssuerCommand;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.BuildingForRentDTO;
import com.everhomes.rest.techpark.expansion.CheckIsLeaseIssuerCommand;
import com.everhomes.rest.techpark.expansion.CheckIsLeaseIssuerDTO;
import com.everhomes.rest.techpark.expansion.CreateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.DeleteApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.DeleteLeaseIssuerCommand;
import com.everhomes.rest.techpark.expansion.DeleteLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseDetailDTO;
import com.everhomes.rest.techpark.expansion.FindLeasePromotionByIdCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdResponse;
import com.everhomes.rest.techpark.expansion.GetLeasePromotionConfigCommand;
import com.everhomes.rest.techpark.expansion.GetLeasePromotionRequestFormCommand;
import com.everhomes.rest.techpark.expansion.LeaseFormRequestDTO;
import com.everhomes.rest.techpark.expansion.LeasePromotionConfigDTO;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentCommand;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailResponse;
import com.everhomes.rest.techpark.expansion.ListLeaseIssuerApartmentsCommand;
import com.everhomes.rest.techpark.expansion.ListLeaseIssuerBuildingsCommand;
import com.everhomes.rest.techpark.expansion.ListLeaseIssuerBuildingsResponse;
import com.everhomes.rest.techpark.expansion.ListLeaseIssuersCommand;
import com.everhomes.rest.techpark.expansion.ListLeaseIssuersResponse;
import com.everhomes.rest.techpark.expansion.SetLeasePromotionConfigCommand;
import com.everhomes.rest.techpark.expansion.TransformToCustomerCommand;
import com.everhomes.rest.techpark.expansion.TransformToCustomerResponse;
import com.everhomes.rest.techpark.expansion.UpdateApplyEntryStatusCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionOrderCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionRequestFormCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionStatusCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RequireAuthentication;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
	 * <b>URL: /techpark/entry/listEnterprisesAbstract</b>
	 * <p>企业概要列表</p>
	 */
	@RequestMapping("listEnterprisesAbstract")
	@RestReturn(value=ListEnterpriseDetailResponse.class)
	public RestResponse listEnterprisesAbstract(ListEnterpriseDetailCommand cmd){
		ListEnterprisesCommand command = ConvertHelper.convert(cmd, ListEnterprisesCommand.class);
		if (cmd.getAllFlag() != null){
			command.setPageSize(100000);
		}
		RestResponse response = new RestResponse(organizationService.listEnterprisesAbstract(command));

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/getOrganizationDetailWithDefaultAttachmentById</b>
	 * <p>根据Id查询对应的企业详情信息</p>
	 */
	@RequestMapping("getOrganizationDetailWithDefaultAttachmentById")
	@RestReturn(OrganizationDetailDTO.class)
	public RestResponse getOrganizationDetailWithDefaultAttachmentById(GetOrganizationDetailByIdCommand cmd){

		OrganizationDetailDTO org = organizationService.getOrganizationDetailWithDefaultAttachmentById(cmd);
		RestResponse response = new RestResponse(org);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/listEnterpriseDetails</b>
	 * <p>企业列表</p>
	 */
	@RequestMapping("listEnterpriseDetails")
	@RestReturn(value=ListEnterpriseDetailResponse.class)
	public RestResponse listEnterpriseDetails(ListEnterpriseDetailCommand cmd){
		ListEnterprisesCommand command = ConvertHelper.convert(cmd, ListEnterprisesCommand.class);
//		command.setPageSize(100000);
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
			dto.setEnterpriseName(c.getName());//优先展示企业名而非昵称 by xiongying20170826
			if(dto.getEnterpriseName() == null || StringUtils.isNullOrEmpty(dto.getEnterpriseName()))
				dto.setEnterpriseName(c.getDisplayName());
			dto.setContactPhone(c.getAccountPhone());


			String pinyin = PinYinHelper.getPinYin(dto.getEnterpriseName());
			dto.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
			dto.setFullPinyin(pinyin.replaceAll(" ", ""));
			dto.setInitial(PinYinHelper.getCapitalInitial(dto.getFullPinyin()));

			return dto;
		}).collect(Collectors.toList()));
		res.setNextPageAnchor(r.getNextPageAnchor());
		RestResponse response = new RestResponse(res);
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/entry/getEnterpriseDetailById</b>
	 * <p>获取企业信息</p>
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
	 * <b>URL: /techpark/entry/listForRents</b>
	 * <p>招租列表</p>
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
	 * <b>URL: /techpark/entry/createLeasePromotionForAdmin</b>
	 * <p>创建招租(后台管理用)</p>
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
	 * <b>URL: /techpark/entry/updateLeasePromotionForAdmin</b>
	 * <p>修改招租（后台管理用）</p>
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
	 * <b>URL: /techpark/entry/createLeasePromotion</b>
	 * <p>创建招租（app用）</p>
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
	 * <b>URL: /techpark/entry/updateLeasePromotion</b>
	 * <p>修改招租（app用）</p>
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
	 * <b>URL: /techpark/entry/findLeasePromotionById</b>
	 * <p>根据id查询招租</p>
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
	 * <b>URL: /techpark/entry/updateLeasePromotionStatus</b>
	 * <p>修改招租状态</p>
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
	 * <b>URL: /techpark/entry/deleteLeasePromotion</b>
	 * <p>删除招租</p>
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
	 * <b>URL: /techpark/entry/listApplyEntrys</b>
	 * <p>入住信息列表</p>
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
	 * <b>URL: /techpark/entry/exportApplyEntrys</b>
	 * <p>导出入住信息列表</p>
	 */
	@RequestMapping("exportApplyEntrys")
	public void exportApplyEntrys(ListEnterpriseApplyEntryCommand cmd, HttpServletResponse resp){
		enterpriseApplyEntryService.exportApplyEntrys(cmd, resp);

	}

	/**
	 * <b>URL: /techpark/entry/applyEntry</b>
	 * <p>申请入住</p>
	 */
	@RequestMapping("applyEntry")
	@RestReturn(value=ApplyEntryResponse.class)
	public RestResponse applyEntry(EnterpriseApplyEntryCommand cmd){
        ApplyEntryResponse applyEntryResponse = enterpriseApplyEntryService.applyEntry(cmd);
		RestResponse response = new RestResponse(applyEntryResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/updateApplyEntryStatus</b>
	 * <p>修改入住状态</p>
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
	 * <b>URL: /techpark/entry/deleteApplyEntry</b>
	 * <p>删除申请</p>
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
	 * <b>URL: /techpark/entry/setLeasePromotionConfig</b>
	 * <p>修改园区入驻设置</p>
	 */
	@RequestMapping("setLeasePromotionConfig")
	@RestReturn(value=String.class)
	public RestResponse setLeasePromotionConfig(SetLeasePromotionConfigCommand cmd){

		enterpriseApplyEntryService.setLeasePromotionConfig(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/getLeasePromotionConfig</b>
	 * <p>获取园区入驻设置</p>
	 */
	@RequestMapping("getLeasePromotionConfig")
	@RestReturn(value=LeasePromotionConfigDTO.class)
    @RequireAuthentication(value = false)
	public RestResponse getLeasePromotionConfig(GetLeasePromotionConfigCommand cmd){

		LeasePromotionConfigDTO dto = enterpriseApplyEntryService.getLeasePromotionConfig(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/entry/addLeaseIssuer</b>
	 * <p>添加出租发布者</p>
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
	 * <b>URL: /techpark/entry/deleteLeaseIssuer</b>
	 * <p>删除出租发布者</p>
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
	 * <b>URL: /techpark/entry/listLeaseIssuers</b>
	 * <p>获取出租发布者列表</p>
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
	 * <b>URL: /techpark/entry/checkIsLeaseIssuer</b>
	 * <p>检查是否可以发布</p>
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
	 * <p>根据出租发布者楼栋列表</p>
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
	 * <p>根据出租发布者门牌</p>
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
	 * <b>URL: /techpark/entry/openCustomRequestForm</b>
	 * <p> 开启自定义并复制表单 </p>
	 */
	@RequestMapping("openCustomRequestForm")
	@RestReturn(value=LeaseFormRequestDTO.class)
	public RestResponse openCustomRequestForm(@Valid OpenCustomRequestFormCommand cmd) {

		enterpriseApplyEntryService.openCustomRequestForm(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /techpark/entry/closeCustomRequestForm</b>
	 * <p> 关闭自定义表单 </p>
	 */
	@RequestMapping("closeCustomRequestForm")
	@RestReturn(value=String.class)
	public RestResponse closeCustomRequestForm(@Valid UpdateLeasePromotionRequestFormCommand cmd) {

		enterpriseApplyEntryService.closeCustomRequestForm(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 *
	 * <b>URL: /techpark/entry/updateLeasePromotionOrder</b>
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
	
	/**
	 * <b>URL: /techpark/entry/transformToCustomer</b>
	 * <p>转为意向客户（园区入驻模块）</p>
	 */
	@RequestMapping("transformToCustomer")
	@RestReturn(value=Long.class,collection=true)
	public RestResponse transformToCustomer(TransformToCustomerCommand cmd){
		List<Long> result = enterpriseApplyEntryService.transformToCustomer(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
