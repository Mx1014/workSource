package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.hsr.geohash.GeoHash;

import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.rentalv2.AttachmentType;
import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceListResponse;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.YellowPageAattchmentDTO;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.rest.yellowPage.YellowPageType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class YellowPageServiceImpl implements YellowPageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageServiceImpl.class);

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
		 
		YellowPage yellowPage = this.yellowPageProvider.getYellowPageById(cmd.getId());
	
		populateYellowPage(yellowPage);
		YellowPageDTO response = null;
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
		return response;
	}
 
	@Override
	public YellowPageListResponse getYellowPageList(
			GetYellowPageListCommand cmd) { 
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
		YellowPageListResponse response = new YellowPageListResponse();
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
		 
		YellowPage yellowPage = this.yellowPageProvider.queryYellowPageTopic(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getType());
		if (null == yellowPage)
			{
			LOGGER.error("can not find the topic community ID = "+cmd.getOwnerId() +"; and type = "+cmd.getType()); 
			return null;
			}
		populateYellowPage(yellowPage);
		YellowPageDTO response = null;
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
		return response;
	}

	@Override
	public void updateServiceAllianceCategory(UpdateServiceAllianceCategoryCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		ServiceAllianceCategories category = new ServiceAllianceCategories();
		ServiceAllianceCategories parent = yellowPageProvider.findCategoryById(cmd.getParentId());
		if(cmd.getCategoryId() == null) {
			category.setParentId(CategoryConstants.CATEGORY_ID_YELLOW_PAGE);
			category.setName(cmd.getName());
			category.setOwnerId(cmd.getOwnerId());
			category.setOwnerType(cmd.getOwnerType());
			category.setNamespaceId(namespaceId);
			category.setStatus((byte)2);
			category.setPath(parent.getName() + "/" + cmd.getName());
			yellowPageProvider.createCategory(category);
		} else {
			category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
			category.setName(cmd.getName());
			category.setPath(parent.getName() + "/" + cmd.getName());
			yellowPageProvider.updateCategory(category);
			
			//yellow page中引用该类型的记录的serviceType字段也要更新
			List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());
			if(yps != null && yps.size() > 0) {
				for(YellowPage yp : yps) {
					yp.setStringTag3(category.getName());
					yellowPageProvider.updateYellowPage(yp);
				}
			}
		}
		
//		Category category = new Category();
//		Category parent = categoryProvider.findCategoryById(CategoryConstants.CATEGORY_ID_YELLOW_PAGE);
//		if(cmd.getCategoryId() == null) {
//			category.setParentId(CategoryConstants.CATEGORY_ID_YELLOW_PAGE);
//			category.setName(cmd.getName());
//			category.setNamespaceId(namespaceId);
//			category.setStatus((byte)2);
//			category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			category.setPath(parent.getName() + "/" + cmd.getName());
//			categoryProvider.createCategory(category);
//		} else {
//			category = categoryProvider.findCategoryById(cmd.getCategoryId());
//			category.setName(cmd.getName());
//			category.setPath(parent.getName() + "/" + cmd.getName());
//			categoryProvider.updateCategory(category);
//			
//			//yellow page中引用该类型的记录的serviceType字段也要更新
//			List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());
//			if(yps != null && yps.size() > 0) {
//				for(YellowPage yp : yps) {
//					yp.setStringTag3(category.getName());
//					yellowPageProvider.updateYellowPage(yp);
//				}
//			}
//		}
		
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
		ServiceAllianceDTO response = null;
//		ServiceAlliance serviceAlliance =  ConvertHelper.convert(yellowPage ,ServiceAlliance.class);
		response = ConvertHelper.convert(sa,ServiceAllianceDTO.class);
//		response.setDisplayName(serviceAlliance.getNickName());
		
		return response;
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
//			dto.setDisplayName(serviceAlliance.getNickName());
			response.getDtos().add(dto);

        }
        return response;
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
			createServiceAllianceAttachments(cmd.getAttachments(),serviceAlliance.getId());
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
			createServiceAllianceAttachments(cmd.getAttachments(),serviceAlliance.getId());
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
			List<ServiceAllianceAttachmentDTO> attachments,Long ownerId) {
		if(null == attachments)
			return;
		for (ServiceAllianceAttachmentDTO dto:attachments ){
			if(null == dto.getContentUri())
				continue;
			ServiceAllianceAttachment attachment =  ConvertHelper.convert(dto,ServiceAllianceAttachment.class);
			attachment.setOwnerId(ownerId);
			attachment.setContentType(PostContentType.IMAGE.getCode());
			this.yellowPageProvider.createServiceAllianceAttachments(attachment);
		}
	}
	
	private void populateServiceAlliance(ServiceAlliances sa) { 
		this.yellowPageProvider.populateServiceAlliancesAttachment(sa);
		 
		populateServiceAllianceUrl(sa);
		populateServiceAllianceAttachements(sa,sa.getAttachments());
	 
		
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

}
