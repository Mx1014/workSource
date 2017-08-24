package com.everhomes.yellowPage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.server.schema.Tables;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.reserver.ReserverEntity;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.yellowPage.AddNotifyTargetCommand;
import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.AttachmentDTO;
import com.everhomes.rest.yellowPage.DeleteNotifyTargetCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceDisplayModeCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.JumpModuleDTO;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.ListAttachmentsCommand;
import com.everhomes.rest.yellowPage.ListAttachmentsResponse;
import com.everhomes.rest.yellowPage.ListNotifyTargetsCommand;
import com.everhomes.rest.yellowPage.ListNotifyTargetsResponse;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesCommand;
import com.everhomes.rest.yellowPage.NotifyTargetDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayDestination;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayMode;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDisplayModeDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceListResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceLocalStringCode;
import com.everhomes.rest.yellowPage.ServiceAllianceSourceRequestType;
import com.everhomes.rest.yellowPage.SetNotifyTargetStatusCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDefaultOrderCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDisplayFlagCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.VerifyNotifyTargetCommand;
import com.everhomes.rest.yellowPage.YellowPageAattchmentDTO;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.rest.yellowPage.YellowPageType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.user.RequestTemplates;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

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
    private BuildingProvider buildingProvider;
    
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
    
    private StringTemplateLoader templateLoader;
    
    private Configuration templateConfig;
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;

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
		
		//增加building内容
		if(null != response.getBuildingId()){
			Building building = buildingProvider.findBuildingById(response.getBuildingId());
			response.setBuildingName(building.getName());
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
		
		//没传时 取logourl
		if(cmd.getSelectedLogoUrl() == null) {
			cmd.setSelectedLogoUrl(cmd.getLogoUrl());
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
			category.setSelectedLogoUrl(cmd.getSelectedLogoUrl());
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
			category.setSelectedLogoUrl(cmd.getSelectedLogoUrl());
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
		
		ServiceAllianceDTO dto = null;
		
//		ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
		dto = ConvertHelper.convert(sa,ServiceAllianceDTO.class);
		if(dto.getJumpType() != null) {
			
			if(JumpType.TEMPLATE.equals(JumpType.fromCode(dto.getJumpType()))) {
				RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
				if(template != null) {
					dto.setTemplateName(template.getName());
					dto.setButtonTitle(template.getButtonTitle());
				}
			} else if(JumpType.MODULE.equals(JumpType.fromCode(dto.getJumpType()))) {
				dto.setTemplateName(dto.getTemplateType());
				dto.setButtonTitle("我要申请");
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

		if (!StringUtils.isEmpty(sa.getButtonTitle())) {
			dto.setButtonTitle(sa.getButtonTitle());
		}

		this.processDetailUrl(dto);
//		response.setDisplayName(serviceAlliance.getNickName());
		ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(dto.getOwnerType());
		if(belongType == ServiceAllianceBelongType.COMMUNITY){
			Community community = communityProvider.findCommunityById(dto.getOwnerId());
			if(community != null) {
				dto.setNamespaceId(community.getNamespaceId());
			}
		}else{
			Organization organization = organizationProvider.findOrganizationById(dto.getOwnerId());
			if(organization!=null){
				dto.setNamespaceId(organization.getNamespaceId());
			}
		}
//		dto.setNamespaceId(UserContext.getCurrentNamespaceId());

		processServiceUrl(dto);
		return dto;
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

		long startTime = System.currentTimeMillis();

		if(null != cmd.getCommunityId()) {
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType("community");
		}
		// ownerId 一般传入小区、园区id，这里的ownerId当作域空间来使用，暂时屏蔽掉，不知道会不会引发其他的问题 modify by sw 20170421
//		else if(null != cmd.getOwnerId()){
//
////			List<Community> communities = communityProvider.listCommunitiesByNamespaceId(cmd.getOwnerId().intValue());
////			if(null != communities && 0 != communities.size()){
////				cmd.setOwnerId(communities.get(0).getId());
////				cmd.setOwnerType("community");
////			}
//		}

		long time2 = System.currentTimeMillis();
		LOGGER.info("get community Id time: {}", time2 - startTime);

		ServiceAllianceListResponse response = new ServiceAllianceListResponse();
		response.setSkipType((byte) 0);

		ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(cmd.getType());
		if(rule != null) {
			response.setSkipType((byte) 1);
		}

		long time3 = System.currentTimeMillis();
		LOGGER.info("get rule time: {}", time3 - time2);

		rule = yellowPageProvider.getCateorySkipRule(cmd.getCategoryId());
		if(rule != null) {
			response.setSkipType((byte) 1);
		}

		long time4 = System.currentTimeMillis();
		LOGGER.info("get rule time: {}", time4 - time3);

		response.setDtos(new ArrayList<ServiceAllianceDTO>());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getNextPageAnchor());
//        List<YellowPage> yellowPages = this.yellowPageProvider.queryServiceAlliance(locator, pageSize + 1,cmd.getOwnerType(), 
//        		cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), cmd.getKeywords());
        
        //add by dengs .20170428 如果是客户端来，将community所在organaization 的服务联盟也查询出来。
        List<ServiceAlliances> sas = null;
        ServiceAllianceSourceRequestType sourceRequestType = ServiceAllianceSourceRequestType.fromCode(cmd.getSourceRequestType());
        //如果为CLIENT，或者空值，认为是客户端
        if(ServiceAllianceSourceRequestType.CLIENT == sourceRequestType || sourceRequestType == null){
        	 List<Organization> organizationList= this.organizationProvider.findOrganizationByCommunityId(cmd.getOwnerId());
        	 if(organizationList!=null &&organizationList.size()>0){
        		 //目前只考虑一个物业管理公司的情况。
        		 Organization pm = organizationList.get(0);
        		 sas = this.yellowPageProvider.queryServiceAlliance(locator, pageSize + 1,cmd.getOwnerType(), 
        	        		cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), cmd.getKeywords(),pm.getId(),ServiceAllianceBelongType.ORGANAIZATION.getCode());
        		 for (ServiceAlliances serviceAlliance : sas) {
        			ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(serviceAlliance.getOwnerType());
					if(belongType == ServiceAllianceBelongType.ORGANAIZATION){
						//传给客户端的ownertype,ownerid 都改成小区，兼容老版本
						serviceAlliance.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
						serviceAlliance.setOwnerId(cmd.getOwnerId());
					}
				}
        	 }
        	
        }else{
			Condition conditionOR = null;
			if (cmd.getOwnerType().equals(ServiceAllianceBelongType.ORGANAIZATION.getCode())) {
				List<OrganizationCommunity> communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
				for (OrganizationCommunity organizationCommunity : communityList) {
					Condition condition = Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(organizationCommunity.getCommunityId())
							.and(Tables.EH_SERVICE_ALLIANCES.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
					if (conditionOR == null) {
						conditionOR = condition;
					} else {
						conditionOR = conditionOR.or(condition);
					}
				}
			}
        	sas = this.yellowPageProvider.queryServiceAlliance(locator, pageSize + 1,cmd.getOwnerType(), 
 	        		cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), cmd.getKeywords(),conditionOR);

        }

		long time5 = System.currentTimeMillis();
		LOGGER.info("getServiceAllianceEnterpriseList time: {}", time5 - time4);

        if(null == sas || sas.size() == 0)
        	return response;
      
        if(sas.size() > pageSize) {
        	sas.remove(sas.size() - 1);
	        //modfiy by dengs,通过DEFAULT_ORDER排序了，锚点依据也变成DEFAULT_ORDER
	        response.setNextPageAnchor(sas.get(sas.size() - 1).getDefaultOrder());
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
					dto.setButtonTitle("我要申请");
				}
				// 服务联盟跳转到审批，审批模块可控制在app端是否显示
				if((ServiceAllianceSourceRequestType.CLIENT == sourceRequestType || sourceRequestType == null)
						&& dto.getJumpType() == JumpType.MODULE.getCode()){
					if(dto.getModuleUrl()!=null){
						int start = dto.getModuleUrl().indexOf('?');
						String s[] = dto.getModuleUrl().substring(start).split("&");
						s = s[0].split("=");
						if(s.length>1){
							try {
								Long approveId = Long.valueOf(s[1]);
								GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(approveId);
								if(CommonStatus.ACTIVE.getCode() != approval.getStatus().intValue()){
									dto.setButtonTitle(null);
									dto.setJumpType(JumpType.NONE.getCode());
									dto.setModuleUrl(null);
								}

							}catch (Exception e){}
						}

					}
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

			if (!StringUtils.isEmpty(sa.getButtonTitle())) {
				dto.setButtonTitle(sa.getButtonTitle());
			}

			processServiceUrl(dto);
			this.processDetailUrl(dto);
//			dto.setDisplayName(serviceAlliance.getNickName());
			response.getDtos().add(dto);

        }

        this.processRange(response.getDtos());

		long time6 = System.currentTimeMillis();
		LOGGER.info("populate dto time: {}", time6 - time5);

		LOGGER.info("getServiceAllianceEnterpriseList total time: {}", time6 - startTime);

		return response;
	}

	private  void processRange(List<ServiceAllianceDTO> dtos){
		for (ServiceAllianceDTO dto:dtos){
			String range = dto.getRange();
			if (range!=null && !range.equals("all")){
				String [] communities = range.split(",");
				List<Long> communityIds = new ArrayList<>();
				for (int i = 0;i<communities.length;i++)
					communityIds.add(Long.valueOf(communities[i]));
				List<Community> communities2 = communityProvider.findCommunitiesByIds(communityIds);
				String rangeDisplay = "";
				for (Community co : communities2)
					rangeDisplay += co.getName()+",";
				if (rangeDisplay.length()>0)
					rangeDisplay = rangeDisplay.substring(0,rangeDisplay.length()-1);
				dto.setRangeDisplay(rangeDisplay);
			}
			if (range!=null && range.equals("all"))
				dto.setRangeDisplay("全部");
		}
	}

	private void processServiceUrl(ServiceAllianceDTO dto) {
		if (null != dto.getServiceUrl()) {
			try {
				String serviceUrl = dto.getServiceUrl();
				dto.setDisplayServiceUrl(dto.getServiceUrl());
				String routeUri = configurationProvider.getValue(ConfigConstants.APP_ROUTE_BROWSER_OUTER_URI, "");

				serviceUrl = String.format(routeUri, serviceUrl);
				int index = serviceUrl.indexOf("?");
				if (index != -1) {
					String prefix = serviceUrl.substring(0, index + 1);
					serviceUrl = serviceUrl.substring(index + 1, serviceUrl.length());
					serviceUrl = URLEncoder.encode(serviceUrl, "utf8");
					serviceUrl = prefix + serviceUrl;
				}

				dto.setServiceUrl(serviceUrl);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

    private void processDetailUrl(ServiceAllianceDTO dto) {
        try {
            String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
            String detailUrl = configurationProvider.getValue(ConfigConstants.SERVICE_ALLIANCE_DETAIL_URL, "");
            String name = org.apache.commons.lang.StringUtils.trimToEmpty(dto.getName());
            
            String ownerType = dto.getOwnerType();
            ownerType = (ownerType  == null) ? "" : ownerType;
            Long ownerId = dto.getOwnerId();
            ownerId = (ownerId == null) ? 0 : ownerId;
            detailUrl = String.format(detailUrl, dto.getId(), URLEncoder.encode(name, "UTF-8"), RandomUtils.nextInt(2), ownerType, ownerId);
            
//            detailUrl = String.format(detailUrl, dto.getId(), URLEncoder.encode(name, "UTF-8"), RandomUtils.nextInt(2));
            dto.setDetailUrl(homeUrl + detailUrl);
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
			//设置服务联盟显示在app端，by dengs,20170524.
			serviceAlliance.setDisplayFlag(DisplayFlagType.SHOW.getCode());
			
			this.yellowPageProvider.createServiceAlliances(serviceAlliance);
			createServiceAllianceAttachments(cmd.getAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.BANNER.getCode());
			createServiceAllianceAttachments(cmd.getFileAttachments(),serviceAlliance.getId(), ServiceAllianceAttachmentType.FILE_ATTACHMENT.getCode());
		
//			Map<String, Object> urlMap = new HashMap<String, Object>();
//			urlMap.put("id", serviceAlliance.getId());
//			try {
//				String templateKey = "servicealliance.moduleurl";
//				 try {
//		                templateConfig.getTemplate(templateKey, "UTF8");
//		            }catch(Exception e) {
//		                
//		            }
//				 
//				Template freeMarkerTemplate = null;
//	
//				if(freeMarkerTemplate == null) {
//                    String templateText = serviceAlliance.getModuleUrl();
//                    templateLoader.putTemplate(templateKey, templateText);
//                    freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
//	            }
//	            
//				if(freeMarkerTemplate != null) {
//					String moduleUrl =  FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, urlMap);
//					serviceAlliance.setModuleUrl(moduleUrl);
//					this.yellowPageProvider.updateServiceAlliances(serviceAlliance);
//				} 
//			} catch(Exception e) {
//				if(LOGGER.isErrorEnabled()) {
//					LOGGER.error("updateServiceAllianceEnterprise serviceAlliance:" + serviceAlliance + "modify moduleUrl");
//				}
//			}
			
			if(serviceAlliance.getModuleUrl() != null && serviceAlliance.getModuleUrl().contains("{id}")) {
				String moduleUrl = serviceAlliance.getModuleUrl().replace("{id}", serviceAlliance.getId().toString());
				serviceAlliance.setModuleUrl(moduleUrl);
				this.yellowPageProvider.updateServiceAlliances(serviceAlliance);
			}
		
		} else {
			ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			if(sa.getLatitude() != null && sa.getLongitude() != null) {
				sa.setGeohash(GeoHashUtils.encode(sa.getLatitude(), sa.getLongitude()));
			}
			serviceAlliance.setType(sa.getType());
			serviceAlliance.setCreateTime(sa.getCreateTime());
			serviceAlliance.setCreatorUid(sa.getCreatorUid());
			//by dengs,20170524 序号和是否在app端显示不能更新掉了。
			serviceAlliance.setDefaultOrder(sa.getDefaultOrder());
			serviceAlliance.setDisplayFlag(sa.getDisplayFlag());
			
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
	        attachment.setFileSize(dto.getFileSize());
	        
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

		//TODO:从电商拿当前域空间店铺、

		JumpModuleDTO bisModule = null;
		for (JumpModuleDTO m: modules) {
			if ("BIZS".equals(m.getModuleUrl())) {
				bisModule = m;
				break;
			}
		}

		if (null != bisModule) {
			Map<String,String> param = new HashMap<>();
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			param.put("namespaceId", String.valueOf(namespaceId));
			List<JumpModuleDTO> bizModules = new ArrayList<>();

			String json = post(createRequestParam(param), "/zl-ec/rest/openapi/shop/queryShopInfoByNamespace");
			if (null != json) {
				ReserverEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<ReserverEntity<Object>>(){});
				if (null != entity) {
					Object obj = entity.getBody();
					if (null != obj) {
						List<BizEntity> bizs = JSONObject.parseObject(obj.toString(), new TypeReference<List<BizEntity>>(){});;
						for (BizEntity b: bizs) {
							JumpModuleDTO d = new JumpModuleDTO();
//				long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceJumpModule.class));
//				d.setId(id);
							d.setModuleName(b.getShopName());
							try {
								d.setModuleUrl(String.format("zl://browser/i?url=%s",URLEncoder.encode(b.getShopURL(), StandardCharsets.UTF_8.name())));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							d.setNamespaceId(namespaceId);
							d.setParentId(bisModule.getId());
							bizModules.add(d);

						}
					}
				}
			}
			modules.addAll(bizModules);
		}

		return createTree(modules);
	}

	private List<JumpModuleDTO> createTree(List<JumpModuleDTO> modules) {
		List<JumpModuleDTO> nodeList = new ArrayList<>();
		for(JumpModuleDTO node1 : modules){
			boolean mark = false;
			for(JumpModuleDTO node2 : modules){
				if(node1.getParentId()!= null && node1.getParentId()!= 0L && node1.getParentId().equals(node2.getId())){
					mark = true;
					if(node2.getChildren() == null)
						node2.setChildren(new ArrayList<JumpModuleDTO>());
					node2.getChildren().add(node1);
					break;
				}
			}
			if(!mark){
				nodeList.add(node1);
			}
		}
		return nodeList;
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

	private String post(Map<String,String> param, String method) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String serverUrl = configurationProvider.getValue("position.reserver.serverUrl", "");

		HttpPost httpPost = new HttpPost(serverUrl + method);
		CloseableHttpResponse response = null;

		String json = null;
		try {
			String p = StringHelper.toJsonString(param);
			StringEntity stringEntity = new StringEntity(p, StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("content-type", "application/json");

			response = httpclient.execute(httpPost);

			int status = response.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					json = EntityUtils.toString(entity, "utf8");
				}
			}
		} catch (IOException e) {
			LOGGER.error("Reserver request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Reserver request error.");
		}finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("Reserver close instream, response error, param={}", param, e);
				}
			}
		}
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from business, param={}, json={}", param, json);

		return json;
	}

	private Map createRequestParam(Map<String,String> params) {
		Integer nonce = (int)(Math.random()*1000);
		Long timestamp = System.currentTimeMillis();

		String appKey = configurationProvider.getValue("position.reserver.appKey", "");
		String secretKey = configurationProvider.getValue("position.reserver.secretKey", "");
		params.put("nonce", String.valueOf(nonce));
		params.put("timestamp", String.valueOf(timestamp));
		params.put("appKey", appKey);

		Map<String, String> mapForSignature = new HashMap<>();
		for(Map.Entry<String, String> entry : params.entrySet()) {
			mapForSignature.put(entry.getKey(), entry.getValue());
		}

		String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
		try {
			params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return params;
	}

	@Override
	public void updateServiceAllianceEnterpriseDisplayFlag(UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					" Unknown id = {}",cmd.getId());
		}
		DisplayFlagType flagType = DisplayFlagType.fromCode(cmd.getDisplayFlag());
		if(flagType != null){
			ServiceAlliances serviceAlliance = yellowPageProvider.findServiceAllianceById(cmd.getId(),null,null);
			if(serviceAlliance == null)
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						" Unknown id = {}",cmd.getId());
			cmd.setDisplayFlag(flagType.getCode());
		}
		yellowPageProvider.updateServiceAlliancesDisplayFlag(cmd.getId(),cmd.getDisplayFlag());
	}

	@Override
	public ServiceAllianceListResponse updateServiceAllianceEnterpriseDefaultOrder(
			UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd) {
		List<ServiceAllianceDTO> values = cmd.getValues();
		if(values == null || values.size()<2){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"can't change the order, values = {}",values);
		}
		//检查数据,并且查询原来的defaultorder，并且按照defaultorder升序生成serviceAlliancesList by dengs,20170525
		List<ServiceAlliances>  serviceAlliancesList = checkServiceAllianceEnterpriseOrder(values);
		List<ServiceAlliances>  updateList = new ArrayList<ServiceAlliances>();
		
		for (int i = 0; i < serviceAlliancesList.size(); i++) {
			ServiceAlliances serviceAlliances = new ServiceAlliances();
			serviceAlliances.setId(values.get(i).getId());//原始id
			serviceAlliances.setDefaultOrder(serviceAlliancesList.get(i).getDefaultOrder());//排序后的顺序
			updateList.add(serviceAlliances);
		}
	
		yellowPageProvider.updateOrderServiceAllianceDefaultOrder(updateList);
		
		//返回更新后的结果
		ServiceAllianceListResponse response = new ServiceAllianceListResponse();
		response.setDtos(updateList.stream().map(r->ConvertHelper.convert(r, ServiceAllianceDTO.class)).collect(Collectors.toList()));
		return response;
	}

	/**
	 * 检查需要排序的服务联盟集合的id和defaultOrder
	 */
//	private Map<String, Long> checkServiceAllianceEnterpriseOrder(List<ServiceAllianceDTO> values) {
	private List<ServiceAlliances> checkServiceAllianceEnterpriseOrder(List<ServiceAllianceDTO> values) {
		Map<String, Long> idOrderMap = new HashMap<String,Long>();
		
		List<ServiceAlliances>  serviceAllianceList = yellowPageProvider.listServiceAllianceSortOrders(
				values.stream().map(value -> value.getId()).collect(Collectors.toList()));
		
		if(values.size() != serviceAllianceList.size()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					" Uknown Ids = {}",values);
		}
		
		Collections.sort(serviceAllianceList,(s1,s2)->{
			if(s1.getDefaultOrder()-s2.getDefaultOrder() == 0L){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						" repeated service alliance id = {}",s1.getId());
			}
			return s1.getDefaultOrder()>s2.getDefaultOrder()?1:-1;
		});
		
//		for (ServiceAlliances serviceAlliances : serviceAllianceList) {
//			String key = String.valueOf(serviceAlliances.getId());
//			//检查前端传入的集合中，存在重复的服务联盟企业的情况。抛出异常。
//			if(idOrderMap.containsKey(key)){
//				
//			}
//			idOrderMap.put(key, serviceAlliances.getDefaultOrder());
//		}
//		return idOrderMap;
		return serviceAllianceList;
	}
}
