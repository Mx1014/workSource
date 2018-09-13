// @formatter:off
package com.everhomes.enterprise;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


import com.everhomes.organization.*;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.organization.OrganizationSiteApartmentDTO;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.collect.Lists;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.mysql.jdbc.StringUtils;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired 
    private UserActivityProvider userActivityProvider;
    
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
    EnterpriseContactService enterpriseContactService; 
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
	private ContentServerService contentServerService;
    
    @Autowired
	private DbProvider dbProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private OrganizationProvider organizationProvider;
    
    
    @Override
    public List<Enterprise> listEnterpriseByCommunityId(ListingLocator locator,String enterpriseName, Long communityId, Integer status, int pageSize) {
    	List<Enterprise> enterprises = new ArrayList<Enterprise>();
    	List<Enterprise> enters = new ArrayList<Enterprise>();
    	Long groupId = null;
    	if(!org.springframework.util.StringUtils.isEmpty(enterpriseName)){
    		Enterprise enterprise = new Enterprise();
    		enterprise.setName(enterpriseName);;
    		enters = enterpriseProvider.listEnterprisesByName(new CrossShardListingLocator(), 10000, enterprise);
    		if(enters.size() > 0)
    			groupId = enters.get(0).getId();
    	}
    	
    	Long enterpriseId = groupId;
    	
    	List<EnterpriseCommunityMap> enterpriseMaps = this.enterpriseProvider.queryEnterpriseMapByCommunityId(locator
                , communityId, pageSize, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.ne(EnterpriseCommunityMapStatus.INACTIVE.getCode()));
                if(null != enterpriseId)
                	query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.eq(enterpriseId));
                if(status != null) {
                    query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.eq(status.byteValue()));    
                }
                
                return query;
            }
        });    
    	
    	if(org.springframework.util.StringUtils.isEmpty(enterpriseName)){
    		for(EnterpriseCommunityMap cm : enterpriseMaps) {
	            Enterprise enterprise = enterpriseProvider.getEnterpriseById(cm.getMemberId());
	            if(enterprise != null) {
	            	enterprises.add(enterprise);
	            }
	            
    		}
    	}else{
    		if(enterpriseMaps.size() > 0 && enters.size() > 0){
    			enterprises.add(enters.get(0));
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
        List<Enterprise> enterprises = this.listEnterpriseByCommunityId(locator, cmd.getEnterpriseName(),cmd.getCommunityId(), cmd.getStatus(), pageSize);
        
        List<EnterpriseDTO> dtos = new ArrayList<EnterpriseDTO>();
        for(Enterprise enterprise : enterprises) {
             dtos.add(toEnterpriseDto(userId, enterprise));
        }
        
        ListEnterpriseResponse resp = new ListEnterpriseResponse();
        resp.setEnterprises(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }

    /**
     * 根据项目编号communityId来查询在该项目下的所有公司
     * @param cmd
     * @return
     */
    @Override
    public ListEnterprisesResponse listEnterprisesByCommunityId(ListEnterpriseByCommunityIdCommand cmd){
        List<OrganizationSiteApartmentDTO> organizationSiteApartmentDTOList = Lists.newArrayList();
        ListEnterprisesResponse response = new ListEnterprisesResponse();
        List<EnterprisePropertyDTO> newEnterprisePropertyDTOS = Lists.newArrayList();

        //首先需要进行非空校验
        if(cmd.getCommunityId() != null){
            //说明项目编号不为空，那么我们就根据该communityId进行查询eh_organization_workPlaces表中对应的所有的公司的id
            ListingLocator locator = new ListingLocator();
            //设置pageAnchor
            locator.setAnchor(cmd.getPageAnchor());
            int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
            List<EnterprisePropertyDTO> enterprisePropertyDTOS = organizationProvider.findEnterpriseListByCommunityId(locator,cmd.getCommunityId(),pageSize,cmd.getKeyword());

            //首先需要进行非空校验
            if(CollectionUtils.isNotEmpty(enterprisePropertyDTOS)){
                Map<Long, List<EnterprisePropertyDTO>> collect = enterprisePropertyDTOS.stream()
                        .collect(Collectors.groupingBy(EnterprisePropertyDTO::getOrganizationId));

                collect.forEach((k , v) -> {
                    EnterprisePropertyDTO dto = new EnterprisePropertyDTO();
                    dto.setName(v.iterator().next().getName());
                    dto.setOrganizationId(k);
                    dto.setId(v.iterator().next().getId());
                    if(v.iterator().next().getWholeAddressName() != null){
                        v.stream().map(EnterprisePropertyDTO::getWholeAddressName).reduce((r1, r2) -> r1+","+r2).ifPresent(dto::setWholeAddressName);
                        dto.setWholeAddressName(v.iterator().next().getWholeAddressName());
                    }else{
                        dto.setWholeAddressName("");
                    }
                    newEnterprisePropertyDTOS.add(dto);
                });


                //说明查询出来的集合List<EnterprisePropertyDTO>不为空，那么我们就进行遍历
                for(EnterprisePropertyDTO enterprisePropertyDTO : enterprisePropertyDTOS){
                    //得到每一个对象EnterprisePropertyDTO
                    //然后我们将对应的楼栋和门牌设置进去
                    //根据community_id来查询eh_communityAndBuilding_relationes表中的addressId信息
                    List<Long> addressIdList = Lists.newArrayList();
                    addressIdList = organizationProvider.getCommunityAndBuildingRelationesAddressIdsByCommunityId(cmd.getCommunityId());

                    if(CollectionUtils.isNotEmpty(addressIdList) && cmd.getNamespaceId() != null){
                        //说明查询出来的addressIdList集合不为空，那么我们就根据这些addressIdList来查询eh_addresses表中信息，从而得到对应的楼栋和门牌
                        List<Address> addressList = addressProvider.findAddressByIds(addressIdList,cmd.getNamespaceId());
                        if(CollectionUtils.isNotEmpty(addressList)){
                            for(Address address : addressList){
                                OrganizationSiteApartmentDTO organizationSiteApartmentDTO = new OrganizationSiteApartmentDTO();
                                organizationSiteApartmentDTO.setBuildingName(address.getBuildingName());
                                organizationSiteApartmentDTO.setApartmentName(address.getApartmentName());
                                organizationSiteApartmentDTOList.add(organizationSiteApartmentDTO);
                            }
                            enterprisePropertyDTO.setSiteDtos(organizationSiteApartmentDTOList);
                        }
                    }
                }
            }
        }
        response.setEnterprises(newEnterprisePropertyDTOS);
        return response;
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
    public List<CommunityDoc> searchCommunities(SearchEnterpriseCommunityCommand cmd) {
    	if(null == cmd.getCommunityType()){
    		cmd.setCommunityType(CommunityType.COMMERCIAL.getCode());
    	}
    	
        if(cmd.getKeyword() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword paramter.");
        }
        int pageNum = (cmd.getPageOffset() == null||cmd.getPageOffset()<=0) ? 1: cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? this.configProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        
        return communitySearcher.searchEnterprise(cmd.getKeyword(), cmd.getCommunityType(), cmd.getRegionId(), pageNum - 1, pageSize);
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
                    dto.setCommunityType(community.getCommunityType());
                    dto.setDefaultForumId(community.getDefaultForumId());
                    dto.setFeedbackForumId(community.getFeedbackForumId());
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
        
        String postUri = enterprise.getPostUri();
        if(postUri != null && postUri.length() > 0) {
        	try{
        		dto.setPostUri(postUri);
        		String url = contentServerService.parserUri(postUri, EntityType.GROUP.getCode(), enterprise.getId());
	           
        		dto.setPostUrl(url);
	       
			 }catch(Exception e){
	           
				 LOGGER.error("Failed to parse post uri, enterpriseId=" + enterprise.getId(), e);
	       
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
            	EnterpriseContact ec = this.enterpriseContactProvider.getContactById(entry.getContactId());
            	if(ec.getStatus() != GroupMemberStatus.INACTIVE.getCode() && ec.getUserId().equals(userId)) {
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
        }
        //List<Enterprise> enterpriseList = this.enterpriseProvider.queryEnterpriseByPhone(phone);
        
        return enterpriseList;
    }
    
    @Override
    public List<EnterpriseDTO> listUserRelatedEnterprises(ListUserRelatedEnterprisesCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Integer namespaceId = UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId());
//        Long communityId = cmd.getCommunityId();
//        List<Long> enterpriseIdList = listEnterpriseIdByCommunityId(communityId);
        
        List<UserGroup> userGroupList = userProvider.listUserGroups(user.getId(), GroupDiscriminator.ENTERPRISE.getCode());
        int size = (userGroupList == null) ? 0 : userGroupList.size();

        List<EnterpriseDTO> enterpriseList = new ArrayList<EnterpriseDTO>();
        if(size > 0) {
            Enterprise enterprise = null;
            EnterpriseDTO dto = null;
            for(UserGroup userGroup : userGroupList) {
                GroupMemberStatus status = GroupMemberStatus.fromCode(userGroup.getMemberStatus());
                if(status == GroupMemberStatus.INACTIVE) {
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("The group is filtered for in inactive member status, userId=" + userId 
                            + ", enterpriseId=" + userGroup.getGroupId() + ", memberStatus=" + status);
                    }
                    continue;
                }
                
//                if(communityId != null && !enterpriseIdList.contains(userGroup.getGroupId())) {
//                    if(LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("The group is filtered for not in the community, userId=" + userId 
//                            + ", enterpriseId=" + userGroup.getGroupId() + ", communityId=" + communityId);
//                    }
//                    continue;
//                }
                
                enterprise = this.enterpriseProvider.findEnterpriseById(userGroup.getGroupId());
                
                if(enterprise == null) {
                    if(LOGGER.isDebugEnabled()) {
                    	LOGGER.debug("The enterprise is not exist, userId=" + userId 
                          + ", enterpriseId=" + userGroup.getGroupId());
                    }
                  continue;
                }
                
                if(enterprise != null &&  !namespaceId.equals(enterprise.getNamespaceId())) {
	                if(LOGGER.isDebugEnabled()) {
	                	LOGGER.debug("The group is filtered for not in the namespaceId, userId=" + userId 
	                      + ", enterpriseId=" + userGroup.getGroupId() + ", namespaceId=" + namespaceId);
	              
	                }
                
	                continue;
                }	
                this.enterpriseProvider.populateEnterpriseAttachments(enterprise);
                this.enterpriseProvider.populateEnterpriseAddresses(enterprise);
                populateEnterprise(enterprise);
                
                // 为了使得企业信息里包含园区信息（特别是论坛信息），故需要给企业一个当前使用的园区
                enterprise.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
                enterprise.setVisibleRegionId(enterprise.getVisibleRegionId());
                dto = toEnterpriseDto(userId, enterprise);
                if(dto != null) {
                    enterpriseList.add(dto);
                }
            }
        }

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List user related enterprises, userId=" + userId + ", size=" + size 
                    + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return enterpriseList;
    }
    
    private List<Long> listEnterpriseIdByCommunityId(Long communityId) {
        List<Long> enterpriseIdList = new ArrayList<Long>();
        if(communityId != null) {
            List<EnterpriseCommunityMap> enterpriseMapList = listEnterpriseCommunityMapByCommunityId(communityId);
            if(enterpriseMapList != null) {
                for(EnterpriseCommunityMap map : enterpriseMapList) {
                    enterpriseIdList.add(map.getMemberId());
                }
            }
        }
        
        return enterpriseIdList;
    }
    
    private List<EnterpriseCommunityMap> listEnterpriseCommunityMapByCommunityId(Long communityId) {
        ListingLocator locator = new ListingLocator();
        List<EnterpriseCommunityMap> enterpriseMapList = this.enterpriseProvider.queryEnterpriseMapByCommunityId(locator, 
            communityId, Integer.MAX_VALUE - 1, (loc, query) -> {
            query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
            // 包含已经审核和待审核的
            query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.ne(EnterpriseCommunityMapStatus.INACTIVE.getCode()));
            return null;
        });
        
        return enterpriseMapList;
    }

    @Override
    public void requestToJoinCommunity(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = new EnterpriseCommunityMap();
        ec.setCommunityId(communityId);
        ec.setCreatorUid(admin.getId());
        ec.setMemberId(enterpriseId);
        ec.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
        ec.setMemberStatus(EnterpriseCommunityMapStatus.WAITING_FOR_APPROVAL.getCode());
        ec.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.enterpriseProvider.createEnterpriseCommunityMap(ec);
    }

    @Override
    public void approve(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = this.enterpriseProvider.findEnterpriseCommunityByEnterpriseId(enterpriseId, communityId);
        ec.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
        ec.setOperatorUid(admin.getId());
        this.enterpriseProvider.updateEnterpriseCommunityMap(ec);        
    }

    @Override
    public void reject(User admin, Long enterpriseId, Long communityId) {
        EnterpriseCommunityMap ec = this.enterpriseProvider.findEnterpriseCommunityByEnterpriseId(enterpriseId, communityId);
        ec.setMemberStatus(EnterpriseCommunityMapStatus.INACTIVE.getCode());
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
        ec.setMemberStatus(EnterpriseCommunityMapStatus.WAITING_FOR_ACCEPTANCE.getCode());
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
            	address.setStatus(EnterpriseAddressStatus.ACTIVE.getCode());

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
	public void setCurrentEnterprise(SetCurrentEnterpriseCommand cmd) { 
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		long timestemp = DateHelper.currentGMTTime().getTime();	
		validUserEnterprise(userId,cmd.getEnterpriseId());
		   
		String key = UserCurrentEntityType.ENTERPRISE.getUserProfileKey();
        userActivityProvider.updateUserCurrentEntityProfile(userId, key, cmd.getEnterpriseId(), timestemp, user.getNamespaceId());
	}

	public void validUserEnterprise(Long userId, Long enterpriseId) { 
		EnterpriseContact  contact = enterpriseContactProvider.queryContactByUserId(enterpriseId, userId);
		if(null==contact)
			throw RuntimeErrorException
			.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_USER_NOT_FOUND,
					"you are not in the enterprise !");
	}

	@Override
	public void updateContactor(UpdateContactorCommand cmd) {
		
		User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        EnterpriseContact enterCon = this.enterpriseContactProvider.queryEnterpriseContactor(cmd.getEnterpriseId());
        if(enterCon != null) {
        	enterCon.setRole(RoleConstants.ResourceUser);
        	enterCon.setStatus(EnterpriseContactStatus.ACTIVE.getCode());
        	this.enterpriseContactProvider.updateContact(enterCon);
        }

        UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(),cmd.getEntryValue());
		if(identifier == null) {
			//帮他在左邻注册一个账号
			identifier = this.dbProvider.execute((TransactionStatus status) -> {
				User newuser = new User();
				newuser.setStatus(UserStatus.ACTIVE.getCode());
				newuser.setNamespaceId(cmd.getNamespaceId());
				newuser.setAccountName(cmd.getContactName());
				newuser.setNickName(cmd.getContactName());
				newuser.setCommunityId(cmd.getCommunityId());
				newuser.setGender(UserGender.UNDISCLOSURED.getCode());
				String salt=EncryptionUtils.createRandomSalt();
				newuser.setSalt(salt);
				try {
					newuser.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s","8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92",salt)));
				} catch (Exception e) {
					LOGGER.error("encode password failed");
					throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

				}

				userProvider.createUser(newuser);

				UserIdentifier newIdentifier = new UserIdentifier();
				newIdentifier.setOwnerUid(newuser.getId());
				newIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
				newIdentifier.setIdentifierToken(cmd.getEntryValue());
				newIdentifier.setNamespaceId(cmd.getNamespaceId());

				newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
				userProvider.createIdentifier(newIdentifier);

				return newIdentifier;
			});
		}
		
		EnterpriseContactEntry entry = this.enterpriseContactProvider.getEnterpriseContactEntryByPhone(cmd.getEnterpriseId(), cmd.getEntryValue());
		if(entry == null) {
			EnterpriseContact ec = new EnterpriseContact();
			ec.setUserId(identifier.getOwnerUid());
    		ec.setCreatorUid(userId);
    		ec.setEnterpriseId(cmd.getEnterpriseId());
    		if(cmd.getContactName() != null) 
    			ec.setName(cmd.getContactName());
    		
    		ec.setRole(RoleConstants.ResourceAdmin);
    		ec.setStatus(EnterpriseContactStatus.ACTIVE.getCode());
    		Long contactId = this.enterpriseContactProvider.createContact(ec);
    		
    		EnterpriseContactEntry conentry = new EnterpriseContactEntry();
    		conentry.setEnterpriseId(ec.getEnterpriseId());
    		conentry.setCreatorUid(userId);
    		conentry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
    		conentry.setEntryValue(cmd.getEntryValue());
    		conentry.setContactId(contactId);
    		this.enterpriseContactProvider.createContactEntry(conentry);
    		enterpriseContactService.createOrUpdateUserGroup(ec);
    		
		} else {
			EnterpriseContact enterpriseContact = this.enterpriseContactProvider.queryContactById(entry.getContactId());
			enterpriseContact.setRole(RoleConstants.ResourceAdmin);
			enterpriseContact.setUserId(identifier.getOwnerUid());
			enterpriseContact.setStatus(EnterpriseContactStatus.ACTIVE.getCode());
			this.enterpriseContactProvider.updateContact(enterpriseContact);
			enterpriseContactService.createOrUpdateUserGroup(enterpriseContact);
		}
		
	}

	@Override
	public void deleteEnterprise(DeleteEnterpriseCommand cmd) {
		EnterpriseCommunityMap ec = this.enterpriseProvider.findEnterpriseCommunityByEnterpriseId(cmd.getCommunityId(), cmd.getEnterpriseId());
		if(ec != null) {
			ec.setMemberStatus(EnterpriseCommunityMapStatus.INACTIVE.getCode());
			this.enterpriseProvider.updateEnterpriseCommunityMap(ec);
	
			List<EnterpriseAddress> eas = this.enterpriseProvider.findEnterpriseAddressByEnterpriseId(cmd.getEnterpriseId());
			
			for(EnterpriseAddress ea : eas) {
				Address addr = addressProvider.findAddressById(ea.getAddressId());
				if(addr != null && addr.getCommunityId() == cmd.getCommunityId()) {
					ea.setStatus(EnterpriseAddressStatus.INACTIVE.getCode());
					this.enterpriseProvider.updateEnterpriseAddress(ea);
				}
	
			}
			
			this.enterpriseProvider.deleteEnterpriseAttachmentsByEnterpriseId(cmd.getEnterpriseId());
			
			List<Long> contactIds = this.enterpriseContactProvider.deleteContactByEnterpriseId(cmd.getEnterpriseId());
			this.enterpriseContactProvider.deleteContactEntryByContactId(contactIds);
			enterpriseSearcher.deleteById(cmd.getEnterpriseId());
		}
	}

	@Override
	public ImportDataResponse importEnterpriseData(MultipartFile mfile,
			Long userId, ImportEnterpriseDataCommand cmd) {
		ImportDataResponse importDataResponse = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
			
			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());
			//导入数据，返回导入错误的日志数据集
			List<String> errorDataLogs = importEnterprise(convertToStrList(resultList), userId, cmd);
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}else{
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}
			
			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}

	private List<String> convertToStrList(List list) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			sb.append(r.getF()).append("||");
			sb.append(r.getG()).append("||");
			sb.append(r.getH());
			result.add(sb.toString());
		}
		return result;
	}
	
	private List<String> importEnterprise(List<String> list, Long userId, ImportEnterpriseDataCommand cmd){
		List<String> errorDataLogs = new ArrayList<String>();
		
		Integer namespaceId = cmd.getNamespaceId() == null ? 0 : cmd.getNamespaceId();

		User user = UserContext.current().getUser();
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				Enterprise enterprise = new Enterprise();
				enterprise.setName(s[0]);
				enterprise.setDisplayName(s[1]);
				enterprise.setEnterpriseAddress(s[2]);;
				enterprise.setContactsPhone(s[3]);;
				
				enterprise.setDescription(s[7]);
				enterprise.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				
				enterprise.setCreatorUid(userId);
				
				enterprise.setNamespaceId(namespaceId);
				
				LOGGER.info("add enterprise");
				this.enterpriseProvider.createEnterprise(enterprise);
				requestToJoinCommunity(user, enterprise.getId(), org.getCommunityId());
				String contactName = s[5];
				String contactToken = s[6];
				UpdateContactorCommand command = new UpdateContactorCommand();
				command.setContactName(contactName);
				command.setEntryValue(contactToken);
				command.setEnterpriseId(enterprise.getId());
				//userId查communityId
				command.setCommunityId(org.getCommunityId());
				updateContactor(command);
				enterpriseSearcher.feedDoc(enterprise);
				return null;
			});
		}
		return errorDataLogs;
		
	}
	
	@Override
	public EnterpriseDTO findEnterpriseByAddress(Long addressId){
		Enterprise enterprise = enterpriseProvider.findEnterpriseByAddressId(addressId);
		return ConvertHelper.convert(enterprise, EnterpriseDTO.class);
	}

    /**
     * 查询该域空间下不在该项目中的所有企业
     * @param cmd
     * @return
     */
    @Override
	public ListEnterpriseResponse listEnterpriseNoReleaseWithCommunityId(listEnterpriseNoReleaseWithCommunityIdCommand cmd){
        //创建ListEnterpriseResponse类的对象
        ListEnterpriseResponse listEnterpriseResponse = new ListEnterpriseResponse();
        listEnterpriseResponse.setEnterprises(null);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        //创建分页的对象
        CrossShardListingLocator locator = new CrossShardListingLocator();
        //设置第几页
        locator.setAnchor(cmd.getPageAnchor());
        //进行非空校验
        if(cmd.getNamespaceId() != null && cmd.getCommunityId() != null){
            //说明传进来的参数不为空，那么我们首先需要根据这两个参数进行查询eh_communitys表中
            //在该域空间下不包含该项目当中的所有项目的编号，注意他会返回一个集合List<Long>
            List<Long> communityIdList = communityProvider.findOrganizationIdsByNamespaceId(cmd.getCommunityId(),cmd.getNamespaceId());
            //进行非空校验
            if(CollectionUtils.isNotEmpty(communityIdList)){
                //说明集合不为空，那么我们就根据该集合进行查询eh_organization_community_requests表，得到一个公司id的集合
                List<Integer> organizationIdList = organizationProvider.findOrganizationIdListByCommunityIdList(communityIdList);
                //进行非空校验
                if(CollectionUtils.isNotEmpty(organizationIdList)){
                    //说明集合不为空，那么我们就根据该公司编号的集合在eh_organizations表中查询对应的公司的信息，注意他会返回一个List<Organization>集合
                    List<EnterpriseDTO> organizationList = organizationProvider.findOrganizationsByOrgIdList(organizationIdList,cmd.getKeyword(),locator,pageSize);
                    if(CollectionUtils.isNotEmpty(organizationList)){
                        listEnterpriseResponse.setEnterprises(organizationList);
                        listEnterpriseResponse.setNextPageAnchor(locator.getAnchor());
                        return listEnterpriseResponse;
                    }
                }
            }
        }
        return listEnterpriseResponse;
    }

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param cmd
     */
    @Override
    public void deleteEnterpriseByOrgIdAndCommunityId(DeleteEnterpriseCommand cmd){
        //首先进行非空校验
        if(cmd.getEnterpriseId() != null && cmd.getCommunityId() != null){
            //说明传过来的参数不为空，那么我们就根据这两个参数来删除表eh_organization_workplaces中的对应的数据，同时删除表eh_enterprise_community_map
            //中的数据
            //这两个操作必须保持在同一个事务中进行
            dbProvider.execute((TransactionStatus status) -> {
                //创建一个OrganizationWorkPlaces类的对象
                OrganizationWorkPlaces organizationWorkPlaces = new OrganizationWorkPlaces();
                //将数据封装在对象OrganizationWorkPlaces中
                organizationWorkPlaces.setOrganizationId(cmd.getEnterpriseId());
                organizationWorkPlaces.setCommunityId(cmd.getCommunityId());
                //调用enterpriseProvider接口中的deleteEnterpriseByOrgIdAndCommunityId(OrganizationWorkPlaces organizationWorkPlaces)方法，
                //将表eh_organization_workPlaces中的数据进行删除
                enterpriseProvider.deleteEnterpriseByOrgIdAndCommunityId(organizationWorkPlaces);
                //接下来我们来删除表eh_enterprise_community_map表中的对应的数据
                //创建EnterpriseCommunityMap类的对象
                EnterpriseCommunityMap enterpriseCommunityMap = new EnterpriseCommunityMap();
                //将数据封装在EnterpriseCommunityMap对象中
                enterpriseCommunityMap.setMemberId(cmd.getEnterpriseId());
                enterpriseCommunityMap.setCommunityId(cmd.getCommunityId());
                //调用enterpriseProvider中的deleteEnterpriseFromEnterpriseCommunityMapByOrgIdAndCommunityId(EnterpriseCommunityMap enterpriseCommunityMap)
                //方法来删除表eh_enterprise_community_map中的数据
                enterpriseProvider.deleteEnterpriseFromEnterpriseCommunityMapByOrgIdAndCommunityId(enterpriseCommunityMap);
                return null;
            });
        }
    }
	
}
