// @formatter:off
package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RoleConstants;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDoc;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.AttachmentDescriptor;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.region.RegionProvider;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.search.GroupQueryResult;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.visibility.VisibleRegionType;
import com.mysql.jdbc.StringUtils;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    EnterpriseProvider enterpriseProvider;
   
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CommunitySearcher communitySearcher;
    
    @Autowired
    EnterpriseSearcher enterpriseSearcher;
    
    @Autowired
    MessagingService messagingService;
    
    @Autowired
    LocaleTemplateService localeTemplateService;
    
    @Autowired
    CommunityProvider communityProvider;
    
    @Autowired
    RegionProvider regionProvider;
    
    @Autowired 
    EnterpriseContactProvider enterpriseContactProvider; 
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
	private ContentServerService contentServerService;
    
    @Override
    public List<Enterprise> listEnterpriseByCommunityId(ListingLocator locator, Long communityId, Integer status, int pageSize) {
        List<EnterpriseCommunityMap> enterpriseMaps = this.enterpriseProvider.queryEnterpriseMapByCommunityId(locator
                , communityId, pageSize, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
                if(status != null) {
                    query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.eq(status.byteValue()));    
                }
                
                return query;
            }
        });    
        
        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        for(EnterpriseCommunityMap cm : enterpriseMaps) {
            Enterprise enterprise = enterpriseProvider.getEnterpriseById(cm.getMemberId());
            if(enterprise != null) {
                enterprises.add(enterprise);
            }
            
        }
       
        this.enterpriseProvider.populateEnterpriseAttachments(enterprises);
        this.enterpriseProvider.populateEnterpriseAddresses(enterprises);
        populateEnterprises(enterprises);
        
        return enterprises;
    }
    
    @Override
    public ListEnterpriseResponse listEnterpriseByCommunityId(ListEnterpriseByCommunityIdCommand cmd) {
        
    	User user = UserContext.current().getUser();
        Long userId = user.getId();
        
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Enterprise> enterprises = this.listEnterpriseByCommunityId(locator, cmd.getCommunityId(), cmd.getStatus(), pageSize);
        
        populateEnterprises(enterprises);
       
        List<EnterpriseDTO> dtos = new ArrayList<EnterpriseDTO>();
        for(Enterprise enterprise : enterprises) {
             dtos.add(toEnterpriseDto(userId, enterprise));
        }
        
        ListEnterpriseResponse resp = new ListEnterpriseResponse();
        resp.setEnterprises(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public Enterprise getEnterpriseById(Long id) {
    	
    	Enterprise enterprise = this.enterpriseProvider.getEnterpriseById(id);
    	
    	this.enterpriseProvider.populateEnterpriseAttachments(enterprise);
    	this.enterpriseProvider.populateEnterpriseAddresses(enterprise);
        populateEnterprise(enterprise);
        
        return enterprise;
    }
    
    @Override
    public EnterpriseDTO createEnterprise(CreateEnterpriseCommand cmd) {
    	
    	User user = UserContext.current().getUser();
        Long userId = user.getId();
        
    	Enterprise enterprise = new Enterprise();
        enterprise.setCreatorUid(userId);
        enterprise.setAvatar(cmd.getAvatar());
        enterprise.setDescription(cmd.getDescription());
        enterprise.setDisplayName(cmd.getDisplayName());
        enterprise.setName(cmd.getName());
        enterprise.setMemberCount(cmd.getMemberCount());
        enterprise.setContactsPhone(cmd.getContactsPhone());
        this.enterpriseProvider.createEnterprise(enterprise);
        
        if(cmd.getEntries() != null) {
        	
    		EnterpriseContact ec = new EnterpriseContact();
    		ec.setCreatorUid(userId);
    		ec.setEnterpriseId(enterprise.getId());
    		if(cmd.getContactor() != null) 
    			ec.setName(cmd.getContactor());
    		
    		ec.setRole(RoleConstants.SystemAdmin);
    		Long contactId = this.enterpriseContactProvider.createContact(ec);
    		
    		EnterpriseContactEntry entry = new EnterpriseContactEntry();
    		entry.setEnterpriseId(ec.getEnterpriseId());
    		entry.setCreatorUid(userId);
    		entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
    		entry.setEntryValue(cmd.getEntries());
    		entry.setContactId(contactId);
    		this.enterpriseContactProvider.createContactEntry(entry);
    		
    		enterprise.setEntries(entry.getEntryValue());
    		enterprise.setContactor(ec.getName());
        }
        
        requestToJoinCommunity(user, enterprise.getId(), cmd.getCommunityId());
        
        List<Long> addressIds = cmd.getAddressId();
        if(addressIds != null && addressIds.size() > 0) {
        	List<Address> address = new ArrayList<Address>();
        	for(Long addressId :addressIds) {
        		if(addressId != null) {
	        		EnterpriseAddress enterpriseAddr = new EnterpriseAddress();
	                enterpriseAddr.setAddressId(addressId);
	                enterpriseAddr.setEnterpriseId(enterprise.getId());
	                enterpriseAddr.setCreatorUid(userId);
	                enterpriseAddr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                enterpriseAddr.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                enterpriseAddr.setStatus(EnterpriseAddressStatus.WAITING_FOR_APPROVAL.getCode());
	
	                this.enterpriseProvider.createEnterpriseAddress(enterpriseAddr);
	                
	                Address addr = this.addressProvider.findAddressById(addressId);
	                if(addr != null)
	                	address.add(addr);
        		}
        	}
        	enterprise.setAddress(address);
        }
        
        
        
        processEnterpriseAttachments(userId, cmd.getAttachments(), enterprise);
        
        EnterpriseDTO enterpriseDto = toEnterpriseDto(userId, enterprise);
        
        
        return enterpriseDto;
    }
    
    @Override
    public List<CommunityDoc> searchCommunities(SearchEnterpriseCommunityCommand cmd) {
        if(cmd.getKeyword() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword paramter.");
        }
        int pageNum = (cmd.getPageOffset() == null||cmd.getPageOffset()<=0) ? 1: cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? this.configProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        
        return communitySearcher.searchEnterprise(cmd.getKeyword(), cmd.getRegionId(), pageNum - 1, pageSize);
    }
    
    @Override
    public ListEnterpriseResponse searchEnterprise(SearchEnterpriseCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = (user == null) ? -1L : user.getId();
        
        ListEnterpriseResponse resp = new ListEnterpriseResponse();
        GroupQueryResult rlt = this.enterpriseSearcher.query(cmd);
        resp.setNextPageAnchor(rlt.getPageAnchor());
        List<EnterpriseDTO> ents = new ArrayList<EnterpriseDTO>();
        for(Long id : rlt.getIds()) {
            Enterprise enterprise = this.enterpriseProvider.getEnterpriseById(id);
            
            this.enterpriseProvider.populateEnterpriseAttachments(enterprise);
            this.enterpriseProvider.populateEnterpriseAddresses(enterprise);
            populateEnterprise(enterprise);
            
            EnterpriseDTO dto = toEnterpriseDto(userId, enterprise);
            if(dto != null) {
                ents.add(dto);
            }
        }
        
        resp.setEnterprises(ents);
        return resp;
    }
    
    private List<EnterpriseDTO> toEnterpriseDtos(Long userId, List<Enterprise> enterpriseList) {
        List<EnterpriseDTO> result = new ArrayList<EnterpriseDTO>();
        
        EnterpriseDTO dto = null;
        for(Enterprise enterprise : enterpriseList) {
            dto = toEnterpriseDto(userId, enterprise);
            if(dto != null) {
                result.add(dto);
            }
        }
        
        return result;
    }
    
    private EnterpriseDTO toEnterpriseDto(Long userId, Enterprise enterprise) {
        if(enterprise == null) {
            return null;
        }
        
        EnterpriseDTO dto = ConvertHelper.convert(enterprise, EnterpriseDTO.class);
        dto.setContactCount(enterprise.getMemberCount());
        dto.setContactStatus(enterprise.getStatus());
        
        VisibleRegionType regionType = VisibleRegionType.fromCode(enterprise.getVisibleRegionType());
        if(regionType == VisibleRegionType.COMMUNITY) {
            Long communityId = enterprise.getVisibleRegionId();
            if(communityId != null) {
                Community community = communityProvider.findCommunityById(communityId);
                if(community != null) {
                    dto.setCommunityId(communityId);
                    dto.setCommunityName(community.getName());
                    dto.setAreaId(community.getAreaId());
                    dto.setAreaName(community.getAreaName());
                    dto.setCityId(community.getCityId());
                    dto.setCityName(community.getCityName());
                }
            }
        }
        
        EnterpriseContact contact = enterpriseContactProvider.queryContactByUserId(dto.getId(), userId);
        if(contact != null) {
            String contactNickName = contact.getNickName();
            if(contactNickName == null) {
                contactNickName = contact.getName();
            }
            dto.setContactNickName(contactNickName);
            
            dto.setContactOf((byte)1);
            dto.setContactStatus(contact.getStatus());
            dto.setContactRole(contact.getRole());
        }
        
        String avatarUri = enterprise.getAvatar();
		 if(avatarUri != null && avatarUri.length() > 0) {
			 try{
             
				 dto.setAvatarUri(avatarUri);
				 String url = contentServerService.parserUri(avatarUri, EntityType.GROUP.getCode(), enterprise.getId());
               
				 dto.setAvatarUrl(url);
               
           
			 }catch(Exception e){
               
				 LOGGER.error("Failed to parse avatar uri, enterpriseId=" + enterprise.getId(), e);
           
			 }
		 }
		 
		 EnterpriseContact contactor = this.enterpriseContactProvider.queryEnterpriseContactor(dto.getId());
		 if(contactor != null) {
			 String contactorName = contactor.getName();
			 dto.setContactor(contactorName);
			 
			 List<EnterpriseContactEntry> ece = this.enterpriseContactProvider.queryContactEntryByContactId(contactor);
			 if(ece != null && ece.size() > 0)
				 dto.setEntries(ece.get(0).getEntryValue());
		 }
        
        return dto;
    }
    
    @Override
    public List<EnterpriseCommunity> listEnterpriseEnrollCommunties(CrossShardListingLocator locator, Long enterpriseId, int pageSize) {
        List<EnterpriseCommunityMap> ms = this.enterpriseProvider.queryEnterpriseCommunityMap(locator, pageSize, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.eq(enterpriseId));
                return query;
            }
            
        });
        
        List<EnterpriseCommunity> ecs = new ArrayList<EnterpriseCommunity>();
        for(EnterpriseCommunityMap m : ms) {
            EnterpriseCommunity ec = this.enterpriseProvider.getEnterpriseCommunityById(m.getCommunityId());
            if(null != ec) {
                ecs.add(ec);
            }
        }
        
        return ecs;
    }

    @Override
    public ListEnterpriseCommunityResponse listEnterpriseEnrollCommunties(GetEnterpriseInfoCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        
        int pageSize = 100;
        List<EnterpriseCommunity> ecs = this.listEnterpriseEnrollCommunties(locator, cmd.getEnterpriseId(), pageSize);
        List<EnterpriseCommunityDTO> dtos = new ArrayList<EnterpriseCommunityDTO>();
        for(EnterpriseCommunity ec : ecs) {
            dtos.add(ConvertHelper.convert(ec, EnterpriseCommunityDTO.class));
        }
        ListEnterpriseCommunityResponse resp = new ListEnterpriseCommunityResponse();
        resp.setEnterpriseCommunities(dtos);
        return resp;
    }
    
    @Override
    public EnterpriseCommunity getEnterpriseCommunityById(Long id) {
        return this.enterpriseProvider.getEnterpriseCommunityById(id);
    }
    
    @Override
    public List<EnterpriseDTO> listEnterpriseByPhone(String phone) {
        User user = UserContext.current().getUser();
        Long userId = (user == null) ? -1L : user.getId();
        
        byte entryType = EnterpriseContactEntryType.Mobile.getCode();
        List<EnterpriseContactEntry> entryList = this.enterpriseContactProvider.queryEnterpriseContactEntries(entryType, phone);
        
        List<EnterpriseDTO> enterpriseList = new ArrayList<EnterpriseDTO>();
        if(entryList != null) {
            Enterprise enterprise = null;
            EnterpriseDTO dto = null;
            for(EnterpriseContactEntry entry : entryList) {
                enterprise = this.enterpriseProvider.findEnterpriseById(entry.getEnterpriseId());
                
                this.enterpriseProvider.populateEnterpriseAttachments(enterprise);
                this.enterpriseProvider.populateEnterpriseAddresses(enterprise);
                populateEnterprise(enterprise);
                
                dto = toEnterpriseDto(userId, enterprise);
                if(dto != null) {
                    enterpriseList.add(dto);
                }
            }
        }
        //List<Enterprise> enterpriseList = this.enterpriseProvider.queryEnterpriseByPhone(phone);
        
        return enterpriseList;
    }

    @Override
    public void requestToJoinCommunity(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = new EnterpriseCommunityMap();
        ec.setCommunityId(communityId);
        ec.setCreatorUid(admin.getId());
        ec.setMemberId(enterpriseId);
        ec.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
        ec.setMemberStatus(EnterpriseCommunityMapStatus.Approving.getCode());
        ec.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.enterpriseProvider.createEnterpriseCommunityMap(ec);
    }

    @Override
    public void approve(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = this.enterpriseProvider.findEnterpriseCommunityByEnterpriseId(enterpriseId, communityId);
        ec.setMemberStatus(EnterpriseCommunityMapStatus.Approved.getCode());
        ec.setOperatorUid(admin.getId());
        this.enterpriseProvider.updateEnterpriseCommunityMap(ec);        
    }

    @Override
    public void reject(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = this.enterpriseProvider.findEnterpriseCommunityByEnterpriseId(enterpriseId, communityId);
        ec.setMemberStatus(EnterpriseCommunityMapStatus.Inactive.getCode());
        ec.setOperatorUid(admin.getId());
        this.enterpriseProvider.updateEnterpriseCommunityMap(ec);        
    }

    @Override
    public void revoke(User admin, Long enterpriseId, Long communityId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inviteToJoinCommunity(Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = new EnterpriseCommunityMap();
        ec.setCommunityId(communityId);
        ec.setCreatorUid(UserContext.current().getUser().getId());
        ec.setMemberId(enterpriseId);
        ec.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
        ec.setMemberStatus(EnterpriseCommunityMapStatus.Inviting.getCode());
        ec.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.enterpriseProvider.createEnterpriseCommunityMap(ec);
    }

    @Override
    public void importToJoinCommunity(List<Enterprise> enterprises, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }
    
    private void processEnterpriseAddresses(long userId, List<Long> addressIds, Enterprise enterprise) {
        List<Address> results = null;
        
        if(addressIds != null) {
            results = new ArrayList<Address>();
            
            EnterpriseAddress address = null;
            for(Long addressId : addressIds) {
            	address = new EnterpriseAddress();
            	address.setAddressId(addressId);
            	address.setEnterpriseId(enterprise.getId());
            	address.setCreatorUid(userId);
            	address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            	address.setStatus(EnterpriseAddressStatus.WAITING_FOR_APPROVAL.getCode());

                try {
                    this.enterpriseProvider.createEnterpriseAddress(address);
                    Address addr = this.addressProvider.findAddressById(addressId);
                    results.add(addr);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the address, userId=" + userId 
                        + ", address=" + address, e);
                }
            }
            
            enterprise.setAddress(results);
            
        }
    }
    
    private void processEnterpriseAttachments(long userId, List<AttachmentDescriptor> attachmentList, Enterprise enterprise) {
        List<EnterpriseAttachment> results = null;
        
        this.enterpriseProvider.deleteEnterpriseAttachmentsByEnterpriseId(enterprise.getId());
        if(attachmentList != null) {
            results = new ArrayList<EnterpriseAttachment>();
            
            EnterpriseAttachment attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
            	if(!StringUtils.isNullOrEmpty(descriptor.getContentUri())) {
            		
	                attachment = new EnterpriseAttachment();
	                attachment.setCreatorUid(userId);
	                attachment.setEnterpriseId(enterprise.getId());
	                attachment.setContentType(descriptor.getContentType());
	                attachment.setContentUri(descriptor.getContentUri());
	                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                
	                // Make sure we can save as many attachments as possible even if any of them failed
	                try {
	                    this.enterpriseProvider.createEnterpriseAttachment(attachment);
	                    results.add(attachment);
	                } catch(Exception e) {
	                    LOGGER.error("Failed to save the attachment, userId=" + userId 
	                        + ", attachment=" + attachment, e);
	                }
        		} 
            }
            
            enterprise.setAttachments(results);
            
            
        }
    }
    
    private void populateEnterprises(List<Enterprise> enterpriseList) {
        if(enterpriseList == null || enterpriseList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The enterprise list is empty");
            }
            return;
        } else {
            for(Enterprise enterprise : enterpriseList) {
            	populateEnterprise(enterprise);
            }
        }
    }

	/**
	 * 填充楼栋信息
	 */
	 private void populateEnterprise(Enterprise enterprise) {
		 
		 if(enterprise == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The enterprise is null");
            }
		 } else {
             
			 populateEnterpriseAttachements(enterprise, enterprise.getAttachments());
	        
		 }
	 }
	 
	 private void populateEnterpriseAttachements(Enterprise enterprise, List<EnterpriseAttachment> attachmentList) {
		 
		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The enterprise attachment list is empty, enterpriseId=" + enterprise.getId());
	            }
		 } else {
	            for(EnterpriseAttachment attachment : attachmentList) {
	                populateEnterpriseAttachement(enterprise, attachment);
	            }
		 }
	 }
	 
	 private void populateEnterpriseAttachement(Enterprise enterprise, EnterpriseAttachment attachment) {
        
		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The enterprise attachment is null, enterpriseId=" + enterprise.getId());
			 }
		 } else {
			 
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
                  
					 String url = contentServerService.parserUri(contentUri, EntityType.GROUP.getCode(), enterprise.getId());
                    
					 attachment.setContentUrl(url);
                    
                
				 }catch(Exception e){
                    
					 LOGGER.error("Failed to parse attachment uri, enterpriseId=" + enterprise.getId() + ", attachmentId=" + attachment.getId(), e);
                
				 }
            
			 } else {
             
				 if(LOGGER.isWarnEnabled()) {
                 
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
                
				 }
            
			 }
        
		 }
    
	 }

	@Override
	public EnterpriseDTO updateEnterprise(UpdateEnterpriseCommand cmd) {
		User user = UserContext.current().getUser();
        Long userId = user.getId();
        
    	Enterprise enterprise = new Enterprise();
    	enterprise.setId(cmd.getId());
        enterprise.setAvatar(cmd.getAvatar());
        enterprise.setDescription(cmd.getDescription());
        enterprise.setDisplayName(cmd.getDisplayName());
        enterprise.setName(cmd.getName());
        enterprise.setMemberCount(cmd.getMemberCount());
        enterprise.setContactsPhone(cmd.getContactsPhone());
        this.enterpriseProvider.updateEnterprise(enterprise);
        
        List<Long> contactIds = this.enterpriseContactProvider.deleteContactByEnterpriseId(cmd.getId());
        this.enterpriseContactProvider.deleteContactEntryByContactId(contactIds);
        if(cmd.getEntries() != null) {
        		EnterpriseContact ec = new EnterpriseContact();
        		ec.setCreatorUid(userId);
        		ec.setEnterpriseId(enterprise.getId());
        		if(cmd.getContactor() != null) 
        			ec.setName(cmd.getContactor());
        		
        		ec.setRole(RoleConstants.SystemAdmin);
        		Long contactId = this.enterpriseContactProvider.createContact(ec);
        		
        		EnterpriseContactEntry entry = new EnterpriseContactEntry();
        		entry.setEnterpriseId(ec.getEnterpriseId());
        		entry.setCreatorUid(userId);
        		entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
        		entry.setEntryValue(cmd.getEntries());
        		entry.setContactId(contactId);
        		this.enterpriseContactProvider.createContactEntry(entry);
        		
        		enterprise.setEntries(entry.getEntryValue());
        		enterprise.setContactor(ec.getName());
        	
        }
        
        List<Long> addressIds = cmd.getAddressId();
        if(addressIds != null && addressIds.size() > 0) {
        	List<Address> address = new ArrayList<Address>();
        	for(Long addressId :addressIds) {
        		if(addressId != null) {
        			if(this.enterpriseProvider.isExistInEnterpriseAddresses(cmd.getId(), addressId)) {
        			
		        		EnterpriseAddress enterpriseAddr = new EnterpriseAddress();
		                enterpriseAddr.setAddressId(addressId);
		                enterpriseAddr.setEnterpriseId(enterprise.getId());
		                enterpriseAddr.setCreatorUid(userId);
		                enterpriseAddr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		                enterpriseAddr.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		                enterpriseAddr.setStatus(EnterpriseAddressStatus.WAITING_FOR_APPROVAL.getCode());
		
		                this.enterpriseProvider.createEnterpriseAddress(enterpriseAddr);
        			}
	                
	                Address addr = this.addressProvider.findAddressById(addressId);
	                if(addr != null)
	                	address.add(addr);
        		}
        	}
        	enterprise.setAddress(address);
        }
        
        
        
        processEnterpriseAttachments(userId, cmd.getAttachments(), enterprise);
        
        EnterpriseDTO enterpriseDto = toEnterpriseDto(userId, enterprise);
        
        
        return enterpriseDto;
	}

}
