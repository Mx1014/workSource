package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.SearchCommunityCommand;
import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDoc;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessageMetaConstant;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
import com.everhomes.region.RegionProvider;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.search.GroupQueryResult;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.visibility.VisibleRegionType;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {

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
        
        return enterprises;
    }
    
    @Override
    public ListEnterpriseResponse listEnterpriseByCommunityId(ListEnterpriseByCommunityIdCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Enterprise> enterprises = this.listEnterpriseByCommunityId(locator, cmd.getCommunityId(), cmd.getStatus(), pageSize);
       
        List<EnterpriseDTO> dtos = new ArrayList<EnterpriseDTO>();
        for(Enterprise enterprise : enterprises) {
             dtos.add(ConvertHelper.convert(enterprise, EnterpriseDTO.class));
        }
        
        ListEnterpriseResponse resp = new ListEnterpriseResponse();
        resp.setEnterprises(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public Enterprise getEnterpriseById(Long id) {
        return this.enterpriseProvider.getEnterpriseById(id);
    }
    
    @Override
    public void createEnterprise(Enterprise enterprise) {
        enterprise.setCreatorUid(UserContext.current().getUser().getId());
        this.enterpriseProvider.createEnterprise(enterprise);
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

}
