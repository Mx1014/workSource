package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.investment.InvitedCustomerService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.*;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.investment.CreateInvitedCustomerCommand;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerContactType;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.CustomerRequirementAddressDTO;
import com.everhomes.rest.investment.CustomerRequirementDTO;
import com.everhomes.rest.investment.InvitedCustomerDTO;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.*;
import com.everhomes.yellowPage.YellowPage;
import com.everhomes.yellowPage.YellowPageProvider;
import com.mysql.fabric.xmlrpc.base.Array;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class EnterpriseApplyEntryServiceImpl implements EnterpriseApplyEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryServiceImpl.class);

    public static final Long DEFAULT_CATEGORY_ID = 1L;

	private SmsProvider smsProvider;
	private ContractProvider contractProvider;
	private BuildingProvider buildingProvider;
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	private EnterpriseOpRequestBuildingProvider enterpriseOpRequestBuildingProvider;
	private OrganizationProvider organizationProvider;
	private ConfigurationProvider configurationProvider;
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	private ContentServerService contentServerService;
	private EnterpriseProvider enterpriseProvider;
	private GroupProvider groupProvider;
	private CommunityProvider communityProvider;
	private UserProvider userProvider;
	private YellowPageProvider yellowPageProvider;
    private FlowService flowService;
    private DbProvider dbProvider;
	private OrganizationService organizationService;
    private LocaleTemplateService localeTemplateService;
	private EnterpriseLeaseIssuerProvider enterpriseLeaseIssuerProvider;
    private AddressProvider addressProvider;
    private RolePrivilegeService rolePrivilegeService;
	private GeneralFormService generalFormService;
	private GeneralFormValProvider generalFormValProvider;
	private GeneralFormProvider generalFormProvider;
	private EnterpriseApplyBuildingProvider enterpriseApplyBuildingProvider;
	private LocaleStringService localeStringService;

	@Autowired
	private PortalService portalService;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private InvitedCustomerService invitedCustomerService;
	
	@Override
	public GetEnterpriseDetailByIdResponse getEnterpriseDetailById(GetEnterpriseDetailByIdCommand cmd) {

		GetEnterpriseDetailByIdResponse res = new GetEnterpriseDetailByIdResponse();
		EnterpriseDetail enterpriseDetail = this.getEnterpriseDetailByEnterpriseId(cmd.getId());
		EnterpriseDetailDTO dto = toEnterpriseDetailDTO(enterpriseDetail);
		dto.setContactPhone(enterpriseDetail.getContact());
		res.setDetail(dto);
		return res;
	}
	
	private EnterpriseDetail getEnterpriseDetailByEnterpriseId(Long enterpriseId){
		Group group = groupProvider.findGroupById(enterpriseId);
		
		if(null == group){
			return null;
		}
		EnterpriseDetail enterpriseDetail = enterpriseApplyEntryProvider.getEnterpriseDetailById(enterpriseId);
		if(null == enterpriseDetail){
			enterpriseDetail = new EnterpriseDetail();
		}
		enterpriseDetail.setEnterpriseId(group.getId());
    	enterpriseDetail.setEnterpriseName(group.getName());
		String description =  enterpriseDetail.getDescription();
    	enterpriseDetail.setDescription(StringUtils.isEmpty(description) ? group.getDescription() : description);
    	String contact =  enterpriseDetail.getContact();
    	enterpriseDetail.setContact(StringUtils.isEmpty(contact) ? group.getEnterpriseContact() : contact);
    	String address = enterpriseDetail.getAddress();
    	enterpriseDetail.setAddress(StringUtils.isEmpty(address) ? group.getEnterpriseAddress() : address);
    	enterpriseDetail.setAvatar(group.getAvatar());
    	return enterpriseDetail;
	}
	
	private EnterpriseDetailDTO toEnterpriseDetailDTO(EnterpriseDetail enterpriseDetail) {
	    User user = UserContext.current().getUser();
	    Long userId = (user == null) ? -1L : user.getId();
	    
	    EnterpriseDetailDTO dto = null;
	    if(enterpriseDetail != null) {
	        dto = ConvertHelper.convert(enterpriseDetail, EnterpriseDetailDTO.class);
	        dto.setAvatarUri(enterpriseDetail.getAvatar());
	        dto.setAvatarUrl(contentServerService.parserUri(dto.getAvatarUri(),EntityType.GROUP.getCode(), enterpriseDetail.getEnterpriseId()));
	        List<EnterpriseAttachment> attachments = enterpriseProvider.listEnterpriseAttachments(enterpriseDetail.getEnterpriseId());
	        if(attachments != null && attachments.size() > 0)
	        {
	            List<EnterpriseAttachmentDTO> attachmentDtoList = new ArrayList<EnterpriseAttachmentDTO>();
	            for(EnterpriseAttachment attachment : attachments) {
	                EnterpriseAttachmentDTO attachmentDto = ConvertHelper.convert(attachment, EnterpriseAttachmentDTO.class);
	                String uri = attachment.getContentUri();
	                if(uri != null && uri.length() > 0) {
	                    try{
	                        String url = contentServerService.parserUri(uri, EntityType.GROUP.getCode(), enterpriseDetail.getEnterpriseId());
	                        attachmentDto.setContentUrl(url);
	                    }catch(Exception e){
	                        LOGGER.error("Failed to parse content uri of enterprise attachments, userId=" + userId 
	                            + ", enterpriseId=" + enterpriseDetail.getEnterpriseId(), e);
	                    }
	                }
	                
	                attachmentDtoList.add(attachmentDto);
	            }
	            
	            dto.setAttachments(attachmentDtoList);
	        }
	    }	
	    
	    return dto;
	}

	@Override
	public ListEnterpriseApplyEntryResponse listApplyEntrys(ListEnterpriseApplyEntryCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040140L, cmd.getAppId(), null,cmd.getCommunityId());//申请记录
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040140L, cmd.getAppId(), cmd.getCurrentPMId(),cmd.getCommunityId());//申请记录
		}
		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		ListEnterpriseApplyEntryResponse response = new ListEnterpriseApplyEntryResponse();
		
		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);

		if(cmd.getPageSize() == null) {
			cmd.setPageSize(configurationProvider.getIntValue("pagination.default.size", AppConstants.PAGINATION_DEFAULT_SIZE));
		}
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
		List<EnterpriseOpRequest> enterpriseOpRequests = null;
		//增加了判断buildingId
		if(null == cmd.getBuildingId()) {
			enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, cmd.getPageSize());
		} else {
			List<EnterpriseOpRequestBuilding> opRequestBuildings = this.enterpriseOpRequestBuildingProvider.queryEnterpriseOpRequestBuildings(
					new ListingQueryBuilderCallback() {
						@Override
						public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
								SelectQuery<? extends Record> query) {
							query.addConditions(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.BUILDING_ID.eq(cmd.getBuildingId()));  
							return query;
						}
					});

			List<Long> idList = new ArrayList<>();
			for(EnterpriseOpRequestBuilding opBuilding : opRequestBuildings){
				idList.add(opBuilding.getEnterpriseOpRequestsId());
			}
			if(idList.size() > 0) {
				enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, cmd.getPageSize(), idList);
			}
		}
		if(null == enterpriseOpRequests) {
			return response;
		}
		response.setNextPageAnchor(locator.getAnchor());

		String locale = UserContext.current().getUser().getLocale();
		String defaultValue = localeStringService.getLocalizedString(ApplyEntryErrorCodes.SCOPE, String.valueOf(ApplyEntryErrorCodes.WU), locale, "");

		List<EnterpriseApplyEntryDTO> dtos = enterpriseOpRequests.stream().map(r -> populateEnterpriseApplyEntryDTO(r, defaultValue))
				.collect(Collectors.toList());

		response.setEntrys(dtos);
		return response;
	}

	@Override
	public void exportApplyEntrys(ListEnterpriseApplyEntryCommand cmd, HttpServletResponse resp) {
		cmd.setPageSize(Integer.MAX_VALUE - 1);
		ListEnterpriseApplyEntryResponse response = listApplyEntrys(cmd);

		Workbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		Sheet sheet = wb.createSheet("ApplyEntrys");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("申请时间");
		row.createCell(1).setCellValue("项目");
		row.createCell(2).setCellValue("楼栋");
		row.createCell(3).setCellValue("门牌");
		row.createCell(4).setCellValue("申请来源");
		row.createCell(5).setCellValue("申请人");
		row.createCell(6).setCellValue("电话");
		row.createCell(7).setCellValue("企业");
		row.createCell(8).setCellValue("状态");

		SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (null != response) {
			List<EnterpriseApplyEntryDTO> list = response.getEntrys();
			for(int i=0;i<list.size();i++){
				Row tempRow = sheet.createRow(i + 1);
				EnterpriseApplyEntryDTO entry = list.get(i);
				tempRow.createCell(0).setCellValue(datetimeSF.format(entry.getCreateTime()));
				tempRow.createCell(1).setCellValue(checkStr(entry.getCommunityName()));
				tempRow.createCell(2).setCellValue(checkStr(entry.getBuildingName()));
				tempRow.createCell(3).setCellValue(checkStr(entry.getApartmentName()));
				tempRow.createCell(4).setCellValue(ApplyEntrySourceType.fromType(entry.getSourceType()).getDescription());
				tempRow.createCell(5).setCellValue(checkStr(entry.getApplyUserName()));
				tempRow.createCell(6).setCellValue(checkStr(entry.getApplyContact()));
				tempRow.createCell(7).setCellValue(checkStr(entry.getEnterpriseName()));
				tempRow.createCell(8).setCellValue(ApplyEntryStatus.fromType(entry.getStatus()).getDescription());

			}
		}

		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, resp);
		} catch (IOException e) {
			LOGGER.error("exportApplyEntrys is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportApplyEntrys is fail.");
		}

	}

	private String checkStr(String str) {
		return null == str ? "无" : str;
	}

	private EnterpriseApplyEntryDTO populateEnterpriseApplyEntryDTO(EnterpriseOpRequest enterpriseOpRequest, String defaultValue) {

		EnterpriseApplyEntryDTO dto = ConvertHelper.convert(enterpriseOpRequest, EnterpriseApplyEntryDTO.class);

		if (StringUtils.isEmpty(dto.getDescription())) {
			dto.setDescription(defaultValue);
		}

		//对于有合同的(一定是续租)
		if(null != enterpriseOpRequest.getContractId()){
			Contract contract = contractProvider.findContractById(enterpriseOpRequest.getContractId());
			if(null != contract) {
				dto.setContract(organizationService.processContract(contract));
			}
		}

		List<BuildingDTO> buildings = new ArrayList<>();

		List<EnterpriseOpRequestBuilding> opBuildings = enterpriseOpRequestBuildingProvider.queryEnterpriseOpRequestBuildings(new ListingQueryBuilderCallback() {
			//续租申请，申请来源=续租 续租申请，楼栋=合同里关联的楼栋（可能多个）
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
																SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ENTERPRISE_OP_REQUESTS_ID.eq(dto.getId()));
				query.addConditions(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.STATUS.eq(EnterpriseOpRequestBuildingStatus.NORMAL.getCode()));
				return query;
			}
		});
		for(EnterpriseOpRequestBuilding opBuilding : opBuildings){
			LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(opBuilding.getBuildingId());

			if (null != leaseBuilding) {
				buildings.add(processBuildingDTO(leaseBuilding));
			}
		}

		//对于不同的类型有不同的楼栋
		if(ApplyEntrySourceType.RENEW.getCode().equals(dto.getSourceType())){

		}else if(ApplyEntrySourceType.BUILDING.getCode().equals(dto.getSourceType())){
			GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
			cmd2.setSourceType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
			cmd2.setSourceId(dto.getId());
			List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd2);
			dto.setFormValues(formValues);
		}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(dto.getSourceType())){
			//虚位以待处的申请
			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(dto.getSourceId());

			if (null != leasePromotion) {
				dto.setApartmentName(leasePromotion.getApartmentName());

				//当招租信息的buildingId是0的时候，表示是手填的楼栋信息，返回手填的楼栋信息
				if(leasePromotion.getBuildingId() == 0L){
					LeaseBuilding leaseBuilding = new LeaseBuilding();
					leaseBuilding.setName(leasePromotion.getBuildingName());
					buildings.add(processBuildingDTO(leaseBuilding));
				}
			}

			GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
			cmd2.setSourceType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
			cmd2.setSourceId(dto.getId());
			List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd2);
			dto.setFormValues(formValues);
		}else if (ApplyEntrySourceType.MARKET_ZONE.getCode().equals(dto.getSourceType())){

		}

		//填充楼栋门牌
		if (null != enterpriseOpRequest.getAddressId()){
			Address address = addressProvider.findAddressById(enterpriseOpRequest.getAddressId());
			if (null != address){
				dto.setApartmentName(address.getApartmentName());
				dto.setBuildingName(address.getBuildingName());
			}
		}

		dto.setBuildings(buildings);

		Community community = communityProvider.findCommunityById(enterpriseOpRequest.getCommunityId());
		dto.setCommunityName(community.getName());
		return dto;
	}

	private BuildingDTO processBuildingDTO(LeaseBuilding leaseBuilding){
		BuildingDTO buildingDTO = new BuildingDTO();
		buildingDTO.setBuildingName(leaseBuilding.getName());
		buildingDTO.setName(leaseBuilding.getName());
		return buildingDTO;
	}

	@Override
	public ApplyEntryResponse applyEntry(EnterpriseApplyEntryCommand cmd) {
		ApplyEntryResponse resp = new ApplyEntryResponse();

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);
		request.setApplyUserId(UserContext.current().getUser().getId());
		if(null != cmd.getContactPhone()) {
			request.setApplyContact(cmd.getContactPhone());
		}

		request.setOperatorUid(request.getApplyUserId());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());

        FlowCase flowCase = dbProvider.execute(status -> {

            enterpriseApplyEntryProvider.createApplyEntry(request);

			//对接表单
			if (null != cmd.getRequestFormId()) {
				addGeneralFormInfo(cmd.getRequestFormId(), cmd.getFormValues(), EntityType.ENTERPRISE_OP_REQUEST.getCode(),
						request.getId(), LeasePromotionFlag.ENABLED.getCode());
			}

			//added by Janson
			String projectType = EntityType.COMMUNITY.getCode();
			Long projectId = cmd.getCommunityId();

			ResourceCategoryAssignment[] resourceCategories = new ResourceCategoryAssignment[1];

			//添加楼栋关联关系
			String buildingName = addEnterpriseOpRequestBuildings(request, resourceCategories);
			//TODO:暂时屏蔽掉
//			if(null != resourceCategories[0] && null!= resourceCategories[0].getResourceCategryId()) {
//				projectId = resourceCategories[0].getResourceCategryId();
//				projectType = EntityType.RESOURCE_CATEGORY.getCode();
//			}

			FlowCase flowCase1 = null;
    		if (LeaseIssuerType.ORGANIZATION.getCode().equals(request.getIssuerType())) {
				flowCase1 = this.createFlowCase(request, projectId, projectType, buildingName);
                request.setFlowcaseId(flowCase1.getId());
            }
			enterpriseApplyEntryProvider.updateApplyEntry(request);

			return flowCase1;
        });
        
        if (flowCase != null) {
        	//TODO: 组装resp
        	String url = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
        	resp.setUrl(url);
        }
		return resp;
	}

	private String addEnterpriseOpRequestBuildings(EnterpriseOpRequest request, ResourceCategoryAssignment[] resourceCategories) {

		String issuerType = LeaseIssuerType.ORGANIZATION.getCode();
		String requestAddress = null;
		Set<Long> buildingIds = new HashSet<>();

		EnterpriseOpRequestBuilding opRequestBuilding = new EnterpriseOpRequestBuilding();
		opRequestBuilding.setEnterpriseOpRequestsId(request.getId());
		opRequestBuilding.setCreatorUid(UserContext.current().getUser().getId());
		opRequestBuilding.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		opRequestBuilding.setStatus(EnterpriseOpRequestBuildingStatus.NORMAL.getCode());

		//TODO : 根据情况保存地址
		if(null != request.getContractId()){
			//兼容老app, 带合同一定是续租,此处set 来源类型
			request.setSourceType(ApplyEntrySourceType.RENEW.getCode());
			//1.保存合同带的地址
			Contract contract = contractProvider.findContractById(request.getContractId());
			if(null == contract )
				throw errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"can not find contract!!");
			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(UserContext.getCurrentNamespaceId(),
					contract.getContractNumber());

			Long firstBuildingId = null;
			for(BuildingApartmentDTO buildingApartmentDTO: buildings){
				Building building = communityProvider.findBuildingByCommunityIdAndName(request.getCommunityId(), buildingApartmentDTO.getBuildingName());
				if(building != null){
					//转换楼栋id
					LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(building.getId());

					opRequestBuilding.setBuildingId(leaseBuilding.getId());
					enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
					//记录楼栋id，创建工作流时使用
					buildingIds.add(leaseBuilding.getId());
					//默认取第一个楼栋id，创建工作流时使用
					if (null == firstBuildingId) {
						firstBuildingId = building.getId();
						resourceCategories[0] = communityProvider.findResourceCategoryAssignment(firstBuildingId,
								EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
					}
				}
			}
		}else if (request.getSourceType().equals(ApplyEntrySourceType.RENEW.getCode())){

			List<OrganizationAddress> addresses = organizationProvider.listOrganizationAddressByOrganizationId(request.getEnterpriseId());
			if (!addresses.isEmpty()) {
				//续租时，默认取公司第一个地址
				Address address = addressProvider.findAddressById(addresses.get(0).getAddressId());
				if (null != address) {
					Building building = communityProvider.findBuildingByCommunityIdAndName(request.getCommunityId(), address.getBuildingName());
					if (null != building) {
						//转换楼栋id
						LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(building.getId());
						if (null != leaseBuilding) {
							opRequestBuilding.setBuildingId(leaseBuilding.getId());
							enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
							//设置门牌地址
							request.setAddressId(address.getId());
						}
					}
				}
			}
		}else if (request.getSourceType().equals(ApplyEntrySourceType.MARKET_ZONE.getCode())){
			//2. 创客空间带的地址
			YellowPage yellowPage = yellowPageProvider.getYellowPageById(request.getSourceId());
			//转换楼栋id
			LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(yellowPage.getBuildingId());
			opRequestBuilding.setBuildingId(leaseBuilding.getId());
			enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

			resourceCategories[0] = communityProvider.findResourceCategoryAssignment(yellowPage.getBuildingId(),
					EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());

		}else if (request.getSourceType().equals(ApplyEntrySourceType.BUILDING.getCode())){
			//3. 园区介绍直接就是楼栋的地址
			//暂时
			LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(request.getSourceId());
			if (null != leaseBuilding) {

				opRequestBuilding.setBuildingId(request.getSourceId());
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

				if (leaseBuilding.getBuildingId() != 0L) {
					resourceCategories[0] = communityProvider.findResourceCategoryAssignment(leaseBuilding.getBuildingId(),
							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				}
			}else {
				leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(request.getSourceId());
				if (null != leaseBuilding) {
					opRequestBuilding.setBuildingId(leaseBuilding.getId());
					enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

					//数据不一致时，将项目管理中的楼栋同步到 招租管理楼栋
					Building building = communityProvider.findBuildingById(request.getSourceId());

					if (null != building) {
						leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByName(building.getCommunityId(), building.getName());
						if (null == leaseBuilding) {
							leaseBuilding = ConvertHelper.convert(building, LeaseBuilding.class);
							leaseBuilding.setBuildingId(building.getId());
							leaseBuilding.setManagerContact(building.getContact());
							leaseBuilding.setDeleteFlag((byte)0);
							enterpriseApplyBuildingProvider.createLeaseBuilding(leaseBuilding);
						}
						resourceCategories[0] = communityProvider.findResourceCategoryAssignment(request.getSourceId(),
								EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
					}
					//更新sourceId为 转换之后的leaseBuildingId
					request.setSourceId(leaseBuilding.getId());
				}
			}

			//如果是项目介绍的申请，产品定义buildingName 值显示项目名称
			Community community = communityProvider.findCommunityById(request.getCommunityId());
			requestAddress = community.getName() + leaseBuilding.getName();
		}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(request.getSourceType())){
			//4. 虚位以待的楼栋地址
			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(request.getSourceId());
			//兼容老版本app，默认设置为管理公司发布，如果是招租信息的申请，这里取当前招租信息的发布类型
			issuerType = leasePromotion.getIssuerType();

			//设置门牌地址
			request.setAddressId(leasePromotion.getAddressId());

			opRequestBuilding.setBuildingId(leasePromotion.getBuildingId());
			enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

			//招租信息的buildingId如果是0，则表示楼栋信息是手动输入，门牌也是手动输入
			if (leasePromotion.getBuildingId() != OTHER_BUILDING_ID) {
				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(leasePromotion.getBuildingId());
				//leaseBuilding的buildingId是对应项目管理楼栋id，如果是0，表示是园区入驻的楼栋介绍自定义新增的楼栋
				if (leaseBuilding.getBuildingId() != 0L) {
					resourceCategories[0] = communityProvider.findResourceCategoryAssignment(leaseBuilding.getBuildingId(),
							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				}

				String apartmentName = defaultIfNull(leasePromotion.getApartmentName(), "");
				Address address = addressProvider.findAddressById(leasePromotion.getAddressId());
				if (null != address) {
					apartmentName = address.getApartmentName();
				}
				requestAddress = leaseBuilding.getName() + apartmentName;
			}else {
				requestAddress = defaultIfNull(leasePromotion.getBuildingName(), "") +
						defaultIfNull(leasePromotion.getApartmentName(), "");
			}
		}else if(ApplyEntrySourceType.LEASE_PROJECT.getCode().equals(request.getSourceType())){
			//如果是项目介绍的申请，产品定义buildingName 值显示项目名称
			Community community = communityProvider.findCommunityById(request.getCommunityId());
			requestAddress = community.getName();

		}

		if (null != opRequestBuilding.getBuildingId()) {
			buildingIds.add(opRequestBuilding.getBuildingId());
		}
		//从招租信息获取发布人信息，填充到申请信息中
		request.setIssuerType(issuerType);

		return parseRequestAddress(buildingIds, requestAddress);
	}

	private String parseRequestAddress(Set<Long> buildingIds, String requestAddress) {

		if (requestAddress == null) {
			StringBuilder sb = new StringBuilder();
			int n = 1;
			for (Long id: buildingIds) {

				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(id);
				if (null != leaseBuilding) {
					if (n == buildingIds.size()) {
						sb.append(leaseBuilding.getName());
					}else {
						sb.append(leaseBuilding.getName()).append(",");
					}
				}
				n++;
			}
			requestAddress = defaultIfNull(sb.toString(),"");
		}

		return requestAddress;
	}

	private String processFlowURL(Long flowCaseId, String string, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
	}

	private String convertSourceType(String type) {
		ApplyEntrySourceType sourceType = ApplyEntrySourceType.fromType(type);

		if (null != sourceType) {
			switch (sourceType) {
				case LEASE_PROJECT:
					return FlowOwnerType.LEASE_PROJECT.getCode();
				case BUILDING:
					return FlowOwnerType.LEASE_PROJECT.getCode();
				case FOR_RENT:
					return FlowOwnerType.LEASE_PROMOTION.getCode();
				case RENEW:
					return FlowOwnerType.LEASE_RENEW.getCode();
			}
		}
		return null;
	}

    private FlowCase createFlowCase(EnterpriseOpRequest request, Long projectId, String projectType, String buildingName) {

		String sourceType = request.getSourceType();

		if (sourceType.equals(ApplyEntrySourceType.LEASE_PROJECT.getCode())) {
			sourceType = ApplyEntrySourceType.BUILDING.getCode();
		}

		String ownerType = convertSourceType(sourceType);

		Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID,
				FlowModuleType.LEASE_PROMOTION.getCode() + "_" + request.getCategoryId(), request.getCommunityId(), ownerType);

		if (null == flow) {
			flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID,
					FlowModuleType.LEASE_PROMOTION.getCode() + "_" + request.getCategoryId(), request.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());
		}
		//做兼容，以前没有多入口，加了多入口之后，默认老数据入口 CategoryId DEFAULT_CATEGORY_ID，为了不改变工作流使用，老数据回去查询以前老工作流
		if (null == flow && request.getCategoryId() == DEFAULT_CATEGORY_ID.longValue()) {
			flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID,
					FlowModuleType.NO_MODULE.getCode(), request.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());
		}

		if(null == flow) {
			LOGGER.error("Enable flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
					"Enable flow not found.");
		}

        CreateFlowCaseCommand flowCaseCmd = new CreateFlowCaseCommand();
        flowCaseCmd.setApplyUserId(request.getApplyUserId());
        flowCaseCmd.setReferId(request.getId());
        // flowCase摘要内容
        flowCaseCmd.setContent(this.getBriefContent(request, buildingName));
        flowCaseCmd.setReferType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
        flowCaseCmd.setProjectId(projectId);
        
        //TODO better added by janson 临时办法，4.4.2 必须改动
        if(UserContext.getCurrentNamespaceId(null).equals(999983)) {
        	flowCaseCmd.setTitle("园区入驻");
        }
		flowCaseCmd.setServiceType("园区入驻");
		flowCaseCmd.setFlowMainId(flow.getFlowMainId());
		flowCaseCmd.setFlowVersion(flow.getFlowVersion());
		flowCaseCmd.setProjectType(projectType);
		flowCaseCmd.setProjectId(projectId);
		flowCaseCmd.setCurrentOrganizationId(request.getEnterpriseId());
		flowCaseCmd.setServiceType("园区入驻");


		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		listServiceModuleAppsCommand.setModuleId(40100L);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);

		if (apps!=null && apps.getServiceModuleApps().size()>0)
			flowCaseCmd.setTitle(apps.getServiceModuleApps().get(0).getName());
		else
			flowCaseCmd.setTitle("园区入驻");

		return flowService.createFlowCase(flowCaseCmd);

    }

    private String getBriefContent(EnterpriseOpRequest request, String buildingName) {
        String locale = UserContext.current().getUser().getLocale();
        Map<String, Object> map = new HashMap<>();

        map.put("buildingName", buildingName);

        map.put("sourceType", defaultIfNull(getSourceTypeName(request.getSourceType()),""));

        return localeTemplateService.getLocaleTemplateString(ApplyEntryErrorCodes.SCOPE,
                ApplyEntryErrorCodes.FLOW_BRIEF_CONTENT_CODE, locale, map, "");
    }

    @Override
    public String getSourceTypeName(String type) {

		GetLeasePromotionConfigCommand cmd = new GetLeasePromotionConfigCommand();
		cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		LeasePromotionConfigDTO config = getLeasePromotionConfig(cmd);

		ApplyEntrySourceType sourceType = ApplyEntrySourceType.fromType(type);
		String sourceTypeName = null != sourceType ? sourceType.getDescription() : "";

		byte i = -1;
		if (ApplyEntrySourceType.BUILDING == sourceType) {
			i = LeasePromotionOrder.PARK_INTRODUCE.getCode();
		}else if (ApplyEntrySourceType.FOR_RENT == sourceType) {
			i = LeasePromotionOrder.LEASE_PROMOTION.getCode();
		}
		if (null != config.getDisplayNames() ) {
			int configSize = config.getDisplayNames().size();
			int totalSize = LeasePromotionOrder.values().length;
			int currentSize = totalSize - configSize;
			for (Integer k: config.getDisplayOrders()) {
				if (k.byteValue() ==i) {
					sourceTypeName =config.getDisplayNames().get(k - 1 - currentSize);
				}
			}
		}

		return sourceTypeName;
	}

    private String defaultIfNull(String obj, String defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
	 * 用户{userName}（手机号：{userPhone}）于{applyTime}提交了预约{applyType}申请：
 	 * 参观位置：{location}
 	 * 面积需求：{area}
	 * 公司名称：{enterpriseName}
	 * 备注：{description}
	 * */
	private void sendApplyEntrySmsToManager(String phoneNumber,String userName,String userPhone,String applyTime,String location
			,String area,String enterpriseName,String description , Integer namespaceId,String applyType){
		
		List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_USERNAME, processNull(userName));
		smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_USERPHONE, processNull(userPhone)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_APPLYTIME, processNull(applyTime)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_APPLYTYPE, processNull(applyType)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_LOCATION, processNull(location)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_AREA, processNull(area)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_ENTERPRISENAME, processNull(enterpriseName)); 
		smsProvider.addToTupleList(variables,SmsTemplateCode.KEY_DESCRIPTION, processNull(description));  
	    String templateScope = SmsTemplateCode.SCOPE;
	    int templateId = SmsTemplateCode.WEIXIN_APPLY_RENEW_CODE;
	    String templateLocale = UserContext.current().getUser().getLocale();
	    smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);
	}

	public String processNull(String variable){
		if(org.apache.commons.lang.StringUtils.isBlank(variable) )
			return "无";
		else
			return variable;
	}
	
	@Override
	public ListBuildingForRentResponse listLeasePromotions(ListBuildingForRentCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), null,cmd.getCommunityId());//房源招租权限
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), cmd.getCurrentPMId(),cmd.getCommunityId());//房源招租权限
		}
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		ListBuildingForRentResponse res = new ListBuildingForRentResponse();
		if (null==cmd.getRentType()) {
			cmd.setRentType(LeasePromotionType.ORDINARY.getCode());
		}
		//兼容app，4.9.0之前的园区入驻项目介绍列表是项目管理中的楼栋列表，BuildingId是楼栋管理的楼栋id，
		// 现在项目介绍列表是招租管理中的楼栋列表，LeaseBuildingId是招租管理楼栋的id
		if (null != cmd.getLeaseBuildingId()) {
			cmd.setBuildingId(cmd.getLeaseBuildingId());
		}else{
			if (null != cmd.getBuildingId() && cmd.getBuildingId() != 0L) {
				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(cmd.getBuildingId());
				if (null != leaseBuilding) {
					cmd.setBuildingId(leaseBuilding.getId());
				}
			}
		}

		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);
		leasePromotion.setCreateUid(cmd.getUserId());

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
	    
		List<LeasePromotion> leasePromotions = enterpriseApplyEntryProvider.listLeasePromotions(leasePromotion, locator, pageSize);
		
		res.setNextPageAnchor(locator.getAnchor());

		//检查当前用户是不是有权限发布招租
		Long userId = UserContext.currentUserId();
		CheckIsLeaseIssuerDTO flag = new CheckIsLeaseIssuerDTO();
		flag.setFlag(LeasePromotionFlag.DISABLED.getCode());
		if (null != cmd.getOrganizationId()) {
			CheckIsLeaseIssuerCommand cmd2 = new CheckIsLeaseIssuerCommand();
			cmd2.setOrganizationId(cmd.getOrganizationId());
			flag.setFlag(checkIsLeaseIssuer(cmd2).getFlag());
		}

		List<BuildingForRentDTO> dtos = leasePromotions.stream().map((c) ->{
            BuildingForRentDTO dto = ConvertHelper.convert(c, BuildingForRentDTO.class);
			dto.setLeasePromotionFormId(c.getGeneralFormId());

			populateRentDTO(dto, c);

			//判断此招租，当前登录的人是否有权限删除，编辑
			dto.setDeleteFlag(LeasePromotionDeleteFlag.NOTSUPPROT.getCode());
			if (LeaseIssuerType.NORMAL_USER.getCode().equals(c.getIssuerType())) {
				if (LeasePromotionFlag.ENABLED.getCode() == flag.getFlag() && c.getCreateUid().equals(userId)) {
					dto.setDeleteFlag(LeasePromotionDeleteFlag.SUPPROT.getCode());
				}
			}

            return dto;
		}).collect(Collectors.toList());
		
		res.setDtos(dtos);
		return res;
	}

	private void populateRentDTO(BuildingForRentDTO dto, LeasePromotion leasePromotion) {

		//设置详情url
		processDetailUrl(dto);
		//有门牌id时，表示此招租和楼栋门牌关联，不是手动输入门牌地址
		if (null != leasePromotion.getAddressId() && leasePromotion.getAddressId() != 0L) {
			Address address = addressProvider.findAddressById(leasePromotion.getAddressId());
			if (null != address) {
				dto.setApartmentName(address.getApartmentName());
			}
		}
		//有楼栋id时，表示此招租和楼栋关联，不是手动输入楼栋地址
		if (null != dto.getBuildingId() && dto.getBuildingId() != 0L) {
			LeaseBuilding building = enterpriseApplyBuildingProvider.findLeaseBuildingById(dto.getBuildingId());
			if(null != building){
				dto.setBuildingName(building.getName());
				dto.setCommunityId(building.getCommunityId());
				Community community = communityProvider.findCommunityById(building.getCommunityId());
				dto.setCommunityName(community.getName());
			}
		}else {
			dto.setCommunityName("其他");
		}
		//兼容历史app，rentPosition字段值返回的就是楼栋名称
		dto.setRentPosition(dto.getBuildingName());

		Long userId = UserContext.currentUserId();
		//设置封面图url 和banner图
		if (null != leasePromotion.getPosterUri()) {
			dto.setPosterUrl(contentServerService.parserUri(leasePromotion.getPosterUri(), EntityType.USER.getCode(), userId));
		}else {
			String uri = configurationProvider.getValue("apply.entry.default.post.url", "");
			dto.setPosterUrl(contentServerService.parserUri(uri, EntityType.USER.getCode(), userId));
		}

		List<LeasePromotionAttachment> attachments = findAttachmentsByOwnerTypeAndOwnerId(EntityType.LEASE_PROMOTION.getCode(), dto.getId());
		dto.setAttachments(attachments.stream().map(a -> {
			BuildingForRentAttachmentDTO ad = ConvertHelper.convert(a, BuildingForRentAttachmentDTO.class);
			ad.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.USER.getCode(), userId));
			return ad;
		}).collect(Collectors.toList()));
		//暂时用枚举，如果拓展单位类型，则须在表中添加字段

		//	启用表单，则查询表单值
		if (LeasePromotionFlag.ENABLED.getCode() == leasePromotion.getCustomFormFlag()) {

			GetGeneralFormValuesCommand cmd = new GetGeneralFormValuesCommand();
			cmd.setSourceType(EntityType.LEASE_PROMOTION.getCode());
			cmd.setSourceId(dto.getId());
			cmd.setOriginFieldFlag(NormalFlag.NEED.getCode());
			List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd);
			dto.setFormValues(formValues);

//			LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(dto.getNamespaceId(),
//					dto.getCommunityId(), EntityType.COMMUNITY.getCode(), EntityType.LEASE_PROMOTION.getCode());
//			if (null != request) {
//				dto.setRequestFormId(request.getSourceId());
//			}
		}

		dto.setProjectDTOS(getProjectDTOs(dto.getId()));

		GetLeasePromotionConfigCommand configCmd = new GetLeasePromotionConfigCommand();
		configCmd.setNamespaceId(leasePromotion.getNamespaceId());
		LeasePromotionConfigDTO config = getLeasePromotionConfig(configCmd);
		dto.setConsultFlag(config.getConsultFlag());
		dto.setUnit(config.getRentAmountUnit());

	}

    private void processDetailUrl(BuildingForRentDTO dto) {
		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId(), dto.getNamespaceId());

		dto.setDetailUrl(homeUrl + detailUrl);

		String buildingDetailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_BUILDING_DETAIL_URL, "");

		buildingDetailUrl = String.format(buildingDetailUrl, dto.getBuildingId());
		dto.setBuildingDetailUrl(homeUrl + buildingDetailUrl);
    }

	@Override
	public BuildingForRentDTO createLeasePromotion(CreateLeasePromotionCommand cmd, Byte adminFlag){
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), null,0L);
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), cmd.getCurrentPMId(),cmd.getCommunityId());
		}
		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getIssuerType()) {
			cmd.setIssuerType(LeaseIssuerType.ORGANIZATION.getCode());
		}
		if (null == cmd.getCustomFormFlag()) {
			cmd.setCustomFormFlag(LeasePromotionFlag.DISABLED.getCode());
		}
		if (null == cmd.getRentType()) {
			cmd.setRentType(LeasePromotionType.ORDINARY.getCode());
		}

		if (null == cmd.getBuildingId()) {
			cmd.setBuildingId(OTHER_BUILDING_ID);
		}else{
			//兼容app业主发布招租，后台楼栋从EhLeaseBuildings查询，app业主发布招租取的楼栋信息是以前的项目管理楼栋信息
			cmd.setBuildingId(processBuildingId(cmd.getBuildingId(), adminFlag));
		}

		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);
		leasePromotion.setNamespaceId(UserContext.getCurrentNamespaceId());
		if (null != cmd.getEnterTime()) {
			leasePromotion.setEnterTime(new Timestamp(cmd.getEnterTime()));
		}

		dbProvider.execute((TransactionStatus status) -> {

			enterpriseApplyEntryProvider.createLeasePromotion(leasePromotion);

			addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.LEASE_PROMOTION.getCode(),
					leasePromotion.getId(), cmd.getCustomFormFlag());

			addAttachments(cmd.getAttachments(), leasePromotion);

			if (null != cmd.getCommunityIds()) {
				cmd.getCommunityIds().forEach(m -> {
					LeasePromotionCommunity leasePromotionCommunity = new LeasePromotionCommunity();
					leasePromotionCommunity.setLeasePromotionId(leasePromotion.getId());
					leasePromotionCommunity.setCommunityId(m);
					enterpriseApplyBuildingProvider.createLeasePromotionCommunity(leasePromotionCommunity);
				});
			}else if (null != cmd.getCommunityId()) {
				LeasePromotionCommunity leasePromotionCommunity = new LeasePromotionCommunity();
				leasePromotionCommunity.setLeasePromotionId(leasePromotion.getId());
				leasePromotionCommunity.setCommunityId(cmd.getCommunityId());
				enterpriseApplyBuildingProvider.createLeasePromotionCommunity(leasePromotionCommunity);
			}

			return null;
		});

		BuildingForRentDTO dto = ConvertHelper.convert(leasePromotion, BuildingForRentDTO.class);

		populateRentDTO(dto, leasePromotion);

		//普通业主发布的招租，可以删除
		if (LeaseIssuerType.NORMAL_USER.getCode().equals(leasePromotion.getIssuerType())) {
			dto.setDeleteFlag(LeasePromotionFlag.ENABLED.getCode());
		}

		return dto;
	}

	private void addGeneralFormInfo(Long generalFormId, List<PostApprovalFormItem> formValues, String sourceType,
		Long sourceId, Byte customFormFlag) {
		if (LeasePromotionFlag.ENABLED.getCode() == customFormFlag) {
			addGeneralFormValuesCommand cmd = new addGeneralFormValuesCommand();
			cmd.setGeneralFormId(generalFormId);
			cmd.setValues(formValues);
			cmd.setSourceId(sourceId);
			cmd.setSourceType(sourceType);
			generalFormService.addGeneralFormValues(cmd);
		}
	}

	private Long processBuildingId(Long buildingId, Byte adminFlag) {
		if (adminFlag == (byte)2) {
			LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(buildingId);
			if (null != leaseBuilding) {
				return leaseBuilding.getId();
			}
		}
		return buildingId;
	}

	@Override
	public BuildingForRentDTO updateLeasePromotion(UpdateLeasePromotionCommand cmd, Byte adminFlag){

		if (null == cmd.getBuildingId()) {
			cmd.setBuildingId(OTHER_BUILDING_ID);
		}else{
			//兼容app业主发布招租，后台楼栋从EhLeaseBuildings查询，app业主发布招租取的楼栋信息是以前的项目管理楼栋信息
			cmd.setBuildingId(processBuildingId(cmd.getBuildingId(), adminFlag));
		}

		return dbProvider.execute((TransactionStatus status) -> {

			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());

			if (cmd.getBuildingId() == null) {
				cmd.setBuildingId(0L);
			}

			BeanUtils.copyProperties(cmd, leasePromotion);
//			leasePromotion.setBuildingId(cmd.getBuildingId());
//			leasePromotion.setRentPosition(cmd.getRentPosition());
//			leasePromotion.setPosterUri(cmd.getPosterUri());
//			leasePromotion.setRentAreas(cmd.getRentAreas());
//			leasePromotion.setContacts(cmd.getContacts());
//			leasePromotion.setContactPhone(cmd.getContactPhone());
//			leasePromotion.setDescription(cmd.getDescription());
//
//			leasePromotion.setEnterTimeFlag(cmd.getEnterTimeFlag());
			leasePromotion.setEnterTime(null);
			if (LeasePromotionFlag.ENABLED.getCode() == leasePromotion.getEnterTimeFlag()) {
				leasePromotion.setEnterTime(new Timestamp(cmd.getEnterTime()));
			}
//			leasePromotion.setAddressId(cmd.getAddressId());
//			leasePromotion.setOrientation(cmd.getOrientation());
//			leasePromotion.setRentAmount(cmd.getRentAmount());
//			leasePromotion.setLatitude(cmd.getLatitude());
//			leasePromotion.setLongitude(cmd.getLongitude());
//			leasePromotion.setAddress(cmd.getAddress());
//
//			leasePromotion.setCustomFormFlag(cmd.getCustomFormFlag());
//			leasePromotion.setGeneralFormId(cmd.getGeneralFormId());
			if (null == cmd.getCustomFormFlag()) {
				leasePromotion.setCustomFormFlag(LeasePromotionFlag.DISABLED.getCode());
			}

			enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);

			//表单
			if (LeasePromotionFlag.ENABLED.getCode() == leasePromotion.getCustomFormFlag()) {
				generalFormValProvider.deleteGeneralFormVals(EntityType.LEASE_PROMOTION.getCode(), leasePromotion.getId());
				addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.LEASE_PROMOTION.getCode(),
						leasePromotion.getId(), cmd.getCustomFormFlag());
			}

			//先删除全部图片 重新添加
			enterpriseApplyEntryProvider.deleteLeasePromotionAttachment(EntityType.LEASE_PROMOTION.getCode(), leasePromotion.getId());
			addAttachments(cmd.getAttachments(), leasePromotion);

			if (leasePromotion.getIssuerType().equals(LeaseIssuerType.ORGANIZATION.getCode())) {
				enterpriseApplyBuildingProvider.deleteLeasePromotionCommunity(leasePromotion.getId());
				if (null != cmd.getCommunityIds()) {
					cmd.getCommunityIds().forEach(m -> {
						LeasePromotionCommunity leasePromotionCommunity = new LeasePromotionCommunity();
						leasePromotionCommunity.setLeasePromotionId(leasePromotion.getId());
						leasePromotionCommunity.setCommunityId(m);
						enterpriseApplyBuildingProvider.createLeasePromotionCommunity(leasePromotionCommunity);
					});
				}
			}

			BuildingForRentDTO dto = ConvertHelper.convert(leasePromotion, BuildingForRentDTO.class);

			populateRentDTO(dto, leasePromotion);

			//当前用户可以更新
			//普通业主发布的招租，可以删除
			if (LeaseIssuerType.NORMAL_USER.getCode().equals(leasePromotion.getIssuerType())) {
				dto.setDeleteFlag(LeasePromotionDeleteFlag.SUPPROT.getCode());
			}
			return dto;
		});
	}

	private void addAttachments(List<BuildingForRentAttachmentDTO> attachmentDTOs, LeasePromotion leasePromotion) {
		if (null != attachmentDTOs) {
			for (BuildingForRentAttachmentDTO buildingForRentAttachmentDTO : attachmentDTOs) {
				LeasePromotionAttachment attachment = ConvertHelper.convert(buildingForRentAttachmentDTO, LeasePromotionAttachment.class);
				attachment.setOwnerId(leasePromotion.getId());
				attachment.setOwnerType(EntityType.LEASE_PROMOTION.getCode());
				attachment.setCreatorUid(leasePromotion.getCreateUid());
				enterpriseApplyEntryProvider.addPromotionAttachment(attachment);
			}
		}
	}

	private List<LeasePromotionAttachment> findAttachmentsByOwnerTypeAndOwnerId(String ownerType, Long ownerId) {

		return enterpriseApplyEntryProvider.findAttachmentsByOwnerTypeAndOwnerId(ownerType, ownerId);
	}

	@Override
	public BuildingForRentDTO findLeasePromotionById(Long id){
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(id);

        BuildingForRentDTO dto = ConvertHelper.convert(leasePromotion, BuildingForRentDTO.class);

		dto.setLeasePromotionFormId(leasePromotion.getGeneralFormId());

		populateRentDTO(dto, leasePromotion);

		return dto;
	}

	private List<ProjectDTO> getProjectDTOs(Long id) {
		List<Long> communityIds = enterpriseApplyBuildingProvider.listLeasePromotionCommunities(id);
		if (communityIds!=null && communityIds.size()>0) {
			Map<Long, Community> temp = communityProvider.listCommunitiesByIds(communityIds);
			return temp.values().stream().map(r -> {
				ProjectDTO projectDTO = new ProjectDTO();
				projectDTO.setProjectId(r.getId());
				projectDTO.setProjectName(r.getName());

				return projectDTO;
			}).collect(Collectors.toList());
		}else
			return null;
	}

	@Override
	public boolean updateLeasePromotionStatus(UpdateLeasePromotionStatusCommand cmd){
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());
		
		if(leasePromotion == null){
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter.");
		}
		
		return enterpriseApplyEntryProvider.updateLeasePromotionStatus(cmd.getId(), cmd.getStatus());
		
	}
	
	@Override
	public boolean updateApplyEntryStatus(UpdateApplyEntryStatusCommand cmd){

		EnterpriseOpRequest request = enterpriseApplyEntryProvider.getApplyEntryById(cmd.getId());
		
		return enterpriseApplyEntryProvider.updateApplyEntryStatus(cmd.getId(), cmd.getStatus());
		
	}
	
	@Override
	public boolean deleteApplyEntry(DeleteApplyEntryCommand cmd){
		return enterpriseApplyEntryProvider.deleteApplyEntry(cmd.getId());
	}
	
	@Override
	public boolean deleteLeasePromotion(DeleteLeasePromotionCommand cmd){
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());

		if (null == leasePromotion) {
			LOGGER.error("LeasePromotion not found, cmd={}", cmd);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"LeasePromotion not found.");
		}
		leasePromotion.setStatus(LeasePromotionStatus.INACTIVE.getCode());
		return enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);
	}

	@Override
	public ListLeaseIssuersResponse listLeaseIssuers(ListLeaseIssuersCommand cmd) {
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		ListLeaseIssuersResponse resp = new ListLeaseIssuersResponse();

		List<LeaseIssuer> issuers = enterpriseLeaseIssuerProvider.listLeaseIssuers(cmd.getNamespaceId(), null,
				cmd.getKeyword(), cmd.getCategoryId(), cmd.getPageAnchor(), pageSize);

		int size = issuers.size();

		if (size > 0) {
			if (size == pageSize)
				resp.setNextPageAnchor(issuers.get(size -1).getId());
		}

		resp.setRequests(issuers.stream().map(r -> {
			LeaseIssuerDTO dto = ConvertHelper.convert(r, LeaseIssuerDTO.class);
			//TODO:set address
            if (null != r.getEnterpriseId()) {
                Organization org = organizationProvider.findOrganizationById(r.getEnterpriseId());
                OrganizationDetail orgDetail = organizationProvider.findOrganizationDetailByOrganizationId(r.getEnterpriseId());
                if (null != orgDetail) {
                    dto.setIssuerContact(orgDetail.getContact());
                }

                dto.setIssuerName(org.getName());
//                dto.setIssuerContact(org.get);
                List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(r.getEnterpriseId());
                dto.setAddresses(organizationAddresses.stream().map(a -> {
                    Address address = addressProvider.findAddressById(a.getAddressId());
                    return ConvertHelper.convert(address, AddressDTO.class);
                }).collect(Collectors.toList()));
            }else {
                List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIssuerAddresses(r.getId(), null);
                dto.setAddresses(addresses.stream().map(a -> {
                    Address address = addressProvider.findAddressById(a.getAddressId());
                    return ConvertHelper.convert(address, AddressDTO.class);
                }).collect(Collectors.toList()));
            }
			return dto;
		}).collect(Collectors.toList()));

		return resp;
	}

    @Override
    public void deleteLeaseIssuer(DeleteLeaseIssuerCommand cmd) {
        LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.getLeaseIssuerById(cmd.getId());

        if (null == leaseIssuer) {
            LOGGER.error("LeaseIssuer not found, cmd={}", cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "LeaseIssuer not found.");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        dbProvider.execute((TransactionStatus status) -> {
            //删除人
            enterpriseLeaseIssuerProvider.deleteLeaseIssuer(leaseIssuer);
            //删除地址
            enterpriseLeaseIssuerProvider.deleteLeaseIssuerAddressByLeaseIssuerId(leaseIssuer.getId());
            //删除招租信息
            //判断是普通用户还是企业
            if (!StringUtils.isEmpty(leaseIssuer.getIssuerContact())) {

                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId,
                        leaseIssuer.getIssuerContact());

                if (null != userIdentifier) {

                    List<LeasePromotion> list = enterpriseApplyEntryProvider.listLeasePromotionsByUidAndIssuerType(userIdentifier.getOwnerUid(),
                            LeaseIssuerType.NORMAL_USER.getCode());

                    if (!CollectionUtils.isEmpty(list)) {

                        List<Long> ids = list.stream().map(LeasePromotion::getId).collect(Collectors.toList());
                        enterpriseApplyEntryProvider.deleteApplyEntrysByLeasePromotionIds(ids);

                        enterpriseApplyEntryProvider.deleteLeasePromotionByUidAndIssuerType(userIdentifier.getOwnerUid(),
                                LeaseIssuerType.NORMAL_USER.getCode());
                    }
                }
            }else {
                //先查公司管理员，删除公司管理员发的招租
                ListServiceModuleAdministratorsCommand cmd2 = new ListServiceModuleAdministratorsCommand();
                cmd2.setOrganizationId(leaseIssuer.getEnterpriseId());
                List<OrganizationContactDTO> users = rolePrivilegeService.listOrganizationAdministrators(cmd2);
                if (null != users) {
                    for (OrganizationContactDTO u: users) {

                        List<LeasePromotion> list = enterpriseApplyEntryProvider.listLeasePromotionsByUidAndIssuerType(u.getTargetId(),
                                LeaseIssuerType.NORMAL_USER.getCode());
                        if (!CollectionUtils.isEmpty(list)) {
                            List<Long> ids = list.stream().map(r -> r.getId()).collect(Collectors.toList());

                            enterpriseApplyEntryProvider.deleteApplyEntrysByLeasePromotionIds(ids);

                            enterpriseApplyEntryProvider.deleteLeasePromotionByUidAndIssuerType(u.getTargetId(),
                                    LeaseIssuerType.NORMAL_USER.getCode());
                        }
                    }
                }
            }
            return null;
        });

    }

    @Override
    public void addLeaseIssuer(AddLeaseIssuerCommand cmd) {

        if (null == cmd.getCommunityId()) {
            LOGGER.error("Invalid communityId param, cmd={}", cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId param.");
        }
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

        dbProvider.execute((TransactionStatus status) -> {

            if (null != cmd.getEnterpriseIds()) {
                for (Long enterpriseId : cmd.getEnterpriseIds()) {

                    LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.fingLeaseIssuersByOrganizationId(cmd.getNamespaceId(), enterpriseId,
							cmd.getCategoryId());
                    //已存在，过滤掉
                    if (null == leaseIssuer) {
                        leaseIssuer = ConvertHelper.convert(cmd, LeaseIssuer.class);
                        leaseIssuer.setNamespaceId(cmd.getNamespaceId());
                        leaseIssuer.setEnterpriseId(enterpriseId);
                        enterpriseLeaseIssuerProvider.createLeaseIssuer(leaseIssuer);
                    }
                }
            } else {
                LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssuersByContact(cmd.getNamespaceId(), cmd.getIssuerContact(),
						cmd.getCategoryId());

                if (null != leaseIssuer) {
                    LOGGER.error("LeaseIssuer exist, cmd={}", cmd);
                    throw errorWith(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.LEASE_ISSUER_EXIST,
                            "LeaseIssuer exist.");
                }

                if (null == cmd.getAddressIds()) {
                    LOGGER.error("Invalid addressIds param, cmd={}", cmd);
                    throw errorWith(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.LEASE_ISSUER_EXIST,
                            "Invalid addressIds param.");
                }

                leaseIssuer = ConvertHelper.convert(cmd, LeaseIssuer.class);
                leaseIssuer.setNamespaceId(UserContext.getCurrentNamespaceId());
                //TODO:门牌地址
                enterpriseLeaseIssuerProvider.createLeaseIssuer(leaseIssuer);

                for (Long id : cmd.getAddressIds()) {
                    Address address = addressProvider.findAddressById(id);
                    com.everhomes.building.Building building = buildingProvider.findBuildingByName(address.getNamespaceId(),
                            address.getCommunityId(), address.getBuildingName());
                    LeaseIssuerAddress leaseIssuerAddress = new LeaseIssuerAddress();
                    leaseIssuerAddress.setAddressId(id);
                    leaseIssuerAddress.setLeaseIssuerId(leaseIssuer.getId());
                    leaseIssuerAddress.setBuildingId(building.getId());
                    createLeaseIssuerAddress(leaseIssuerAddress);
                }
            }
            return null;
        });

    }

    private void createLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress) {
        leaseIssuerAddress.setStatus((byte)2);
        leaseIssuerAddress.setCreatorUid(UserContext.current().getUser().getId());
        enterpriseLeaseIssuerProvider.createLeaseIssuerAddress(leaseIssuerAddress);
    }

    @Override
    public LeasePromotionConfigDTO getLeasePromotionConfig(GetLeasePromotionConfigCommand cmd) {
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		LeasePromotionConfigDTO dto = new LeasePromotionConfigDTO();

		dto.setNamespaceId(cmd.getNamespaceId());
		dto.setRentAmountFlag(LeasePromotionFlag.DISABLED.getCode());
//		dto.setRentAmountUnit();
		dto.setIssuingLeaseFlag(LeasePromotionFlag.DISABLED.getCode());
		dto.setRenewFlag(LeasePromotionFlag.DISABLED.getCode());
		dto.setAreaSearchFlag(LeasePromotionFlag.DISABLED.getCode());
		dto.setConsultFlag(LeasePromotionFlag.DISABLED.getCode());
		dto.setBuildingIntroduceFlag(LeasePromotionFlag.DISABLED.getCode());
		dto.setHideAddressFlag(LeasePromotionFlag.DISABLED.getCode());
		String[] defaultNames = {"项目介绍","房源招租"};
		String[] defaultOrders = {"1","2"};
		dto.setDisplayNames(Arrays.stream(defaultNames).collect(Collectors.toList()));
		dto.setDisplayOrders(Arrays.stream(defaultOrders).map(Integer::valueOf).collect(Collectors.toList()));

		List<LeasePromotionConfig> configs = enterpriseLeaseIssuerProvider.listLeasePromotionConfigs(cmd.getNamespaceId(), cmd.getCategoryId());
        if (null != configs) {
			configs.forEach(c -> {
				String name = c.getConfigName();
				LeasePromotionConfigType type = LeasePromotionConfigType.fromCode(name);
				switch (type) {
					case RENT_AMOUNT_FLAG: dto.setRentAmountFlag(Byte.valueOf(c.getConfigValue())); break;
					case RENT_AMOUNT_UNIT: dto.setRentAmountUnit(LeasePromotionUnit.fromType(c.getConfigValue()).getDescription()); break;
					case ISSUING_LEASE_FLAG: dto.setIssuingLeaseFlag(Byte.valueOf(c.getConfigValue())); break;
					case RENEW_FLAG: dto.setRenewFlag(Byte.valueOf(c.getConfigValue())); break;
					case AREA_SEARCH_FLAG: dto.setAreaSearchFlag(Byte.valueOf(c.getConfigValue())); break;
					case CONSULT_FLAG: dto.setConsultFlag(Byte.valueOf(c.getConfigValue())); break;
					case BUILDING_INTRODUCE_FLAG: dto.setBuildingIntroduceFlag(Byte.valueOf(c.getConfigValue())); break;
					case HIDE_ADDRESS_FLAG: dto.setHideAddressFlag(Byte.valueOf(c.getConfigValue()));break;
					case DISPLAY_NAME_STR:
						String displayNameStr = c.getConfigValue();
						String[] names = displayNameStr.split(",");
						dto.setDisplayNames(Arrays.stream(names).collect(Collectors.toList()));

						break;
					case DISPLAY_ORDER_STR:
						String displayOrderStr = c.getConfigValue();
						String[] orders = displayOrderStr.split(",");
						dto.setDisplayOrders(Arrays.stream(orders).map(Integer::valueOf).collect(Collectors.toList()));

						break;
					default: break;
				}
			});
		}

        return dto;

    }

	@Override
	public void setLeasePromotionConfig(SetLeasePromotionConfigCommand cmd) {
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040120L, cmd.getAppId(), null,0L);//楼栋介绍权限
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040120L, cmd.getAppId(), cmd.getCurrentPMId(),cmd.getCurrentProjectId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		//如果开启就添加到数据库中，否则默认关闭
		if (LeasePromotionFlag.ENABLED.getCode() == cmd.getBuildingIntroduceFlag()) {

			LeasePromotionConfig config = enterpriseLeaseIssuerProvider.findLeasePromotionConfig(cmd.getNamespaceId(),
					"buildingIntroduceFlag", cmd.getCategoryId());

			if (null != config) {
				config.setConfigValue(String.valueOf(cmd.getBuildingIntroduceFlag()));
				enterpriseLeaseIssuerProvider.updateLeasePromotionConfig(config);
			}else {
				config = new LeasePromotionConfig();
				config.setNamespaceId(cmd.getNamespaceId());
				config.setConfigName("buildingIntroduceFlag");
				config.setConfigValue(String.valueOf(cmd.getBuildingIntroduceFlag()));
				config.setCategoryId(cmd.getCategoryId());
				enterpriseLeaseIssuerProvider.createLeasePromotionConfig(config);
			}

		}else {
			enterpriseLeaseIssuerProvider.deleteLeasePromotionConfig(cmd.getNamespaceId(),
					"buildingIntroduceFlag", cmd.getCategoryId());
		}

	}

    @Override
    public CheckIsLeaseIssuerDTO checkIsLeaseIssuer(CheckIsLeaseIssuerCommand cmd) {
        CheckIsLeaseIssuerDTO dto = new CheckIsLeaseIssuerDTO();
		Long organizationId = cmd.getOrganizationId();
		User user = UserContext.current().getUser();

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		dto.setFlag(LeasePromotionFlag.DISABLED.getCode());

		//检查是不是招租发行人
		if (null != enterpriseLeaseIssuerProvider.findLeaseIssuersByContact(cmd.getNamespaceId(), identifier.getIdentifierToken(),
				cmd.getCategoryId())) {
			dto.setFlag(LeasePromotionFlag.ENABLED.getCode());
		}

		if (null != organizationId) {
			//检查是不是招租发行公司
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssuersByOrganizationId(cmd.getNamespaceId(), organizationId,
					cmd.getCategoryId())) {
				SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
				if (resolver.checkOrganizationAdmin(user.getId(), organizationId)) {
					dto.setFlag(LeasePromotionFlag.ENABLED.getCode());
				}
			}
		}

        return dto;
    }

    @Override
    public ListLeaseIssuerBuildingsResponse listBuildings(ListLeaseIssuerBuildingsCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

        ListLeaseIssuerBuildingsResponse response = new ListLeaseIssuerBuildingsResponse();
		Long organizationId = cmd.getOrganizationId();

        User user = UserContext.current().getUser();
        UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		List<BuildingDTO> buildingDTOs = new ArrayList<>();

		//先查询业主
		LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssuersByContact(cmd.getNamespaceId(), identifier.getIdentifierToken(),
				cmd.getCategoryId());
		if (null != leaseIssuer) {
			List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIssuerBuildings(leaseIssuer.getId());

			addresses.stream().map(a -> {
				Address address = addressProvider.findAddressById(a.getAddressId());
				com.everhomes.building.Building building = buildingProvider.findBuildingByName(address.getNamespaceId(),
						address.getCommunityId(), address.getBuildingName());
				return ConvertHelper.convert(building, BuildingDTO.class);
			}).collect(Collectors.toList()).forEach(b -> {
				if (null != b) {
					buildingDTOs.add(b);
				}
			});
		}

		if (null != organizationId)  {
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssuersByOrganizationId(cmd.getNamespaceId(), organizationId,
					cmd.getCategoryId())) {
				SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
				if (resolver.checkOrganizationAdmin(user.getId(), organizationId)) {
					List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organizationId);

					organizationAddresses.stream().map(a -> {
						Address address = addressProvider.findAddressById(a.getAddressId());
						com.everhomes.building.Building building = buildingProvider.findBuildingByName(address.getNamespaceId(),
								address.getCommunityId(), address.getBuildingName());
						return ConvertHelper.convert(building, BuildingDTO.class);
					}).collect(Collectors.toSet()).forEach(b -> {
						if (null != b) {
							buildingDTOs.add(b);
						}
					});
				}
			}
		}

		response.setBuildings(buildingDTOs);
        return response;
    }

	@Override
	public List<AddressDTO> listLeaseIssuerApartments(ListLeaseIssuerApartmentsCommand cmd) {
		List<AddressDTO> dtos = new ArrayList<>();
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		User user = UserContext.current().getUser();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		Long organizationId = cmd.getOrganizationId();

		//先查询业主
		LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssuersByContact(cmd.getNamespaceId(), identifier.getIdentifierToken(),
				cmd.getCategoryId());
		if (null != leaseIssuer) {
			List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIssuerAddresses(leaseIssuer.getId(), cmd.getBuildingId());

			dtos.addAll(addresses.stream().map(a -> {
				Address address = addressProvider.findAddressById(a.getAddressId());
				return ConvertHelper.convert(address, AddressDTO.class);
			}).collect(Collectors.toList()));
		}

		if (null != organizationId)  {
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssuersByOrganizationId(cmd.getNamespaceId(), organizationId,
					cmd.getCategoryId())) {
				SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
				if (resolver.checkOrganizationAdmin(user.getId(), organizationId)) {
					List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organizationId);

					com.everhomes.building.Building building = buildingProvider.findBuildingById(cmd.getBuildingId());

					dtos.addAll(organizationAddresses.stream().filter(a -> {
						return a.getBuildingName().equals(building.getName());
					}).map(a -> {
						Address address = addressProvider.findAddressById(a.getAddressId());
						return ConvertHelper.convert(address, AddressDTO.class);
					}).collect(Collectors.toSet()));
				}
			}
		}

		return dtos;
	}

	@Override
	public void updateLeasePromotionRequestForm(UpdateLeasePromotionRequestFormCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
				null, null, cmd.getSourceType(), cmd.getCategoryId());

		if (null == request) {
			if (null != cmd.getSourceId()) {
				request = ConvertHelper.convert(cmd, LeaseFormRequest.class);
				enterpriseApplyEntryProvider.createLeaseRequestForm(request);
			}

		}else {
			if (null != cmd.getSourceId()) {
				request.setSourceId(cmd.getSourceId());
				enterpriseApplyEntryProvider.updateLeaseRequestForm(request);
			}else {
				//当没有sourced的时候表示没有设置表单，即删除关联关系
				enterpriseApplyEntryProvider.deleteLeaseRequestForm(request);
			}
		}

	}

	@Override
	public LeaseFormRequestDTO getLeasePromotionRequestForm(GetLeasePromotionRequestFormCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		}

		LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
				null, null, cmd.getSourceType(), cmd.getCategoryId());

		LeaseFormRequestDTO dto = ConvertHelper.convert(request, LeaseFormRequestDTO.class);

		if (null == dto) {
			dto = new LeaseFormRequestDTO();
			dto.setCustomFormFlag(LeasePromotionFlag.DISABLED.getCode());
		}else {
//			GetTemplateByFormIdCommand getTemplateByFormIdCommand = new GetTemplateByFormIdCommand();
//			getTemplateByFormIdCommand.setFormId(request.getSourceId());
//			GeneralFormDTO form = generalFormService.getTemplateByFormId(getTemplateByFormIdCommand);

			GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(request.getSourceId());

			if (form != null) {
				GeneralFormDTO formDTO = ConvertHelper.convert(form, GeneralFormDTO.class);
				List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
				formDTO.setFormFields(fieldDTOs);

				dto.setForm(formDTO);

				dto.setCustomFormFlag(LeasePromotionFlag.ENABLED.getCode());
			}
		}

		return dto;
	}

	@Override
	public void updateLeasePromotionOrder(UpdateLeasePromotionOrderCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), null,0L);
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4010040130L, cmd.getAppId(), cmd.getCurrentPMId(),cmd.getCurrentProjectId());
		}
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		if (null == cmd.getExchangeId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid exchangeId parameter in the command");
		}
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());
		LeasePromotion exchangeLeasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getExchangeId());

		if (null == leasePromotion) {
			LOGGER.error("LeasePromotion not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"LeasePromotion not found");
		}
		if (null == exchangeLeasePromotion) {
			LOGGER.error("LeasePromotion not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"LeasePromotion not found");
		}

		Long order = leasePromotion.getDefaultOrder();
		Long exchangeOrder = exchangeLeasePromotion.getDefaultOrder();

		dbProvider.execute((TransactionStatus status) -> {
			leasePromotion.setDefaultOrder(exchangeOrder);
			exchangeLeasePromotion.setDefaultOrder(order);
			enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);
			enterpriseApplyEntryProvider.updateLeasePromotion(exchangeLeasePromotion);
			return null;
		});
	}

	@Autowired
	public EnterpriseApplyEntryServiceImpl(SmsProvider smsProvider, ContractProvider contractProvider,
		BuildingProvider buildingProvider, ContractBuildingMappingProvider contractBuildingMappingProvider,
		EnterpriseOpRequestBuildingProvider enterpriseOpRequestBuildingProvider, OrganizationProvider organizationProvider,
		ConfigurationProvider configurationProvider, EnterpriseApplyEntryProvider enterpriseApplyEntryProvider,
		ContentServerService contentServerService, EnterpriseProvider enterpriseProvider, GroupProvider groupProvider,
		CommunityProvider communityProvider, UserProvider userProvider, YellowPageProvider yellowPageProvider,
		FlowService flowService, DbProvider dbProvider, OrganizationService organizationService,
		LocaleTemplateService localeTemplateService, EnterpriseLeaseIssuerProvider enterpriseLeaseIssuerProvider,
		AddressProvider addressProvider, RolePrivilegeService rolePrivilegeService, GeneralFormService generalFormService,
		GeneralFormValProvider generalFormValProvider, GeneralFormProvider generalFormProvider,
		EnterpriseApplyBuildingProvider enterpriseApplyBuildingProvider, LocaleStringService localeStringService) {
		this.smsProvider = smsProvider;
		this.contractProvider = contractProvider;
		this.buildingProvider = buildingProvider;
		this.contractBuildingMappingProvider = contractBuildingMappingProvider;
		this.enterpriseOpRequestBuildingProvider = enterpriseOpRequestBuildingProvider;
		this.organizationProvider = organizationProvider;
		this.configurationProvider = configurationProvider;
		this.enterpriseApplyEntryProvider = enterpriseApplyEntryProvider;
		this.contentServerService = contentServerService;
		this.enterpriseProvider = enterpriseProvider;
		this.groupProvider = groupProvider;
		this.communityProvider = communityProvider;
		this.userProvider = userProvider;
		this.yellowPageProvider = yellowPageProvider;
		this.flowService = flowService;
		this.dbProvider = dbProvider;
		this.organizationService = organizationService;
		this.localeTemplateService = localeTemplateService;
		this.enterpriseLeaseIssuerProvider = enterpriseLeaseIssuerProvider;
		this.addressProvider = addressProvider;
		this.rolePrivilegeService = rolePrivilegeService;
		this.generalFormService = generalFormService;
		this.generalFormValProvider = generalFormValProvider;
		this.generalFormProvider = generalFormProvider;
		this.enterpriseApplyBuildingProvider = enterpriseApplyBuildingProvider;
		this.localeStringService = localeStringService;
	}

	@Override
	public List<Long> transformToCustomer(TransformToCustomerCommand cmd) {
		List<IntentionCustomerDTO> intentionCustomers = cmd.getIntentionCustomers();
		
		Map<String, CreateInvitedCustomerCommand> finalCommandMap = new HashMap<>();
		for (IntentionCustomerDTO dto : intentionCustomers) {
			if (finalCommandMap.containsKey(dto.getCustomerName())) {
				CreateInvitedCustomerCommand cmd2 = finalCommandMap.get(dto.getCustomerName());
				//企业客户意向房源
				//TODO 得考虑addressId重复的问题,目前招商客户管理和此处都未考虑该问题，2018年9月20日17:23:08
				//如果不存在addressId，有时addressId前端会传0
				if (dto.getAddressId()!=null && dto.getAddressId()!=0) {
					CustomerRequirementDTO requirement = cmd2.getRequirement();
					List<CustomerRequirementAddressDTO> addresses = requirement.getAddresses();
					CustomerRequirementAddressDTO addressDTO = new CustomerRequirementAddressDTO();
					addressDTO.setAddressId(dto.getAddressId());
					addresses.add(addressDTO);
					requirement.setAddresses(addresses);
				}
				//企业客户联系人
				//TODO 得考虑联系人重复的问题，目前招商客户管理和此处都未考虑该问题，2018年9月20日17:23:12
				List<CustomerContactDTO> contacts = cmd2.getContacts();
				CustomerContactDTO contactDTO = new  CustomerContactDTO();
				contactDTO.setName(dto.getApplyUserName());
				contactDTO.setPhoneNumber(dto.getApplyContact());
				contactDTO.setContactType(CustomerContactType.CUSTOMER_CONTACT.getCode());
				contactDTO.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				contacts.add(contactDTO);
				
				enterpriseApplyEntryProvider.updateApplyEntryTransformFlag(dto.getApplyEntryId(), (byte)1);
			}else {
				CreateInvitedCustomerCommand cmd2 = new CreateInvitedCustomerCommand();
				//企业客户
				cmd2.setName(dto.getCustomerName());
				cmd2.setLevelItemId((long)CustomerLevelType.INTENTIONAL_CUSTOMER.getCode());
				cmd2.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				//企业客户意向房源
				List<CustomerRequirementAddressDTO> addressDTOs = new ArrayList<>();
				CustomerRequirementDTO requirementDTO = new CustomerRequirementDTO();
				//如果不存在addressId，有时addressId前端会传0
				if (dto.getAddressId()!=null && dto.getAddressId()!=0) {
					CustomerRequirementAddressDTO addressDTO = new CustomerRequirementAddressDTO();
					addressDTO.setAddressId(dto.getAddressId());
					addressDTOs.add(addressDTO);
				}
				cmd2.setRequirement(requirementDTO);
				requirementDTO.setAddresses(addressDTOs);
				//企业客户联系人
				List<CustomerContactDTO> contacts = new ArrayList<>();
				CustomerContactDTO contactDTO = new  CustomerContactDTO();
				contactDTO.setName(dto.getApplyUserName());
				contactDTO.setPhoneNumber(dto.getApplyContact());
				contactDTO.setContactType(CustomerContactType.CUSTOMER_CONTACT.getCode());
				contactDTO.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				contacts.add(contactDTO);
				cmd2.setContacts(contacts);
				
				finalCommandMap.put(dto.getCustomerName(), cmd2);
				
				enterpriseApplyEntryProvider.updateApplyEntryTransformFlag(dto.getApplyEntryId(), (byte)1);
			}
		}
		
		List<Long> customerIds = new ArrayList<>(); 
		Set<String> customerNameSet = finalCommandMap.keySet();
		for (String customerName : customerNameSet) {
			CreateInvitedCustomerCommand createInvitedCustomerCommand = finalCommandMap.get(customerName);
			createInvitedCustomerCommand.setNamespaceId(cmd.getNamespaceId());
			createInvitedCustomerCommand.setCommunityId(cmd.getCommunityId());
			createInvitedCustomerCommand.setOrgId(cmd.getOrganizationId());
			InvitedCustomerDTO invitedCustomer = invitedCustomerService.createInvitedCustomerWithoutAuth(createInvitedCustomerCommand);
			customerIds.add(invitedCustomer.getId());
		}
		return customerIds;
	}
}
