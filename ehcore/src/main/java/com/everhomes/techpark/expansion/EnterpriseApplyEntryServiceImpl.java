package com.everhomes.techpark.expansion;

import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.enterprise.EnterpriseCommunityMap;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapStatus;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class EnterpriseApplyEntryServiceImpl implements EnterpriseApplyEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryServiceImpl.class);

	@Autowired
	private SmsProvider smsProvider;

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
    private FlowService flowService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private LocaleStringService localeStringService;

    @Override
	public ListEnterpriseDetailResponse listEnterpriseDetails(
			ListEnterpriseDetailCommand cmd) {
		
		ListEnterpriseDetailResponse res = new ListEnterpriseDetailResponse();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<Long> enterpriseIds = new ArrayList<Long>();
		ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		//根据楼栋名称查询
		if(!StringUtils.isEmpty(cmd.getBuildingName())){
			List<EnterpriseAddress> enterpriseAddresses = enterpriseApplyEntryProvider.listBuildingEnterprisesByBuildingName(cmd.getBuildingName(), locator, pageSize+1);
			for (EnterpriseAddress enterpriseAddress : enterpriseAddresses) {
				enterpriseIds.add(enterpriseAddress.getEnterpriseId());
			}
		}else{
			List<EnterpriseCommunityMap> enterpriseMaps = this.enterpriseProvider.queryEnterpriseMapByCommunityId(locator
		                , cmd.getCommunityId(), pageSize+1, new ListingQueryBuilderCallback() {

		            @Override
		            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
		                    SelectQuery<? extends Record> query) {
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(cmd.getCommunityId()));
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.ne(EnterpriseCommunityMapStatus.INACTIVE.getCode()));
		                return query;
		            }
		      });    
			
			for (EnterpriseCommunityMap enterpriseCommunityMap : enterpriseMaps) {
				enterpriseIds.add(enterpriseCommunityMap.getMemberId());
			}
		}
		
		List<EnterpriseDetailDTO> dtos = new ArrayList<EnterpriseDetailDTO>();
		
		if(null != locator.getAnchor())
			enterpriseIds.remove(enterpriseIds.size()-1);
		
		for (Long enterpriseId : enterpriseIds) {
			EnterpriseDetail enterpriseDetail = this.getEnterpriseDetailByEnterpriseId(enterpriseId);
			if(null != enterpriseDetail){
				EnterpriseDetailDTO dto = toEnterpriseDetailDTO(enterpriseDetail);
				dto.setContactPhone(enterpriseDetail.getContact());
				dtos.add(dto);
			}
				
		}
		
		res.setNextPageAnchor(locator.getAnchor());
		res.setDetails(dtos);
		return res;
	}

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
	public ListEnterpriseApplyEntryResponse listApplyEntrys(
			ListEnterpriseApplyEntryCommand cmd) {
		
		ListEnterpriseApplyEntryResponse res = new ListEnterpriseApplyEntryResponse();
		
		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
		List<EnterpriseOpRequest> enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(request, locator, pageSize);
		
		res.setNextPageAnchor(locator.getAnchor());
		for (EnterpriseOpRequest enterpriseOpRequest : enterpriseOpRequests) {
			if(ApplyEntrySourceType.BUILDING.getCode().equals(enterpriseOpRequest.getSourceType())){
				Building building = communityProvider.findBuildingById(enterpriseOpRequest.getSourceId());
				if(null != building)enterpriseOpRequest.setSourceName(building.getName());
			}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(enterpriseOpRequest.getSourceType())||
					ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(enterpriseOpRequest.getSourceType())){
				LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(enterpriseOpRequest.getSourceId());
				if(null != leasePromotion)enterpriseOpRequest.setSourceName(leasePromotion.getSubject());
			}
		}
		
		List<EnterpriseApplyEntryDTO> dtos = enterpriseOpRequests.stream().map((c) ->{
			return ConvertHelper.convert(c, EnterpriseApplyEntryDTO.class);
		}).collect(Collectors.toList());
		
		res.setEntrys(dtos);
		return res;
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

        boolean createFlowCaseSuccess = dbProvider.execute(status -> {
            enterpriseApplyEntryProvider.createApplyEntry(request);
            return this.createFlowCase(request);
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
        if (createFlowCaseSuccess) {
        	//TODO: 组装resp
            return resp;
        }

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
		return resp;
	}

    private boolean createFlowCase(EnterpriseOpRequest request) {
        Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ExpansionConst.MODULE_ID, null, request.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());
        if (flow != null) {
            CreateFlowCaseCommand flowCaseCmd = new CreateFlowCaseCommand();
            flowCaseCmd.setApplyUserId(request.getApplyUserId());
            flowCaseCmd.setFlowMainId(flow.getFlowMainId());
            flowCaseCmd.setFlowVersion(flow.getFlowVersion());
            flowCaseCmd.setReferId(request.getId());
            flowCaseCmd.setReferType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
            // flowCase摘要内容
            flowCaseCmd.setContent(this.getBriefContent(request));

            flowService.createFlowCase(flowCaseCmd);
            return true;
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("There is no expansion workflow enabled for ownerId: {}", request.getCommunityId());
            }
            return false;
        }
    }

    private String getBriefContent(EnterpriseOpRequest request) {
        String locale = UserContext.current().getUser().getLocale();
        Map<String, Object> map = new HashMap<>();
        String applyType = localeStringService.getLocalizedString(ExpansionLocalStringCode.SCOPE_APPLY_TYPE, request.getApplyType() + "", locale, "");
        map.put("applyType", applyType);
        map.put("areaSize", request.getAreaSize());

        return localeTemplateService.getLocaleTemplateString(ExpansionLocalStringCode.SCOPE,
                ExpansionLocalStringCode.FLOW_BRIEF_CONTENT_CODE, locale, map, "");
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
	public ListBuildingForRentResponse listLeasePromotions(
			ListBuildingForRentCommand cmd) {
		ListBuildingForRentResponse res = new ListBuildingForRentResponse();
		if (null==cmd.getRentType())
			cmd.setRentType( LeasePromotionType.ORDINARY.getCode());
		LeasePromotion lease = ConvertHelper.convert(cmd, LeasePromotion.class);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
	    
		List<LeasePromotion> leasePromotions = enterpriseApplyEntryProvider.listLeasePromotions(lease, locator, pageSize);
		
		res.setNextPageAnchor(locator.getAnchor());
		
		for (LeasePromotion leasePromotion : leasePromotions) {
			leasePromotion.setPosterUrl(contentServerService.parserUri(leasePromotion.getPosterUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			List<LeasePromotionAttachment> attachments = leasePromotion.getAttachments();
			if(null != attachments){
				for (LeasePromotionAttachment leasePromotionAttachment : attachments) {
					leasePromotionAttachment.setContentUrl(contentServerService.parserUri(leasePromotionAttachment.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				}
			}
			
			Building building = communityProvider.findBuildingById(leasePromotion.getBuildingId());
			if(null != building){
				leasePromotion.setBuildingName(building.getName());
				leasePromotion.setAddress(building.getAddress());
				leasePromotion.setLatitude(building.getLatitude());
				leasePromotion.setLongitude(building.getLongitude());
			}
				
		}
		
		List<BuildingForRentDTO> dtos = leasePromotions.stream().map((c) ->{
			return ConvertHelper.convert(c, BuildingForRentDTO.class);
		}).collect(Collectors.toList());
		
		res.setDtos(dtos);
		return res;
	}

	@Override
	public boolean createLeasePromotion(CreateLeasePromotionCommand cmd){
		LeasePromotion leasePromotion = ConvertHelper.convert(cmd, LeasePromotion.class);
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
	
}
