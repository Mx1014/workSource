package com.everhomes.techpark.expansion;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.organization.*;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.UserIdentifier;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
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
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
import com.everhomes.yellowPage.YellowPage;
import com.everhomes.yellowPage.YellowPageProvider;

@Component
public class EnterpriseApplyEntryServiceImpl implements EnterpriseApplyEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryServiceImpl.class);

	@Autowired
	private SmsProvider smsProvider;

	@Autowired
	private ContractProvider contractProvider;

	@Autowired
	private BuildingProvider buildingProvider;
	
	@Autowired
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	@Autowired
	private EnterpriseOpRequestBuildingProvider enterpriseOpRequestBuildingProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private EnterpriseProvider enterpriseProvider;
	
	@Autowired
	private GroupProvider groupProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private YellowPageProvider yellowPageProvider;
	
    @Autowired
    private FlowService flowService;

    @Autowired
    private DbProvider dbProvider;

	@Autowired
	private OrganizationService organizationService;
	
    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private LocaleStringService localeStringService;

	@Autowired
	private EnterpriseLeaseIssuerProvider enterpriseLeaseIssuerProvider;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private CommunityService communityService;

	@Override
	public GetEnterpriseDetailByIdResponse getEnterpriseDetailById(
			GetEnterpriseDetailByIdCommand cmd) {
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
//		if(request.getApplyType() != null &&ApplyEntryApplyType.MAKER_ZONE.getCode() == request.getApplyType().byteValue()){
//			request.setApplyType(null);
//			request.setSourceType(ApplyEntrySourceType.MARKET_ZONE.getCode());
//		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
		List<EnterpriseOpRequest> enterpriseOpRequests = null;
		//增加了判断buildingId
		if(null == cmd.getBuildingId())
			enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, pageSize);
		else{
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
		if(null == enterpriseOpRequests)
			return res;
		res.setNextPageAnchor(locator.getAnchor());

		List<EnterpriseApplyEntryDTO> dtos = enterpriseOpRequests.stream().map((c) ->{
			EnterpriseApplyEntryDTO dto = ConvertHelper.convert(c, EnterpriseApplyEntryDTO.class);
			//对于有合同的(一定是续租)
			if(null != c.getContractId()){
				Contract contract = contractProvider.findContractById(c.getContractId());
				if(null != contract)
					dto.setContract(organizationService.processContract(contract));
			}
			return dto;
		}).collect(Collectors.toList());
		for (EnterpriseApplyEntryDTO dto : dtos) {
			dto.setBuildings(new ArrayList<BuildingDTO>() );
			//对于不同的类型有不同的sourceName 和 楼栋
			if(ApplyEntryApplyType.fromType(dto.getApplyType()).equals(ApplyEntryApplyType.RENEW)){
				//续租的搜
				dto.setSourceName("续租");
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
					dto.getBuildings().add(proessBuildingDTO(building));
				}
			}else if(ApplyEntrySourceType.BUILDING.getCode().equals(dto.getSourceType())){
				//园区介绍处的申请，申请来源=楼栋名称 园区介绍处的申请，楼栋=楼栋名称
				Building building = communityProvider.findBuildingById(dto.getSourceId());
				if(null != building){
					dto.setSourceName(building.getName());
					dto.getBuildings().add(proessBuildingDTO(building));
				}
			}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(dto.getSourceType())||
					ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(dto.getSourceType())){
				//虚位以待处的申请，申请来源=招租标题 虚位以待处的申请，楼栋=招租办公室所在楼栋
				LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(dto.getSourceId());
				if(null != leasePromotion){
					dto.setSourceName(leasePromotion.getSubject());
					Building building = communityProvider.findBuildingById(leasePromotion.getBuildingId());
					dto.getBuildings().add(proessBuildingDTO(building));
				}
			}else if (ApplyEntrySourceType.MARKET_ZONE.getCode().equals(dto.getSourceType())){
				//创客入驻处的申请，申请来源=“创客申请” 创客入驻处的申请，楼栋=创客空间所在的楼栋
				YellowPage yellowPage = yellowPageProvider.getYellowPageById(dto.getSourceId());
				if(null != yellowPage){
					dto.setSourceName("创客申请");
					if(yellowPage.getBuildingId()!=null){
						Building building = communityProvider.findBuildingById(yellowPage.getBuildingId());
						dto.getBuildings().add(proessBuildingDTO(building));
					}
				}
			}
		}
		res.setEntrys(dtos);
		return res;
	}
	private BuildingDTO proessBuildingDTO(Building building){
		BuildingDTO buildingDTO = ConvertHelper.convert(building, BuildingDTO.class); 
		buildingDTO.setBuildingName(buildingDTO.getName());
		buildingDTO.setName(org.springframework.util.StringUtils.isEmpty(buildingDTO.getAliasName()) ? buildingDTO.getName() : buildingDTO.getAliasName());
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
        	//added by Janson
        	String projectType = EntityType.COMMUNITY.getCode();
        	
            enterpriseApplyEntryProvider.createApplyEntry(request);
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
    					opRequestBuilding.setBuildingId(building.getId());
    					resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(), 
    							EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
    					enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
    				}
    			}
    			
    		}else if (cmd.getApplyType().equals(ApplyEntryApplyType.RENEW.getCode())){
    			
    		}else if (cmd.getSourceType().equals(ApplyEntrySourceType.MARKET_ZONE.getCode())){
    			//2. 创客空间带的地址
    			YellowPage yellowPage = yellowPageProvider.getYellowPageById(cmd.getSourceId());
    			opRequestBuilding.setBuildingId(yellowPage.getBuildingId());
    			resourceCategory = communityProvider.findResourceCategoryAssignment(yellowPage.getBuildingId(), 
						EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
    		}else if (cmd.getSourceType().equals(ApplyEntrySourceType.BUILDING.getCode())){
    			//3. 园区介绍直接就是楼栋的地址
    			opRequestBuilding.setBuildingId(cmd.getSourceId());
    			resourceCategory = communityProvider.findResourceCategoryAssignment(cmd.getSourceId(), 
						EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
    		}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(cmd.getSourceType())||
					ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(cmd.getSourceType())){
    			//4. 虚位以待的楼栋地址
    			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getSourceId());
    			opRequestBuilding.setBuildingId(leasePromotion.getBuildingId());
    			resourceCategory = communityProvider.findResourceCategoryAssignment(leasePromotion.getBuildingId(), 
						EntityType.BUILDING.getCode(),UserContext.getCurrentNamespaceId());
				enterpriseOpRequestBuildingProvider.createEnterpriseOpRequestBuilding(opRequestBuilding);
    		}
    		if(null != resourceCategory && null!=resourceCategory.getResourceCategryId()) {
    			projectId = resourceCategory.getResourceCategryId();
    			projectType = EntityType.RESOURCE_CATEGORY.getCode();
    		}

            return this.createFlowCase(request,projectId, projectType);
        });

        // 查找联系人手机号的逻辑不正确，因为参数中的source id有可能是buildingId，也有可能是leasePromotionId，需要根据source type来区分  by lqs 20160813
		// LeasePromotion lp = this.enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getSourceId());
		// LeasePromotionType rentType = LeasePromotionType.fromType(lp.getRentType());
		// String phoneNumber = null;
		// if(rentType != null) {
		// 	switch(rentType) {
		// 	case ORDINARY:
		// 		phoneNumber = lp.getContactPhone();
		// 		break;
		// 	case BUILDING:
		// 		Building building = this.communityProvider.findBuildingById(lp.getBuildingId());
		// 		UserIdentifier identifier = this.userProvider.findClaimedIdentifierByOwnerAndType(building.getManagerUid(), IdentifierType.MOBILE.getCode());
		// 		if(null != identifier)
		// 			phoneNumber = identifier.getIdentifierToken();
		// 		break;
		// 	default:
		// 		break;
        //
		// 	}
		// }
		// SimpleDateFormat datetimeSF = new SimpleDateFormat("MM-dd HH:mm");
        // sendApplyEntrySmsToManager(phoneNumber, cmd.getApplyUserName(),cmd.getContactPhone(), datetimeSF.format(new Date()),
        //         lp.getRentPosition(), cmd.getAreaSize()+"平米", cmd.getEnterpriseName(), cmd.getDescription(), cmd.getNamespaceId());

        // 1.如果创建flowCase成功, 则不在这里发送短信, 移到工作流中配置
        // 2.如果创建flowCase不成功, 说明没有配置使用工作流, 则保持原来的发短信功能不变   add by xq.tian  2016/12/22


        // 根据apply type来区分
        String phoneNumber = null;
        String location = null;
        ApplyEntryApplyType applyType = ApplyEntryApplyType.fromType(cmd.getApplyType());
        if(applyType != null && cmd.getSourceId() != null) {
			//modify by wuhan 2016-8-17 园区入驻2.2全部变成了给管理员发短信(其实是没有for_rent的类型了)
			  Building building = this.communityProvider.findBuildingById(cmd.getSourceId());
			  if(building != null) {
			  	OrganizationMember member = organizationProvider.findOrganizationMemberById(building.getManagerUid());

			      if(null != member) {
			          phoneNumber = member.getContactToken();
			      }
			      location = building.getName();
			  } else {
			      if(LOGGER.isWarnEnabled()) {
			          LOGGER.warn("Building not found, builingId={}, cmd={}", cmd.getSourceId(), cmd);
			      }
			  }

	  		SimpleDateFormat datetimeSF = new SimpleDateFormat("MM-dd HH:mm");

            switch(applyType) {
                case APPLY:
                    sendApplyEntrySmsToManager(phoneNumber, cmd.getApplyUserName(),cmd.getContactPhone(), datetimeSF.format(new Date()),
                            location, cmd.getAreaSize()+"平米", cmd.getEnterpriseName(), cmd.getDescription(), cmd.getNamespaceId(),"看楼");
                    break;
                case RENEW:
                    sendApplyEntrySmsToManager(phoneNumber, cmd.getApplyUserName(),cmd.getContactPhone(), datetimeSF.format(new Date()),
                            location, cmd.getAreaSize()+"平米", cmd.getEnterpriseName(), cmd.getDescription(), cmd.getNamespaceId(),"续租");
                    break;
                case EXPANSION:
                    sendApplyEntrySmsToManager(phoneNumber, cmd.getApplyUserName(),cmd.getContactPhone(), datetimeSF.format(new Date()),
                            location, cmd.getAreaSize()+"平米", cmd.getEnterpriseName(), cmd.getDescription(), cmd.getNamespaceId(),"看楼");
                    break;
                default:
                    if(LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Apply entry source type not supported, applyType={}, cmd={}", applyType, cmd);
                    }
                    break;
            }
        }
        
        if (flowCase != null ) {
        	//TODO: 组装resp
        	String url = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
        	resp.setUrl(url);
        }
		return resp;
	}

	private String processFlowURL(Long flowCaseId, String string, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
		  
		
	}
    private FlowCase createFlowCase(EnterpriseOpRequest request, Long projectId, String projectType) {
        Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID, null, request.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());

        CreateFlowCaseCommand flowCaseCmd = new CreateFlowCaseCommand();
        flowCaseCmd.setApplyUserId(request.getApplyUserId());
        flowCaseCmd.setReferId(request.getId());
        // flowCase摘要内容
        flowCaseCmd.setContent(this.getBriefContent(request));
        flowCaseCmd.setReferType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
        flowCaseCmd.setProjectId(projectId);
        if (flow != null) {
            flowCaseCmd.setFlowMainId(flow.getFlowMainId());
            flowCaseCmd.setFlowVersion(flow.getFlowVersion());
            flowCaseCmd.setProjectType(projectType);
            flowCaseCmd.setProjectId(projectId);

            return flowService.createFlowCase(flowCaseCmd);
        } else {
        	GeneralModuleInfo gm = new GeneralModuleInfo();
        	gm.setOrganizationId(request.getEnterpriseId());
        	gm.setProjectType(projectType);
        	gm.setProjectId(projectId);
        	gm.setNamespaceId(UserContext.getCurrentNamespaceId());
        	gm.setModuleId(ExpansionConst.MODULE_ID);
			gm.setOwnerId(request.getCommunityId());
			gm.setOwnerType(FlowOwnerType.COMMUNITY.getCode());
			return flowService.createDumpFlowCase(gm, flowCaseCmd);
        }
    }

    private String getBriefContent(EnterpriseOpRequest request) {
        String locale = UserContext.current().getUser().getLocale();
        Map<String, Object> map = new HashMap<>();
        String applyType = localeStringService.getLocalizedString(ExpansionLocalStringCode.SCOPE_APPLY_TYPE, request.getApplyType() + "", locale, "");
        map.put("applyType", defaultIfNull(applyType,""));
        map.put("areaSize", defaultIfNull(request.getAreaSize(),""));

        return localeTemplateService.getLocaleTemplateString(ExpansionLocalStringCode.SCOPE,
                ExpansionLocalStringCode.FLOW_BRIEF_CONTENT_CODE, locale, map, "");
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
		if (null==cmd.getRentType())
			cmd.setRentType( LeasePromotionType.ORDINARY.getCode());
		LeasePromotion lease = ConvertHelper.convert(cmd, LeasePromotion.class);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
	    
		List<LeasePromotion> leasePromotions = enterpriseApplyEntryProvider.listLeasePromotions(lease, locator, pageSize);
		
		res.setNextPageAnchor(locator.getAnchor());
		
		List<BuildingForRentDTO> dtos = leasePromotions.stream().map((c) ->{
            BuildingForRentDTO dto = ConvertHelper.convert(c, BuildingForRentDTO.class);

            Address address = addressProvider.findAddressById(c.getAddressId());
            if (null != address) {
                dto.setApartmentName(address.getApartmentName());
            }

            Building building = communityProvider.findBuildingById(c.getBuildingId());
            if(null != building){
                dto.setBuildingName(building.getName());
                dto.setAddress(building.getAddress());
                dto.setLatitude(building.getLatitude());
                dto.setLongitude(building.getLongitude());
            }

            dto.setPosterUrl(contentServerService.parserUri(c.getPosterUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
            List<LeasePromotionAttachment> attachments = c.getAttachments();
            if(null != attachments){
                for (LeasePromotionAttachment leasePromotionAttachment : attachments) {
                    leasePromotionAttachment.setContentUrl(contentServerService.parserUri(leasePromotionAttachment.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
                }
            }

            return dto;
		}).collect(Collectors.toList());
		
		res.setDtos(dtos);
		return res;
	}

	@Override
	public boolean createLeasePromotion(CreateLeasePromotionCommand cmd){
		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);
        if (null != cmd.getEnterTime())
		    leasePromotion.setEnterTime(new Timestamp(cmd.getEnterTime()));
		leasePromotion.setCreateUid(UserContext.current().getUser().getId());
		leasePromotion.setStatus(LeasePromotionStatus.RENTING.getCode());
		if (null==cmd.getRentType())
			cmd.setRentType(LeasePromotionType.ORDINARY.getCode());
		leasePromotion.setRentType(cmd.getRentType());
		leasePromotion = enterpriseApplyEntryProvider.createLeasePromotion(leasePromotion);
		
		List<BuildingForRentAttachmentDTO> attachmentDTOs= cmd.getAttachments();
		
		if(StringUtils.isEmpty(attachmentDTOs)){
			return true;
		}
		
		
		/**
		 * 重新添加
		 */
		for (BuildingForRentAttachmentDTO buildingForRentAttachmentDTO : attachmentDTOs) {
			LeasePromotionAttachment attachment = ConvertHelper.convert(buildingForRentAttachmentDTO, LeasePromotionAttachment.class);
			attachment.setLeaseId(leasePromotion.getId());
			attachment.setCreatorUid(leasePromotion.getCreateUid());
			enterpriseApplyEntryProvider.addPromotionAttachment(attachment);
		}
		return true;
	}

	@Override
	public boolean updateLeasePromotion(UpdateLeasePromotionCommand cmd){
		
		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);
		LeasePromotion lease = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());
		
		leasePromotion.setEnterTime(new Timestamp(cmd.getEnterTime()));
		leasePromotion.setStatus(LeasePromotionStatus.RENTING.getCode());
		leasePromotion.setCreateTime(lease.getCreateTime());
		leasePromotion.setCreateUid(lease.getCreateUid());
		enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);
		
		List<BuildingForRentAttachmentDTO> attachmentDTOs= cmd.getAttachments();
		
		if(StringUtils.isEmpty(attachmentDTOs)){
			return true;
		}
		
		/**
		 * 先删除全部图片
		 */
		enterpriseApplyEntryProvider.deleteLeasePromotionAttachment(leasePromotion.getId());
		
		/**
		 * 重新添加
		 */
		for (BuildingForRentAttachmentDTO buildingForRentAttachmentDTO : attachmentDTOs) {
			LeasePromotionAttachment attachment = ConvertHelper.convert(buildingForRentAttachmentDTO, LeasePromotionAttachment.class);
			attachment.setLeaseId(leasePromotion.getId());
			attachment.setCreatorUid(UserContext.current().getUser().getId());
			enterpriseApplyEntryProvider.addPromotionAttachment(attachment);
		}
		
		return true;
	}
	
	@Override
	public BuildingForRentDTO findLeasePromotionById(Long id){
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(id);
		leasePromotion.setPosterUrl(contentServerService.parserUri(leasePromotion.getPosterUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		List<LeasePromotionAttachment> attachments = leasePromotion.getAttachments();
		if(null != attachments){
			for (LeasePromotionAttachment leasePromotionAttachment : attachments) {
				leasePromotionAttachment.setContentUrl(contentServerService.parserUri(leasePromotionAttachment.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			}
		}
		return ConvertHelper.convert(leasePromotion, BuildingForRentDTO.class);
	}
	
	@Override
	public boolean updateLeasePromotionStatus(UpdateLeasePromotionStatusCommand cmd){
		LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(cmd.getId());
		
		if(LeasePromotionStatus.RENTAL.getCode() == cmd.getStatus()){
			if(LeasePromotionStatus.RENTING.getCode() != leasePromotion.getStatus()){
				LOGGER.error("Status can not be modified. cause:data status ="+ leasePromotion.getStatus());
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Status can not be modified.");
			}
		}
		
		return enterpriseApplyEntryProvider.updateLeasePromotionStatus(cmd.getId(), cmd.getStatus());
		
	}
	
	@Override
	public boolean updateApplyEntryStatus(UpdateApplyEntryStatusCommand cmd){
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
		return enterpriseApplyEntryProvider.deleteLeasePromotion(cmd.getId());
	}

	@Override
	public ListLeaseIssuersResponse listLeaseIssuers(ListLeaseIssuersCommand cmd) {
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		ListLeaseIssuersResponse resp = new ListLeaseIssuersResponse();

		List<LeaseIssuer> issuers = enterpriseLeaseIssuerProvider.listLeaseIssers(namespaceId, cmd.getKeyword(),
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
                List<LeaseIssuerAddress> addresses = enterpriseLeaseIssuerProvider.listLeaseIsserAddresses(r.getId());
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
        leaseIssuer.setStatus(LeaseIssuerStatus.INACTIVE.getCode());
        enterpriseLeaseIssuerProvider.updateLeaseIssuer(leaseIssuer);
    }

    @Override
    public void addLeaseIssuer(AddLeaseIssuerCommand cmd) {

        if (null == cmd.getCommunityId()) {
            LOGGER.error("Invalid communityId param, cmd={}", cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId param.");
        }

        if (null != cmd.getEnterpriseIds()) {
            for (Long id: cmd.getEnterpriseIds()) {
                LeaseIssuer leaseIssuer = ConvertHelper.convert(cmd, LeaseIssuer.class);
                leaseIssuer.setNamespaceId(UserContext.getCurrentNamespaceId());
                leaseIssuer.setEnterpriseId(id);
                enterpriseLeaseIssuerProvider.createLeaseIssuer(leaseIssuer);
            }
        }else {
            LeaseIssuer leaseIssuer = ConvertHelper.convert(cmd, LeaseIssuer.class);
            leaseIssuer.setNamespaceId(UserContext.getCurrentNamespaceId());
            //TODO:门牌地址
            enterpriseLeaseIssuerProvider.createLeaseIssuer(leaseIssuer);

            if (null != cmd.getAddressIds()) {
                for (Long id: cmd.getAddressIds()) {
                    LeaseIssuerAddress leaseIssuerAddress = new LeaseIssuerAddress();
                    leaseIssuerAddress.setAddressId(id);
                    leaseIssuerAddress.setLeaseIssuerId(leaseIssuer.getId());
                    createLeaseIssuerAddress(leaseIssuerAddress);
                }
            }
        }

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

        return ConvertHelper.convert(config, LeasePromotionConfigDTO.class);

    }

    @Override
    public CheckIsLeaseIssuerDTO checkIsLeaseIssuer() {
        CheckIsLeaseIssuerDTO dto = new CheckIsLeaseIssuerDTO();

        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

        List<LeaseIssuer> issuers = enterpriseLeaseIssuerProvider.listLeaseIssers(namespaceId, identifier.getIdentifierToken(),
                null, null);
        dto.setFlag(LeasePromotionFlag.DISABLED.getCode());
        if (0 != issuers.size()) {
            dto.setFlag(LeasePromotionFlag.ENABLED.getCode());
        }

        return dto;
    }

    @Override
    public ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd) {
        ListBuildingCommandResponse buildings = communityService.listBuildings(cmd);
        return null;
    }
}
