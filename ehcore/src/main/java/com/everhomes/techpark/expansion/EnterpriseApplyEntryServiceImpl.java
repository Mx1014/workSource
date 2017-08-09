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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.*;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
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
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.yellowPage.YellowPage;
import com.everhomes.yellowPage.YellowPageProvider;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class EnterpriseApplyEntryServiceImpl implements EnterpriseApplyEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryServiceImpl.class);

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
		
		ListEnterpriseApplyEntryResponse res = new ListEnterpriseApplyEntryResponse();
		
		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
		List<EnterpriseOpRequest> enterpriseOpRequests = null;
		//增加了判断buildingId
		if(null == cmd.getBuildingId()) {
			enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, pageSize);
		} else{
			List<EnterpriseOpRequestBuilding> opRequestBuildings = this.enterpriseOpRequestBuildingProvider.queryEnterpriseOpRequestBuildings(null,
					Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
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
			if(idList.size() >0 )
				enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, pageSize,idList);
			
		}
		if(null == enterpriseOpRequests) {
			return res;
		}
		res.setNextPageAnchor(locator.getAnchor());

		List<EnterpriseApplyEntryDTO> dtos = enterpriseOpRequests.stream().map((c) ->{
			EnterpriseApplyEntryDTO dto = ConvertHelper.convert(c, EnterpriseApplyEntryDTO.class);
			//对于有合同的(一定是续租)
			if(null != c.getContractId()){
				Contract contract = contractProvider.findContractById(c.getContractId());
				if(null != contract)
					dto.setContract(organizationService.processContract(contract));
			}

			//填充楼栋门牌
            if (null != c.getAddressId()){
                Address address = addressProvider.findAddressById(c.getAddressId());
                if (null != address){
                    dto.setApartmentName(address.getApartmentName());
                    dto.setBuildingName(address.getBuildingName());
                }
            }

			return dto;
		}).collect(Collectors.toList());
		for (EnterpriseApplyEntryDTO dto : dtos) {
			dto.setBuildings(new ArrayList<BuildingDTO>() );
			//对于不同的类型有不同的楼栋
			if(dto.getApplyType() == ApplyEntryApplyType.RENEW.getCode()){
				List<EnterpriseOpRequestBuilding> opBuildings = enterpriseOpRequestBuildingProvider.queryEnterpriseOpRequestBuildings(null, Integer.MAX_VALUE,  new ListingQueryBuilderCallback() {
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
					Building building = communityProvider.findBuildingById(opBuilding.getBuildingId());
					if (null != building) {
						dto.getBuildings().add(processBuildingDTO(building));
					}
				}
			}else if(ApplyEntrySourceType.BUILDING.getCode().equals(dto.getSourceType())){
				//园区介绍处的申请
				Building building = communityProvider.findBuildingById(dto.getSourceId());
				if(null != building){
					dto.getBuildings().add(processBuildingDTO(building));
				}

				GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
				cmd2.setSourceType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
				cmd2.setSourceId(dto.getId());
				List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd2);
				dto.setFormValues(formValues);
			}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(dto.getSourceType())||
					ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(dto.getSourceType())){
				//虚位以待处的申请
				LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(dto.getSourceId());
				if(null != leasePromotion){
					Building building = communityProvider.findBuildingById(leasePromotion.getBuildingId());
					if (null != building) {
						dto.getBuildings().add(processBuildingDTO(building));
					}
				}

				GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
				cmd2.setSourceType(EntityType.LEASE_PROMOTION.getCode());
				cmd2.setSourceId(dto.getId());
				List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmd2);
				dto.setFormValues(formValues);
			}else if (ApplyEntrySourceType.MARKET_ZONE.getCode().equals(dto.getSourceType())){
				//创客入驻处的申请
				YellowPage yellowPage = yellowPageProvider.getYellowPageById(dto.getSourceId());
				if(null != yellowPage){
					if(yellowPage.getBuildingId()!=null){
						Building building = communityProvider.findBuildingById(yellowPage.getBuildingId());
						if (null != building) {
							dto.getBuildings().add(processBuildingDTO(building));
						}
					}
				}
			}
		}
		res.setEntrys(dtos);
		return res;
	}
	private BuildingDTO processBuildingDTO(Building building){
		BuildingDTO buildingDTO = ConvertHelper.convert(building, BuildingDTO.class);
		buildingDTO.setBuildingName(buildingDTO.getName());
		buildingDTO.setName(StringUtils.isEmpty(buildingDTO.getAliasName()) ? buildingDTO.getName() : buildingDTO.getAliasName());
		return buildingDTO;
	}

	@Override
	public ApplyEntryResponse applyEntry(EnterpriseApplyEntryCommand cmd) {
		ApplyEntryResponse resp = new ApplyEntryResponse();

		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);
		request.setApplyUserId(UserContext.current().getUser().getId());
		if(null != cmd.getContactPhone())
			request.setApplyContact(cmd.getContactPhone());
		
		request.setOperatorUid(request.getApplyUserId());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());


        FlowCase flowCase = dbProvider.execute(status -> {

			String issuerType = LeaseIssuerType.ORGANIZATION.getCode();
			//added by Janson
        	String projectType = EntityType.COMMUNITY.getCode();
        	
            enterpriseApplyEntryProvider.createApplyEntry(request);

			//对接表单
			if (null != cmd.getRequestFormId()) {
				addGeneralFormInfo(cmd.getRequestFormId(), cmd.getFormValues(), EntityType.ENTERPRISE_OP_REQUEST.getCode(),
						request.getId(), LeasePromotionFlag.ENABLED.getCode());
			}

			Set<Long> buildingIds = new HashSet<>();
			String buildingName = null;
            Long projectId = cmd.getCommunityId();
            EnterpriseOpRequestBuilding opRequestBuilding = new EnterpriseOpRequestBuilding();
            opRequestBuilding.setEnterpriseOpRequestsId(request.getId()); 
            opRequestBuilding.setCreatorUid(UserContext.current().getUser().getId());
            opRequestBuilding.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            opRequestBuilding.setStatus(EnterpriseOpRequestBuildingStatus.NORMAL.getCode());
            ResourceCategoryAssignment resourceCategory = null;
            //TODO : 根据情况保存地址
    		if(null != request.getContractId()){
    			//1.保存合同带的地址
    			Contract contract = contractProvider.findContractById(request.getContractId());
    			if(null == contract )
    				throw errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"can not find contract!!");
    			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(UserContext.getCurrentNamespaceId(),
    					contract.getContractNumber());
    			for(BuildingApartmentDTO buildingApartmentDTO: buildings){
    				com.everhomes.building.Building building = buildingProvider.findBuildingByName(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), buildingApartmentDTO.getBuildingName());
    				if(building !=null){
						LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(building.getId());
						buildingIds.add(leaseBuilding.getId());
    					opRequestBuilding.setBuildingId(leaseBuilding.getId());
    					resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(), 
    							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
    					enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
    				}
    			}
    			
    		}else if (cmd.getApplyType().equals(ApplyEntryApplyType.RENEW.getCode())){

				request.setSourceType(ApplyEntrySourceType.RENEW.getCode());
				List<OrganizationAddress> addresses = organizationProvider.listOrganizationAddressByOrganizationId(cmd.getEnterpriseId());
				if (!addresses.isEmpty()) {
					Address address = addressProvider.findAddressById(addresses.get(0).getAddressId());
					if (null != address) {
						Building building =communityProvider.findBuildingByCommunityIdAndName(cmd.getCommunityId(), address.getBuildingName());
						if (null != building) {
							LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(building.getId());
							opRequestBuilding.setBuildingId(leaseBuilding.getId());
							enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

							request.setBuildingId(leaseBuilding.getId());
							request.setAddressId(address.getId());
						}
					}
				}
			}else if (cmd.getSourceType().equals(ApplyEntrySourceType.MARKET_ZONE.getCode())){
    			//2. 创客空间带的地址
    			YellowPage yellowPage = yellowPageProvider.getYellowPageById(cmd.getSourceId());

				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(yellowPage.getBuildingId());

				opRequestBuilding.setBuildingId(leaseBuilding.getId());
    			resourceCategory = communityProvider.findResourceCategoryAssignment(yellowPage.getBuildingId(), 
						EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

			}else if (cmd.getSourceType().equals(ApplyEntrySourceType.BUILDING.getCode())){
    			//3. 园区介绍直接就是楼栋的地址
    			opRequestBuilding.setBuildingId(cmd.getSourceId());

				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getSourceId());

				if (leaseBuilding.getBuildingId() != 0L) {
					resourceCategory = communityProvider.findResourceCategoryAssignment(leaseBuilding.getBuildingId(),
							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				}

				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

			}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(cmd.getSourceType())){
    			//4. 虚位以待的楼栋地址
    			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getSourceId());

				issuerType = leasePromotion.getIssuerType();

    			opRequestBuilding.setBuildingId(leasePromotion.getBuildingId());

    			if (leasePromotion.getBuildingId() != 0L) {
					LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(leasePromotion.getBuildingId());
					resourceCategory = communityProvider.findResourceCategoryAssignment(leaseBuilding.getBuildingId(),
							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				}else {
					buildingName = leasePromotion.getBuildingName();
				}
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);

			}
    		if(null != resourceCategory && null!=resourceCategory.getResourceCategryId()) {
    			projectId = resourceCategory.getResourceCategryId();
    			projectType = EntityType.RESOURCE_CATEGORY.getCode();
    		}

			if (null != opRequestBuilding.getBuildingId()) {
				buildingIds.add(opRequestBuilding.getBuildingId());
			}

			FlowCase flowCase1 = null;
    		if (LeaseIssuerType.ORGANIZATION.getCode().equals(issuerType)) {
				flowCase1 = this.createFlowCase(request, projectId, projectType, buildingIds, buildingName);
                request.setFlowcaseId(flowCase1.getId());
            }
			request.setIssuerType(issuerType);
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

	private String processFlowURL(Long flowCaseId, String string, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
	}

	private int convertSourceType(String type) {
		ApplyEntrySourceType sourceType = ApplyEntrySourceType.fromType(type);

		if (null != sourceType) {
			switch (sourceType) {
				case BUILDING:
					return 1;
				case FOR_RENT:
					return 2;
				case RENEW:
					return 3;
				case MARKET_ZONE:
					return 4;
			}
		}
		return 0;
	}

    private FlowCase createFlowCase(EnterpriseOpRequest request, Long projectId, String projectType, Set<Long> buildingIds, String buildingName) {

		String tempOwnerId = String.valueOf(request.getCommunityId()) + convertSourceType(request.getSourceType());

		Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID,
				null, Long.valueOf(tempOwnerId), FlowOwnerType.LEASE_PROMOTION.getCode());

		if (null == flow) {
			flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID,
					null, request.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());
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
        flowCaseCmd.setContent(this.getBriefContent(request, buildingIds, buildingName));
        flowCaseCmd.setReferType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
        flowCaseCmd.setProjectId(projectId);
        
        //TODO better added by janson 临时办法，4.4.2 必须改动
        if(UserContext.getCurrentNamespaceId(null).equals(999983)) {
        	flowCaseCmd.setTitle("园区入驻");
        } 
		flowCaseCmd.setFlowMainId(flow.getFlowMainId());
		flowCaseCmd.setFlowVersion(flow.getFlowVersion());
		flowCaseCmd.setProjectType(projectType);
		flowCaseCmd.setProjectId(projectId);
		flowCaseCmd.setCurrentOrganizationId(request.getEnterpriseId());

		return flowService.createFlowCase(flowCaseCmd);

    }

    private String getBriefContent(EnterpriseOpRequest request, Set<Long> buildingIds, String buildingName) {
        String locale = UserContext.current().getUser().getLocale();
        Map<String, Object> map = new HashMap<>();

        if (buildingName == null) {
			StringBuilder sb = new StringBuilder();
			int n = 1;
			for (Long id: buildingIds) {

				LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(id);
				if (n == buildingIds.size()) {
					sb.append(leaseBuilding.getName());
				}else {
					sb.append(leaseBuilding.getName()).append(",");
				}
				n++;
			}
			buildingName = defaultIfNull(sb.toString(),"").toString();
		}

        map.put("buildingName", buildingName);

		GetLeasePromotionConfigCommand cmd = new GetLeasePromotionConfigCommand();
		cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		LeasePromotionConfigDTO config = getLeasePromotionConfig(cmd);

		ApplyEntrySourceType sourceType = ApplyEntrySourceType.fromType(request.getSourceType());
		String sourceTypeName = null != sourceType ? sourceType.getDescription() : "";

		byte i = -1;
		if (ApplyEntrySourceType.BUILDING == sourceType) {
			i = LeasePromotionOrder.PARK_INTRODUCE.getCode();
		}else if (ApplyEntrySourceType.FOR_RENT == sourceType) {
			i = LeasePromotionOrder.LEASE_PROMOTION.getCode();
		}
		if (null != config.getDisplayNames() ) {
			for (Integer k: config.getDisplayOrders()) {
				if (k.byteValue() ==i) {
					sourceTypeName =config.getDisplayNames().get(k - 1);
				}
			}
		}

        map.put("sourceType", defaultIfNull(sourceTypeName,""));

        return localeTemplateService.getLocaleTemplateString(ApplyEntryErrorCodes.SCOPE,
                ApplyEntryErrorCodes.FLOW_BRIEF_CONTENT_CODE, locale, map, "");
    }

    private Object defaultIfNull(Object obj, Object defaultValue) {
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
	public boolean applyRenew(EnterpriseApplyRenewCommand cmd) {
		EnterpriseOpRequest request = enterpriseApplyEntryProvider.getEnterpriseOpRequestByBuildIdAndUserId(cmd.getId(), UserContext.current().getUser().getId());
		
		if(null == request){
			throw errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"You have not applied for admission");
		}
		
		request.setApplyType(ApplyEntryApplyType.RENEW.getCode());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());
		enterpriseApplyEntryProvider.createApplyEntry(request);
        String phoneNumber = null;
        String location = null;
		//续租的时候给楼栋管理员发续租申请短信
        Building building = this.communityProvider.findBuildingById(request.getSourceId());
        if(building != null) {
        	OrganizationMember member = organizationProvider.findOrganizationMemberById(building.getManagerUid());

            if(null != member) {
                phoneNumber = member.getContactToken();
            }
            location = building.getName();
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Building not found, builingId={}, cmd={}", request.getSourceId(), cmd);
            }
        } 

  		SimpleDateFormat datetimeSF = new SimpleDateFormat("MM-dd HH:mm");
  		sendApplyEntrySmsToManager(phoneNumber, request.getApplyUserName(),request.getApplyContact(), datetimeSF.format(new Date()), 
  				location, request.getAreaSize()+"平米", request.getEnterpriseName(), request.getDescription(), request.getNamespaceId(),"续租");
		return true;
	}
	
	@Override
	public ListBuildingForRentResponse listLeasePromotions(ListBuildingForRentCommand cmd) {

		ListBuildingForRentResponse res = new ListBuildingForRentResponse();
		if (null==cmd.getRentType()) {
			cmd.setRentType(LeasePromotionType.ORDINARY.getCode());
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
			}
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
		dto.setUnit(LeasePromotionUnit.MONTH_UNIT.getDescription());

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
	}

    private void processDetailUrl(BuildingForRentDTO dto) {
		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId());

		dto.setDetailUrl(homeUrl + detailUrl);

		String buildingDetailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_BUILDING_DETAIL_URL, "");

		buildingDetailUrl = String.format(buildingDetailUrl, dto.getBuildingId());
		dto.setBuildingDetailUrl(homeUrl + buildingDetailUrl);
    }

	@Override
	public BuildingForRentDTO createLeasePromotion(CreateLeasePromotionCommand cmd, Byte adminFlag){

		if (null == cmd.getIssuerType()) {
			cmd.setIssuerType(LeaseIssuerType.ORGANIZATION.getCode());
		}
		if (null == cmd.getCustomFormFlag()) {
			cmd.setCustomFormFlag(LeasePromotionFlag.DISABLED.getCode());
		}
		if (null == cmd.getRentType()) {
			cmd.setRentType(LeasePromotionType.ORDINARY.getCode());
		}
		//兼容app业主发布招租，后台楼栋从EhLeaseBuildings查询，app业主发布招租取的楼栋信息是以前的项目管理楼栋信息
		cmd.setBuildingId(handleBuildingId(cmd.getBuildingId(), adminFlag));

		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);

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

	private Long handleBuildingId(Long buildingId, Byte adminFlag) {
		if (adminFlag == (byte)2) {
			LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(buildingId);
			if (null == leaseBuilding) {
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Status can not be modified.");
			}
			return leaseBuilding.getId();
		}
		return buildingId;
	}

	@Override
	public BuildingForRentDTO updateLeasePromotion(UpdateLeasePromotionCommand cmd, Byte adminFlag){

		cmd.setBuildingId(handleBuildingId(cmd.getBuildingId(), adminFlag));

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

			enterpriseApplyBuildingProvider.deleteLeasePromotionCommunity(leasePromotion.getId());
			if (null != cmd.getCommunityIds()) {
				cmd.getCommunityIds().forEach(m -> {
					LeasePromotionCommunity leasePromotionCommunity = new LeasePromotionCommunity();
					leasePromotionCommunity.setLeasePromotionId(leasePromotion.getId());
					leasePromotionCommunity.setCommunityId(m);
					enterpriseApplyBuildingProvider.createLeasePromotionCommunity(leasePromotionCommunity);
				});
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

		dto.setProjectDTOS(getProjectDTOs(id));

		return dto;
	}

	private List<ProjectDTO> getProjectDTOs(Long id) {
		List<Long> communityIds = enterpriseApplyBuildingProvider.listLeasePromotionCommunities(id);
		Map<Long, Community> temp = communityProvider.listCommunitiesByIds(communityIds);
		return temp.values().stream().map(r -> {
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setProjectId(r.getId());
			projectDTO.setProjectName(r.getName());

			return projectDTO;
		}).collect(Collectors.toList());
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

		if (null == cmd.getId()) {
			LOGGER.error("Invalid param id, cmd={}", cmd);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Status can not be modified.");
		}

		EnterpriseOpRequest request = enterpriseApplyEntryProvider.getApplyEntryById(cmd.getId());
		
		if(ApplyEntryStatus.RESIDED_IN.getCode() == cmd.getStatus()){
			if(ApplyEntryStatus.PROCESSING.getCode() != request.getStatus()){
				LOGGER.error("Status can not be modified. cause:data status ="+ request.getStatus());
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Status can not be modified.");
			}
		}
		
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
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		ListLeaseIssuersResponse resp = new ListLeaseIssuersResponse();

		List<LeaseIssuer> issuers = enterpriseLeaseIssuerProvider.listLeaseIssers(namespaceId, null, cmd.getKeyword(),
				cmd.getPageAnchor(), pageSize);

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
                List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIsserAddresses(r.getId(), null);
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
		Integer namespaceId = UserContext.getCurrentNamespaceId();

        dbProvider.execute((TransactionStatus status) -> {

            if (null != cmd.getEnterpriseIds()) {
                for (Long id : cmd.getEnterpriseIds()) {

                    LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.fingLeaseIssersByOrganizationId(namespaceId, id);
                    //已存在，过滤掉
                    if (null == leaseIssuer) {
                        leaseIssuer = ConvertHelper.convert(cmd, LeaseIssuer.class);
                        leaseIssuer.setNamespaceId(namespaceId);
                        leaseIssuer.setEnterpriseId(id);
                        enterpriseLeaseIssuerProvider.createLeaseIssuer(leaseIssuer);
                    }
                }
            } else {
                LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssersByContact(namespaceId, cmd.getIssuerContact());

                if (null != leaseIssuer) {
                    LOGGER.error("LeaseIssuer exist, cmd={}", cmd);
                    throw errorWith(ApplyEntryErrorCodes.SCOPE_APPLY_TYPE, ApplyEntryErrorCodes.LEASE_ISSUER_EXIST,
                            "LeaseIssuer exist.");
                }

                if (null == cmd.getAddressIds()) {
                    LOGGER.error("Invalid addressIds param, cmd={}", cmd);
                    throw errorWith(ApplyEntryErrorCodes.SCOPE_APPLY_TYPE, ApplyEntryErrorCodes.LEASE_ISSUER_EXIST,
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
        if (null == cmd.getNamespaceId())
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());

        LeasePromotionConfig config = enterpriseLeaseIssuerProvider.getLeasePromotionConfigByNamespaceId(cmd.getNamespaceId());


		if (null == config) {
			LOGGER.error("LeaseIssuerConfig not found, namespaceId={}", cmd.getNamespaceId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"LeaseIssuerConfig not found.");
		}

        String displayNameStr = config.getDisplayNameStr();
        String displayOrderStr = config.getDisplayOrderStr();
        if (null != displayNameStr) {
        	String[] names = displayNameStr.split(",");
        	String[] orders = displayOrderStr.split(",");
			LeasePromotionConfigDTO dto = ConvertHelper.convert(config, LeasePromotionConfigDTO.class);
			dto.setDisplayNames(Arrays.stream(names).collect(Collectors.toList()));
			dto.setDisplayOrders(Arrays.stream(orders).map(Integer::valueOf).collect(Collectors.toList()));

			dto.setConsultFlag((byte)1);
			return dto;
		}

		LeasePromotionConfigDTO dto = new LeasePromotionConfigDTO();
		List<LeasePromotionConfig2> configs = enterpriseLeaseIssuerProvider.listLeasePromotionConfigByNamespaceId(cmd.getNamespaceId());
        if (null != configs) {
			configs.forEach(c -> {
				String name = c.getConfigName();
				switch (name) {
					case "rentAmountFlag": dto.setRentAmountFlag(Byte.valueOf(c.getConfigValue())); break;
					case "issuingLeaseFlag": dto.setIssuingLeaseFlag(Byte.valueOf(c.getConfigValue())); break;
					case "issuerManageFlag": dto.setIssuerManageFlag(Byte.valueOf(c.getConfigValue())); break;
					case "parkIntroduceFlag": dto.setParkIndroduceFlag(Byte.valueOf(c.getConfigValue())); break;
					case "renewFlag": dto.setRenewFlag(Byte.valueOf(c.getConfigValue())); break;
					case "areaSearchFlag": dto.setAreaSearchFlag(Byte.valueOf(c.getConfigValue())); break;
					case "consultFlag": dto.setConsultFlag(Byte.valueOf(c.getConfigValue())); break;
//					case "displayNameStr": dto.setDisplayNameStr(c.getConfigValue()); break;
//					case "displayOrderStr": dto.setDisplayOrderStr(c.getConfigValue()); break;
					default: break;
				}
			});
		}

        return dto;

    }

    @Override
    public CheckIsLeaseIssuerDTO checkIsLeaseIssuer(CheckIsLeaseIssuerCommand cmd) {
        CheckIsLeaseIssuerDTO dto = new CheckIsLeaseIssuerDTO();
		Long organizationId = cmd.getOrganizationId();
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		dto.setFlag(LeasePromotionFlag.DISABLED.getCode());

		//先检查是不是招租发行人
		if (null != enterpriseLeaseIssuerProvider.findLeaseIssersByContact(namespaceId, identifier.getIdentifierToken())) {
			dto.setFlag(LeasePromotionFlag.ENABLED.getCode());
		}

		if (null != organizationId) {
			//先检查是不是招租发行公司
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssersByOrganizationId(namespaceId, organizationId)) {
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

        ListLeaseIssuerBuildingsResponse response = new ListLeaseIssuerBuildingsResponse();
		Long organizationId = cmd.getOrganizationId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        User user = UserContext.current().getUser();
        UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		List<BuildingDTO> buildingDTOs = new ArrayList<>();

		//先查询业主
		LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssersByContact(namespaceId, identifier.getIdentifierToken());
		if (null != leaseIssuer) {
			List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIsserBuildings(leaseIssuer.getId());

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
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssersByOrganizationId(namespaceId, organizationId)) {
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
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		Long organizationId = cmd.getOrganizationId();

		//先查询业主
		LeaseIssuer leaseIssuer = enterpriseLeaseIssuerProvider.findLeaseIssersByContact(namespaceId, identifier.getIdentifierToken());
		if (null != leaseIssuer) {
			List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIsserAddresses(leaseIssuer.getId(), cmd.getBuildingId());

			dtos.addAll(addresses.stream().map(a -> {
				Address address = addressProvider.findAddressById(a.getAddressId());
				return ConvertHelper.convert(address, AddressDTO.class);
			}).collect(Collectors.toList()));
		}

		if (null != organizationId)  {
			if (null != enterpriseLeaseIssuerProvider.fingLeaseIssersByOrganizationId(namespaceId, organizationId)) {
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

		LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
				cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSourceType());

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
				enterpriseApplyEntryProvider.deleteLeaseRequestForm(request);
			}
		}

	}

	@Override
	public LeaseFormRequestDTO getLeasePromotionRequestForm(GetLeasePromotionRequestFormCommand cmd) {

		LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
				cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSourceType());

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
		EnterpriseApplyBuildingProvider enterpriseApplyBuildingProvider) {
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
	}
}
