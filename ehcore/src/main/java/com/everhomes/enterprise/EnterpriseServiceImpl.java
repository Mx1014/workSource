package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.SearchCommunityCommand;
import com.everhomes.community.CommunityDoc;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    EnterpriseProvider enterpriseProvider;
   
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CommunitySearcher communitySearcher;
    
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
    public List<CommunityDoc> searchCommunities(SearchEnterpriseCommunityCommand cmd) {
        if(cmd.getKeyword() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword paramter.");
        }
        int pageNum = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? this.configProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        
        return communitySearcher.searchEnterprise(cmd.getKeyword(), cmd.getRegionId(), pageNum - 1, pageSize);
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
    public List<Enterprise> listEnterpriseByPhone(String phone) {
        return this.enterpriseProvider.queryEnterpriseByPhone(phone);
    }

    @Override
    public void requestToJoinCommunity(Enterprise enterprise, Long communtyId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void approve(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reject(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void revoke(User admin, Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inviteToJoinCommunity(Enterprise enterprise, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void importToJoinCommunity(List<Enterprise> enterprises, EnterpriseCommunityDTO community) {
        // TODO Auto-generated method stub
        
    }

}
