package com.everhomes.yellowPage;

import com.everhomes.activity.ActivityAttachment;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.activity.ActivityAttachmentDTO;
import com.everhomes.rest.activity.ListActivityAttachmentsResponse;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.yellowPage.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class YellowPageServiceImpl implements YellowPageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageServiceImpl.class);

	@Autowired
	private HotlineService hotlineService;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
    @Autowired
    private YellowPageProvider yellowPageProvider;
    
    @Autowired
    private CommunityProvider communityProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private CategoryProvider categoryProvider;
	
	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private AuditLogProvider auditLogProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private UserActivityProvider userActivityProvider;

	private void populateYellowPage(YellowPage yellowPage) { 
		this.yellowPageProvider.populateYellowPagesAttachment(yellowPage);
		 
		populateYellowPageUrl(yellowPage);
		populateYellowPageAttachements(yellowPage,yellowPage.getAttachments());
	 
		
	}

	private void populateYellowPageUrl(YellowPage yp) { 
		 
		 String posterUri = yp.getPosterUri();
	        if(posterUri != null && posterUri.length() > 0) {
	            try{
	                String posterUrl = contentServerService.parserUri(yp.getPosterUri(), EntityType.USER.getCode(),UserContext.current().getUser().getId());
	                yp.setPosterUrl(posterUrl);
	            }catch(Exception e){
	                LOGGER.error("Failed to parse poster uri of YellowPage, YellowPage =" + yp, e);
	            }
	        }	 
	}
	 private void populateYellowPageAttachements(YellowPage yellowPage, List<YellowPageAttachment> attachmentList) {
		 
		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The YellowPage attachment list is empty, YellowPageId=" + yellowPage.getId());
	            }
		 } else {
	            for(YellowPageAttachment attachment : attachmentList) {
	                populateYellowPageAttachement(yellowPage, attachment);
	            }
		 }
	 }
	 
	 private void populateYellowPageAttachement(YellowPage yellowPage, YellowPageAttachment attachment) {
        
		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The YellowPage attachment is null, YellowPageId=" + yellowPage.getId());
			 }
		 } else {
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(),UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, YellowPageId=" + yellowPage.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				 }
			 }
		 }
	 }

	@Override
	public YellowPageDTO getYellowPageDetail(GetYellowPageDetailCommand cmd) { 
		YellowPageDTO response = null;
		if(cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceEnterpriseDetailCommand command = ConvertHelper.convert(cmd, GetServiceAllianceEnterpriseDetailCommand.class);
			ServiceAllianceDTO sa = getServiceAllianceEnterpriseDetail(command);
					
			response = ConvertHelper.convert(sa,YellowPageDTO.class);
			
			
		} else {
		 
			YellowPage yellowPage = this.yellowPageProvider.getYellowPageById(cmd.getId());
		
			populateYellowPage(yellowPage);
			
			if(cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())){
				ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
				response = ConvertHelper.convert(serviceAlliance,YellowPageDTO.class);
			} 
			else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())){
				MakerZone makerZone =  ConvertHelper.convert(yellowPage ,MakerZone.class);
				response = ConvertHelper.convert(makerZone,YellowPageDTO.class);
			} 
			else{
					response =  ConvertHelper.convert(yellowPage,YellowPageDTO.class);
			}
		}
		return response;
	}
 
	@Override
	public YellowPageListResponse getYellowPageList(
			GetYellowPageListCommand cmd) { 
		YellowPageListResponse response = new YellowPageListResponse();
		response.setYellowPages(new ArrayList<YellowPageDTO>());
		if(cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceEnterpriseListCommand command = ConvertHelper.convert(cmd, GetServiceAllianceEnterpriseListCommand.class);
			command.setPageSize(AppConstants.PAGINATION_MAX_SIZE);
			command.setNextPageAnchor(cmd.getPageAnchor());
			if(!StringUtils.isEmpty(cmd.getServiceType())) {
				ServiceAllianceCategories category = yellowPageProvider.findCategoryByName(UserContext.getCurrentNamespaceId(), cmd.getServiceType());
				command.setCategoryId(category.getId());
			}
			ServiceAllianceListResponse res = this.getServiceAllianceEnterpriseList(command);
			List<ServiceAllianceDTO> dtos = res.getDtos();
			if(dtos != null && dtos.size() > 0) {
				for(ServiceAllianceDTO dto : dtos) {
					YellowPageDTO ypDto = ConvertHelper.convert(dto,YellowPageDTO.class);
					if(null != ypDto) 
						response.getYellowPages().add(ypDto);
				}
			}
			
		}else if(cmd.getType() != null && cmd.getType().equals(YellowPageType.PARKENTSERVICEHOTLINE.getCode())){
			GetHotlineListCommand cmd2 = ConvertHelper.convert(cmd, GetHotlineListCommand.class);
			cmd2.setServiceType(ServiceType.SERVICE_HOTLINE.getCode());
			GetHotlineListResponse resp2 = this.hotlineService.getHotlineList(cmd2);
			if(resp2.getHotlines()!=null)
				resp2.getHotlines().forEach(r->{
					YellowPageDTO dto = ConvertHelper.convert(r, YellowPageDTO.class);
					response.getYellowPages().add(dto);
				});
		}
		else {
			//做兼容
			if(null != cmd.getCommunityId()){
				cmd.setOwnerId(cmd.getCommunityId());
			}else if(null != cmd.getOwnerId()){
				List<Community> communities = communityProvider.listCommunitiesByNamespaceId(cmd.getOwnerId().intValue());
				if(null != communities && 0 != communities.size()){
					cmd.setOwnerId(communities.get(0).getId());
					cmd.setOwnerType("community");
				}
			}
			response.setYellowPages(new ArrayList<YellowPageDTO>());
			int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
	        CrossShardListingLocator locator = new CrossShardListingLocator();
	        locator.setAnchor(cmd.getPageAnchor());
	        List<YellowPage> yellowPages = this.yellowPageProvider.queryYellowPages(locator, pageSize + 1,cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParentId(),cmd.getType(),cmd.getServiceType(),cmd.getKeywords());
	        if(null == yellowPages || yellowPages.size() == 0)
	        	return response;
	      
	        for (YellowPage yellowPage : yellowPages){
	        	populateYellowPage(yellowPage);
	        	if(cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())){
	    			ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
	    			response.getYellowPages().add( ConvertHelper.convert(serviceAlliance,YellowPageDTO.class) );
	    		} 
	
	    		else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())){
	    			MakerZone makerZone =  ConvertHelper.convert(yellowPage ,MakerZone.class);
	    			response.getYellowPages().add(ConvertHelper.convert(makerZone,YellowPageDTO.class));
	    		} 
	
	    		else{
	    			response.getYellowPages().add(ConvertHelper.convert(yellowPage,YellowPageDTO.class));
	    		}
	        }
		}
        return response;
	}
	

	@Override
	public void addYellowPage(AddYellowPageCommand cmd) { 
		YellowPage yp = null;
		if(cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())){
			ServiceAlliance serviceAlliance =  ConvertHelper.convert(cmd ,ServiceAlliance.class);
			yp = ConvertHelper.convert(serviceAlliance,YellowPage.class);
		} 
		else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())){
			MakerZone makerZone =  ConvertHelper.convert(cmd ,MakerZone.class);
			yp = ConvertHelper.convert(makerZone,YellowPage.class);
		} 
		else{
			yp =  ConvertHelper.convert(cmd,YellowPage.class);
		}
		this.yellowPageProvider.createYellowPage(yp);
		createYellowPageAttachments(cmd.getAttachments(),yp.getId());
		
		
	} 
	private void createYellowPageAttachments(
			List<YellowPageAattchmentDTO> attachments,Long ownerId) {
		if(null == attachments)
			return;
		for (YellowPageAattchmentDTO dto:attachments ){
			if(null == dto.getContentUri())
				continue;
			YellowPageAttachment attachment =  ConvertHelper.convert(dto,YellowPageAttachment.class);
			attachment.setOwnerId(ownerId);
			attachment.setContentType(PostContentType.IMAGE.getCode());
			this.yellowPageProvider.createYellowPageAttachments(attachment);
		}
	}

	@Override
	public void deleteYellowPage(DeleteYellowPageCommand cmd) {
		 
		YellowPage yellowPage = this.yellowPageProvider.getYellowPageById(cmd.getId());
		if (null == yellowPage)	
		{
			 LOGGER.error("YellowPage already deleted , YellowPage Id =" + cmd.getId() );
		}
		yellowPage.setStatus(YellowPageStatus.INACTIVE.getCode());
		if(null != yellowPage.getLatitude())
			yellowPage.setGeohash(GeoHashUtils.encode(yellowPage.getLatitude(), yellowPage.getLongitude()));
		this.yellowPageProvider.updateYellowPage(yellowPage);
	}

	@Override
	public void updateYellowPage(UpdateYellowPageCommand cmd) {
		YellowPage yp = null;
		if(cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())){
			ServiceAlliance serviceAlliance =  ConvertHelper.convert(cmd ,ServiceAlliance.class);
			yp = ConvertHelper.convert(serviceAlliance,YellowPage.class);
		} 
		else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())){
			MakerZone makerZone =  ConvertHelper.convert(cmd ,MakerZone.class);
			yp = ConvertHelper.convert(makerZone,YellowPage.class);
		} 
		else{
			yp =  ConvertHelper.convert(cmd,YellowPage.class);
		}
		this.yellowPageProvider.updateYellowPage(yp);
		this.yellowPageProvider.deleteYellowPageAttachmentsByOwnerId(yp.getId());
		createYellowPageAttachments(cmd.getAttachments(),yp.getId());
	}

	@Override
	public YellowPageDTO getYellowPageTopic(GetYellowPageTopicCommand cmd) {
		 
		YellowPageDTO response = null;
		if(cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceCommand command = ConvertHelper.convert(cmd, GetServiceAllianceCommand.class);
			command.setType(cmd.getType().longValue());
			ServiceAllianceDTO sa = getServiceAlliance(command);
					
			response = ConvertHelper.convert(sa,YellowPageDTO.class);
			
			
		} else {
		 
			YellowPage yellowPage = this.yellowPageProvider.queryYellowPageTopic(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getType());
			if (null == yellowPage)
				{
				LOGGER.error("can not find the topic community ID = "+cmd.getOwnerId() +"; and type = "+cmd.getType()); 
				return null;
				}
			populateYellowPage(yellowPage);
			if(cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())){
				ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
				response = ConvertHelper.convert(serviceAlliance,YellowPageDTO.class);
			} 
			else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())){
				MakerZone makerZone =  ConvertHelper.convert(yellowPage ,MakerZone.class);
				response = ConvertHelper.convert(makerZone,YellowPageDTO.class);
			} 
			else{
					response =  ConvertHelper.convert(yellowPage,YellowPageDTO.class);
			}
		}
		return response;
	}

	@Override
	public void updateServiceAllianceCategory(UpdateServiceAllianceCategoryCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		ServiceAllianceCategories category = new ServiceAllianceCategories();
		ServiceAllianceCategories parent = yellowPageProvider.findCategoryById(cmd.getParentId());
		
		if(null == parent) {
			LOGGER.error("wrong parentId. parentId = " + cmd.getParentId());
			throw RuntimeErrorException
			.errorWith(
					YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_CATEGORY_NOT_FOUNT,
					localeStringService.getLocalizedString(
							String.valueOf(YellowPageServiceErrorCode.SCOPE),
							String.valueOf(YellowPageServiceErrorCode.ERROR_CATEGORY_NOT_FOUNT),
							UserContext.current().getUser().getLocale(),
							"parent category not found!"));
		}
		
		if(cmd.getCategoryId() == null) {
			category.setName(cmd.getName());
			category.setOwnerId(cmd.getOwnerId());
			category.setOwnerType(cmd.getOwnerType());
			category.setNamespaceId(namespaceId);
			category.setStatus((byte)2);
            category.setDisplayMode(cmd.getDisplayMode());
			category.setDisplayDestination(cmd.getDisplayDestination());
            /*if(null == parent) {
				category.setParentId(0L);
				category.setPath(cmd.getName());
			} else {
				category.setParentId(parent.getId());
				category.setPath(parent.getName() + "/" + cmd.getName());
			}*/
			category.setParentId(parent.getId());
			category.setPath(parent.getName() + "/" + cmd.getName());
			category.setLogoUrl(cmd.getLogoUrl());
			yellowPageProvider.createCategory(category);
		} else {
			category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
			category.setName(cmd.getName());
			/*if(null == parent) {
				category.setPath(cmd.getName());
			} else {
				category.setPath(parent.getName() + "/" + cmd.getName());
			}*/
			category.setPath(parent.getName() + "/" + cmd.getName());
			category.setLogoUrl(cmd.getLogoUrl());
            category.setDisplayMode(cmd.getDisplayMode());
			category.setDisplayDestination(cmd.getDisplayDestination());
            yellowPageProvider.updateCategory(category);

            if (!Objects.equals(category.getName(), cmd.getName())) {
                //yellow page中引用该类型的记录的serviceType字段也要更新
                List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());
                if(yps != null && yps.size() > 0) {
                    for(YellowPage yp : yps) {
                        yp.setStringTag3(category.getName());
                        yellowPageProvider.updateYellowPage(yp);
                    }
                }
            }
		}
		
		/*Category category = new Category();
		Category parent = categoryProvider.findCategoryById(CategoryConstants.CATEGORY_ID_YELLOW_PAGE);
		if(cmd.getCategoryId() == null) {
			category.setParentId(CategoryConstants.CATEGORY_ID_YELLOW_PAGE);
			category.setName(cmd.getName());
			category.setNamespaceId(namespaceId);
			category.setStatus((byte)2);
			category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			category.setPath(parent.getName() + "/" + cmd.getName());
			categoryProvider.createCategory(category);
		} else {
			category = categoryProvider.findCategoryById(cmd.getCategoryId());
			category.setName(cmd.getName());
			category.setPath(parent.getName() + "/" + cmd.getName());
			categoryProvider.updateCategory(category);

			//yellow page中引用该类型的记录的serviceType字段也要更新
			List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());
			if(yps != null && yps.size() > 0) {
				for(YellowPage yp : yps) {
					yp.setStringTag3(category.getName());
					yellowPageProvider.updateYellowPage(yp);
				}
			}
		}*/
		
	}

	@Override
	public void deleteServiceAllianceCategory(DeleteServiceAllianceCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		
		List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());
		
		if(yps != null && yps.size() > 0) {
			LOGGER.error("the category which id = "+cmd.getCategoryId()+" is in use!");
			throw RuntimeErrorException
					.errorWith(
							YellowPageServiceErrorCode.SCOPE,
							YellowPageServiceErrorCode.ERROR_CATEGORY_IN_USE,
							localeStringService.getLocalizedString(
									String.valueOf(YellowPageServiceErrorCode.SCOPE),
									String.valueOf(YellowPageServiceErrorCode.ERROR_CATEGORY_IN_USE),
									UserContext.current().getUser().getLocale(),
									"category is in use!"));
		}
		
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
		if(null == category || category.getStatus() == 0) {
			LOGGER.error("the category which id = "+cmd.getCategoryId()+" is already deleted!");
			throw RuntimeErrorException
					.errorWith(
							YellowPageServiceErrorCode.SCOPE,
							YellowPageServiceErrorCode.ERROR_CATEGORY_ALREADY_DELETED,
							localeStringService.getLocalizedString(
									String.valueOf(YellowPageServiceErrorCode.SCOPE),
									String.valueOf(YellowPageServiceErrorCode.ERROR_CATEGORY_ALREADY_DELETED),
									UserContext.current().getUser().getLocale(),
									"category already deleted!"));
		}
		
		if(category != null && category.getParentId() == 0) {
			LOGGER.error("the category which parent id is 0!");
			throw RuntimeErrorException
					.errorWith(
							YellowPageServiceErrorCode.SCOPE,
							YellowPageServiceErrorCode.ERROR_DELETE_ROOT_CATEGORY,
							localeStringService.getLocalizedString(
									String.valueOf(YellowPageServiceErrorCode.SCOPE),
									String.valueOf(YellowPageServiceErrorCode.ERROR_DELETE_ROOT_CATEGORY),
									UserContext.current().getUser().getLocale(),
									"cannot delete root category!"));
		}
		
		//set status inactive and add log
		category.setStatus((byte) 0);
		yellowPageProvider.updateCategory(category);
		
		AuditLog log = new AuditLog();
		log.setOperatorUid(user.getId());
		log.setOperationType("DELETE");
		log.setResourceType(EntityType.SACATEGORY.getCode());
		log.setResourceId(category.getId());
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		auditLogProvider.createAuditLog(log);
	}

	@Override
	public ServiceAllianceDTO getServiceAllianceEnterpriseDetail(
			GetServiceAllianceEnterpriseDetailCommand cmd) {
//		YellowPage yellowPage = verifyYellowPage(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
//		
//		populateYellowPage(yellowPage);
		
		ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		populateServiceAlliance(sa);
		
		ServiceAllianceDTO response = null;
		
//		ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
		response = ConvertHelper.convert(sa,ServiceAllianceDTO.class);
		if(!StringUtils.isEmpty(response.getTemplateType())) {
			RequestTemplates template = userActivityProvider.getCustomRequestTemplate(response.getTemplateType());
			if(template != null) {
				response.setTemplateName(template.getName());
				response.setButtonTitle(template.getButtonTitle());
			}
			
		}
//		response.setDisplayName(serviceAlliance.getNickName());
		
		return response;
	}

	@Override
	public ServiceAllianceDTO getServiceAlliance(GetServiceAllianceCommand cmd) {
//		YellowPage yellowPage = this.yellowPageProvider.queryYellowPageTopic(cmd.getOwnerType(),cmd.getOwnerId(),YellowPageType.SERVICEALLIANCE.getCode());
//		if (null == yellowPage)
//		{
//			LOGGER.error("can not find the topic community ID = "+cmd.getOwnerId() +"; and type = " + YellowPageType.SERVICEALLIANCE.getCode()); 
//			return null;
//		}
//		populateYellowPage(yellowPage);
		
		ServiceAlliances sa = this.yellowPageProvider.queryServiceAllianceTopic(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getType());
		if (null == sa)
			{
				LOGGER.error("can not find the topic community ID = "+cmd.getOwnerId() +"; and type = " + cmd.getType()); 
				return null;
			}
		populateServiceAlliance(sa);
		
		if(null == sa.getServiceType() && null != sa.getCategoryId()) {
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getCategoryId());
			sa.setServiceType(category.getName());
		}
		ServiceAllianceDTO dto = ConvertHelper.convert(sa,ServiceAllianceDTO.class);
		if(!StringUtils.isEmpty(dto.getTemplateType())) {
			RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
			if(template != null) {
				dto.setTemplateName(template.getName());
				dto.setButtonTitle(template.getButtonTitle());
			}
		}
		this.processDetailUrl(dto);
		
		return dto;
	}

	@Override
	public ServiceAllianceListResponse getServiceAllianceEnterpriseList(
			GetServiceAllianceEnterpriseListCommand cmd) {
		
		if(null != cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
		}else if(null != cmd.getOwnerId()){
			List<Community> communities = communityProvider.listCommunitiesByNamespaceId(cmd.getOwnerId().intValue());
			if(null != communities && 0 != communities.size()){
				cmd.setOwnerId(communities.get(0).getId());
				cmd.setOwnerType("community");
			}
		}
		ServiceAllianceListResponse response = new ServiceAllianceListResponse();
		response.setSkipType((byte) 0);

		ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(cmd.getType());
		if(rule != null) {
			response.setSkipType((byte) 1);
		}
		
		rule = yellowPageProvider.getCateorySkipRule(cmd.getCategoryId());
		if(rule != null) {
			response.setSkipType((byte) 1);
		}
		
		response.setDtos(new ArrayList<ServiceAllianceDTO>());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getNextPageAnchor());
//        List<YellowPage> yellowPages = this.yellowPageProvider.queryServiceAlliance(locator, pageSize + 1,cmd.getOwnerType(), 
//        		cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), cmd.getKeywords());
        List<ServiceAlliances> sas = this.yellowPageProvider.queryServiceAlliance(locator, pageSize + 1,cmd.getOwnerType(), 
        		cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), cmd.getKeywords());
        if(null == sas || sas.size() == 0)
        	return response;
      
        if(sas.size() > pageSize) {
        	sas.remove(sas.size() - 1);
        	response.setNextPageAnchor(sas.get(sas.size() - 1).getId());
        }
        
        for (ServiceAlliances sa : sas){
//        	populateYellowPage(yellowPage);
//			ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
        	populateServiceAlliance(sa);
			if(null == sa.getServiceType() && null != sa.getCategoryId()) {
				ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getCategoryId());
				sa.setServiceType(category.getName());
			}
			ServiceAllianceDTO dto = ConvertHelper.convert(sa,ServiceAllianceDTO.class);
			if(dto.getJumpType() != null) {
				
				if(JumpType.TEMPLATE.equals(JumpType.fromCode(dto.getJumpType()))) {
					RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
					if(template != null) {
						dto.setTemplateName(template.getName());
						dto.setButtonTitle(template.getButtonTitle());
					}
				} else if(JumpType.MODULE.equals(JumpType.fromCode(dto.getJumpType()))) {
					dto.setTemplateName(dto.getTemplateType());
					dto.setButtonTitle(dto.getTemplateType());
				}
			} else {
				//兼容以前只有模板跳转时jumptype字段为null的情况
				if(dto.getTemplateType() != null) {
					RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
					if(template != null) {
						dto.setTemplateName(template.getName());
						dto.setButtonTitle(template.getButtonTitle());
					}
				}
				
			}
			
			this.processDetailUrl(dto);
//			dto.setDisplayName(serviceAlliance.getNickName());
			response.getDtos().add(dto);

        }
        return response;
	}

    private void processDetailUrl(ServiceAllianceDTO dto) {
        try {
            String detailUrl = configurationProvider.getValue(ServiceAllianceConst.SERVICE_ALLIANCE_DETAIL_URL_CONF, "");
            String name = org.apache.commons.lang.StringUtils.trimToEmpty(dto.getName());
            String url = String.format(detailUrl, dto.getId(), URLEncoder.encode(name, "UTF-8"), RandomUtils.nextInt(2));
            dto.setDetailUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
	public void updateServiceAlliance(UpdateServiceAllianceCommand cmd) {
		ServiceAlliances sa = null;
		
		if(cmd.getId() == null) {
			sa =  ConvertHelper.convert(cmd ,ServiceAlliances.class);
			sa.setCreatorUid(UserContext.current().getUser().getId());
			this.yellowPageProvider.createServiceAlliances(sa);
		} else {
			sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			
			sa.setName(cmd.getName());
			sa.setDisplayName(cmd.getDisplayName());
			sa.setContact(cmd.getContact());
			sa.setDescription(cmd.getDescription());
			sa.setPosterUri(cmd.getPosterUri());
			this.yellowPageProvider.updateServiceAlliances(sa);
		}
		
//		YellowPage yp = null;
//		
//		if(cmd.getId() == null) {
//			ServiceAlliance serviceAlliance =  ConvertHelper.convert(cmd ,ServiceAlliance.class);
//			yp = ConvertHelper.convert(serviceAlliance,YellowPage.class);
//			yp.setType(YellowPageType.SERVICEALLIANCE.getCode());
//			yp.setCreatorUid(UserContext.current().getUser().getId());
//			this.yellowPageProvider.createYellowPage(yp);
//		} else {
//			yp = verifyYellowPage(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
//			
//			yp.setName(cmd.getName());
//			yp.setNickName(cmd.getDisplayName());
//			yp.setContact(cmd.getContact());
//			yp.setDescription(cmd.getDescription());
//			yp.setPosterUri(cmd.getPosterUri());
//			this.yellowPageProvider.updateYellowPage(yp);
//		}
		
		
	}
	
//	private YellowPage verifyYellowPage(Long id, String ownerType, Long ownerId) {
//		
//		YellowPage yellowPage = this.yellowPageProvider.findYellowPageById(id, ownerType, ownerId);
//		if (null == yellowPage || YellowPageStatus.INACTIVE.equals(YellowPageStatus.fromCode(yellowPage.getStatus()))) {
//			 LOGGER.error("YellowPage already deleted , YellowPage Id =" + id );
//			 throw RuntimeErrorException
//				.errorWith(
//						YellowPageServiceErrorCode.SCOPE,
//						YellowPageServiceErrorCode.ERROR_YELLOWPAGE_ALREADY_DELETED,
//						localeStringService.getLocalizedString(
//								String.valueOf(YellowPageServiceErrorCode.SCOPE),
//								String.valueOf(YellowPageServiceErrorCode.ERROR_YELLOWPAGE_ALREADY_DELETED),
//								UserContext.current().getUser().getLocale(),
//								"YellowPage already deleted!"));
//		}
//
//		return yellowPage;
//	}
	
	private ServiceAlliances verifyServiceAlliance(Long id, String ownerType, Long ownerId) {
		
		ServiceAlliances sa = this.yellowPageProvider.findServiceAllianceById(id, ownerType, ownerId);
		if (null == sa || YellowPageStatus.INACTIVE.equals(YellowPageStatus.fromCode(sa.getStatus()))) {
			 LOGGER.error("ServiceAlliance already deleted , ServiceAlliance Id =" + id );
			 throw RuntimeErrorException
				.errorWith(
						YellowPageServiceErrorCode.SCOPE,
						YellowPageServiceErrorCode.ERROR_SERVICEALLIANCE_ALREADY_DELETED,
						localeStringService.getLocalizedString(
								String.valueOf(YellowPageServiceErrorCode.SCOPE),
								String.valueOf(YellowPageServiceErrorCode.ERROR_SERVICEALLIANCE_ALREADY_DELETED),
								UserContext.current().getUser().getLocale(),
								"ServiceAlliance already deleted!"));
		}

		return sa;
	}

	@Override
	public void deleteServiceAllianceEnterprise(
			DeleteServiceAllianceEnterpriseCommand cmd) {
		
		ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		sa.setStatus(YellowPageStatus.INACTIVE.getCode());
		this.yellowPageProvider.updateServiceAlliances(sa);
	}

	@Override
	public void updateServiceAllianceEnterprise(
			UpdateServiceAllianceEnterpriseCommand cmd) {
		
		ServiceAlliances serviceAlliance =  ConvertHelper.convert(cmd ,ServiceAlliances.class);
		
		if(null != serviceAlliance.getCategoryId()) {
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(serviceAlliance.getCategoryId());
			serviceAlliance.setServiceType(category.getName());
		}
		
		if(cmd.getId() == null) {
			serviceAlliance.setCreatorUid(UserContext.current().getUser().getId());
			if(serviceAlliance.getLatitude() != null && serviceAlliance.getLongitude() != null) {
				serviceAlliance.setGeohash(GeoHashUtils.encode(serviceAlliance.getLatitude(), serviceAlliance.getLongitude()));
			}
			
			this.yellowPageProvider.createServiceAlliances(serviceAlliance);
			createServiceAllianceAttachments(cmd.getAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.BANNER.getCode());
			createServiceAllianceAttachments(cmd.getFileAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.FILE_ATTACHMENT.getCode());
		} else {
			ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			if(sa.getLatitude() != null && sa.getLongitude() != null) {
				sa.setGeohash(GeoHashUtils.encode(sa.getLatitude(), sa.getLongitude()));
			}
			serviceAlliance.setType(sa.getType());
			serviceAlliance.setCreateTime(sa.getCreateTime());
			serviceAlliance.setCreatorUid(sa.getCreatorUid());
			
			this.yellowPageProvider.updateServiceAlliances(serviceAlliance);
			this.yellowPageProvider.deleteServiceAllianceAttachmentsByOwnerId(serviceAlliance.getId());
			createServiceAllianceAttachments(cmd.getAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.BANNER.getCode());
			createServiceAllianceAttachments(cmd.getFileAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.FILE_ATTACHMENT.getCode());
		}
//		YellowPage yp = null;
//		ServiceAlliance serviceAlliance =  ConvertHelper.convert(cmd ,ServiceAlliance.class);
//		
//		if(null != serviceAlliance.getCategoryId()) {
//			Category category = categoryProvider.findCategoryById(serviceAlliance.getCategoryId());
//			serviceAlliance.setServiceType(category.getName());
//		}
//		
//		if(cmd.getId() == null) {
//			yp = ConvertHelper.convert(serviceAlliance,YellowPage.class);
//			yp.setType(YellowPageType.SERVICEALLIANCE.getCode());
//			yp.setNickName(cmd.getDisplayName());
//			yp.setCreatorUid(UserContext.current().getUser().getId());
//			if(yp.getLatitude() != null && yp.getLongitude() != null) {
//				yp.setGeohash(GeoHashUtils.encode(yp.getLatitude(), yp.getLongitude()));
//			}
//			
//			this.yellowPageProvider.createYellowPage(yp);
//			createYellowPageAttachments(cmd.getAttachments(),yp.getId());
//		} else {
//			YellowPage yellowPage = verifyYellowPage(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
//			yp = ConvertHelper.convert(serviceAlliance,YellowPage.class);
//			yp.setNickName(cmd.getDisplayName());
//			if(yp.getLatitude() != null && yp.getLongitude() != null) {
//				yp.setGeohash(GeoHashUtils.encode(yp.getLatitude(), yp.getLongitude()));
//			}
//			yp.setType(yellowPage.getType());
//			yp.setCreateTime(yellowPage.getCreateTime());
//			yp.setCreatorUid(yellowPage.getCreatorUid());
//			
//			this.yellowPageProvider.updateYellowPage(yp);
//			this.yellowPageProvider.deleteYellowPageAttachmentsByOwnerId(yp.getId());
//			createYellowPageAttachments(cmd.getAttachments(),yp.getId());
//		}
		
	}
	
	private void createServiceAllianceAttachments(
			List<ServiceAllianceAttachmentDTO> attachments,Long ownerId, Byte attachmentType) {
		if(null == attachments)
			return;
		for (ServiceAllianceAttachmentDTO dto:attachments ){
			if(null == dto.getContentUri())
				continue;
			ServiceAllianceAttachment attachment =  ConvertHelper.convert(dto,ServiceAllianceAttachment.class);
			attachment.setOwnerId(ownerId);
//			attachment.setContentType(PostContentType.IMAGE.getCode());
			attachment.setAttachmentType(attachmentType);
			
			attachment.setCreatorUid(UserContext.current().getUser().getId());
	        ContentServerResource resource = contentServerService.findResourceByUri(dto.getContentUri());
	        Integer size = resource.getResourceSize();
	        attachment.setFileSize(size);
	        
			this.yellowPageProvider.createServiceAllianceAttachments(attachment);
		}
	}
	
	private void populateServiceAlliance(ServiceAlliances sa) { 
		this.yellowPageProvider.populateServiceAlliancesAttachment(sa);
		 
		populateServiceAllianceUrl(sa);
		populateServiceAllianceAttachements(sa,sa.getAttachments());
		populateServiceAllianceAttachements(sa,sa.getFileAttachments());
	 
		
	}

	private void populateServiceAllianceUrl(ServiceAlliances sa) { 
		 
		 String posterUri = sa.getPosterUri();
	        if(posterUri != null && posterUri.length() > 0) {
	            try{
	                String posterUrl = contentServerService.parserUri(sa.getPosterUri(), EntityType.USER.getCode(),UserContext.current().getUser().getId());
	                sa.setPosterUrl(posterUrl);
	            }catch(Exception e){
	                LOGGER.error("Failed to parse poster uri of ServiceAlliances, ServiceAlliances =" + sa, e);
	            }
	        }	 
	}
	 private void populateServiceAllianceAttachements(ServiceAlliances sa, List<ServiceAllianceAttachment> attachmentList) {
		 
		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The ServiceAlliances attachment list is empty, ServiceAlliancesId=" + sa.getId());
	            }
		 } else {
	            for(ServiceAllianceAttachment attachment : attachmentList) {
	            	populateServiceAllianceAttachement(sa, attachment);
	            }
		 }
	 }
	 
	 private void populateServiceAllianceAttachement(ServiceAlliances sa, ServiceAllianceAttachment attachment) {
        
		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The ServiceAlliances attachment is null, ServiceAlliancesId=" + sa.getId());
			 }
		 } else {
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(),UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, ServiceAlliancesId=" + sa.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				 }
			 }
		 }
	 }

	@Override
	public void addTarget(AddNotifyTargetCommand cmd) {
		
		ServiceAllianceNotifyTargets isExist = this.yellowPageProvider.findNotifyTarget(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getCategoryId(), cmd.getContactType() , cmd.getContactToken());
		if(isExist != null) {
			if(ContactType.EMAIL.equals(ContactType.fromCode(cmd.getContactType()))) {
				throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
						YellowPageServiceErrorCode.ERROR_NOTIFY_EMAIL_EXIST,
	 				"邮箱已存在，请重新输入");
			} 
			
			if(ContactType.MOBILE.equals(ContactType.fromCode(cmd.getContactType()))) {
				throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
						YellowPageServiceErrorCode.ERROR_NOTIFY_MOBILE_EXIST,
	 				"该手机号已添加");
			} 
				
		}
		ServiceAllianceNotifyTargets target = ConvertHelper.convert(cmd, ServiceAllianceNotifyTargets.class);
		target.setNamespaceId(UserContext.getCurrentNamespaceId());
		
		this.yellowPageProvider.createNotifyTarget(target);
		
	}

	@Override
	public void deleteNotifyTarget(DeleteNotifyTargetCommand cmd) {

		ServiceAllianceNotifyTargets target = verifyNotifyTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
		this.yellowPageProvider.deleteNotifyTarget(cmd.getId());
	}

	@Override
	public void setNotifyTargetStatus(SetNotifyTargetStatusCommand cmd) {
		ServiceAllianceNotifyTargets target = verifyNotifyTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
		target.setStatus(cmd.getStatus());
		this.yellowPageProvider.updateNotifyTarget(target);
		
	}
	
	private ServiceAllianceNotifyTargets verifyNotifyTarget(String ownerType, Long ownerId, Long id) {
		ServiceAllianceNotifyTargets target = this.yellowPageProvider.findNotifyTarget(ownerType, ownerId, id);
		if(target == null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_TARGET,
 				"推送者不存在");
		}
		return target;
	}

	@Override
	public ListNotifyTargetsResponse listNotifyTargets(
			ListNotifyTargetsCommand cmd) {
		ListNotifyTargetsResponse response = new ListNotifyTargetsResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
		List<ServiceAllianceNotifyTargets> targets = this.yellowPageProvider.listNotifyTargets(cmd.getOwnerType(), cmd.getOwnerId(), 
				cmd.getContactType(), cmd.getCategoryId(), locator, pageSize+1);
		
		if(targets != null && targets.size() > 0) {
			
			Long nextPageAnchor = null;
			if(targets.size() > pageSize) {
	        	targets.remove(targets.size() - 1);
	            nextPageAnchor = targets.get(targets.size() - 1).getId();
	        }
			
			List<NotifyTargetDTO> dtos = targets.stream().map((target) -> {
				NotifyTargetDTO dto = ConvertHelper.convert(target, NotifyTargetDTO.class);
				return dto;
			}).filter(r->r!=null).collect(Collectors.toList());
			
			response.setDtos(dtos);
			
	        response.setNextPageAnchor(nextPageAnchor);
		}
		return response;
	}

	@Override
	public void verifyNotifyTarget(VerifyNotifyTargetCommand cmd) {

		ServiceAllianceNotifyTargets target = this.yellowPageProvider.findNotifyTarget(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getCategoryId(), ContactType.MOBILE.getCode() , cmd.getContactToken());
		if(target != null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_MOBILE_EXIST,
 				"该手机号已添加");
		}
		
		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), cmd.getContactToken());
		if(identifier == null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_TARGET_NOT_REGISTER,
 				"该手机号还未注册，请先注册");
		}
	}

    @Override
    public List<ServiceAllianceCategoryDTO> listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<Byte> displayDestination = new ArrayList<>();
		if(cmd.getDestination() != null) {
			displayDestination.add(cmd.getDestination());
			displayDestination.add(ServiceAllianceCategoryDisplayDestination.BOTH.getCode());
		}

        List<ServiceAllianceCategories> entityResultList = this.yellowPageProvider.listChildCategories(cmd.getOwnerType(), cmd.getOwnerId(),namespaceId,
                cmd.getParentId(), CategoryAdminStatus.ACTIVE, displayDestination);
        return entityResultList.stream().map(r -> {
            ServiceAllianceCategoryDTO dto = ConvertHelper.convert(r, ServiceAllianceCategoryDTO.class);
            String locale = UserContext.current().getUser().getLocale();
            String displayCategoryName = localeStringService.getLocalizedString(ServiceAllianceLocalStringCode.CATEGORY_DISPLAY_SCOPE,
                    String.valueOf(dto.getDisplayMode()), locale, "");
            dto.setDisplayModeName(displayCategoryName);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ServiceAllianceDisplayModeDTO getServiceAllianceDisplayMode(GetServiceAllianceDisplayModeCommand cmd) {
        ServiceAllianceDisplayModeDTO displayModeDTO = new ServiceAllianceDisplayModeDTO();
        displayModeDTO.setDisplayMode(ServiceAllianceCategoryDisplayMode.LIST.getCode());

		List<Byte> displayDestination = new ArrayList<>();
		if(cmd.getDestination() != null) {
			displayDestination.add(cmd.getDestination());
			displayDestination.add(ServiceAllianceCategoryDisplayDestination.BOTH.getCode());
		}

        ServiceAllianceCategories parentCategory = this.yellowPageProvider.findCategoryById(cmd.getParentId());
        if (parentCategory != null) {
            List<ServiceAllianceCategories> childCategories = this.yellowPageProvider.listChildCategories(null, null,
                    UserContext.getCurrentNamespaceId(), parentCategory.getId(), CategoryAdminStatus.ACTIVE, displayDestination);
            if (childCategories != null && childCategories.size() > 0) {
                displayModeDTO.setDisplayMode(childCategories.get(0).getDisplayMode());
            }
        }
        return displayModeDTO;
    }

	@Override
	public List<JumpModuleDTO> listJumpModules() {
		//没配的返回零域的，配了的返回自己域空间的
		List<JumpModuleDTO> modules = yellowPageProvider.jumpModules(UserContext.getCurrentNamespaceId());
		if(modules == null || modules.size() == 0) {
			modules = yellowPageProvider.jumpModules(0);
		}

		return modules;
	}

	@Override
	public ListAttachmentsResponse listAttachments(ListAttachmentsCommand cmd) {
		CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }

        ListAttachmentsResponse response = new ListAttachmentsResponse();
        List<ServiceAllianceAttachment> attachments = yellowPageProvider.listAttachments(locator, cmd.getPageSize() + 1, cmd.getOwnerId());
        if(attachments != null && attachments.size() > 0) {
            if(attachments.size() > cmd.getPageSize()) {
                attachments.remove(attachments.size() - 1);
                response.setNextPageAnchor(attachments.get(attachments.size() - 1).getId());
            }

            List<AttachmentDTO> dtos = attachments.stream().map((r) -> {
            	AttachmentDTO dto = ConvertHelper.convert(r, AttachmentDTO.class);
                String contentUrl = contentServerService.parserUri(dto.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                User creator = userProvider.findUserById(dto.getCreatorUid());
                if(creator != null) {
                    dto.setCreatorName(creator.getNickName());
                }
                dto.setContentUrl(contentUrl);
                return dto;
            }).collect(Collectors.toList());
            response.setAttachments(dtos);
        }

        return response;
	}

}
