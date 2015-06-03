// @formatter:off
package com.everhomes.community;


import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.CommunityAdminStatus;
import com.everhomes.address.CommunityDTO;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;


@Component
public class CommunityServiceImpl implements CommunityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    
    @Override
    public ListWaitingForCommunitesCommandResponse listWaitingForApproveCommunities(ListWaitingForCommunitesCommand cmd) {
        
        if(cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Community> communities = this.communityProvider.listWaitingForApproveCommunities(locator, pageSize + 1,
                (loc, query) -> {
                    Condition c = Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.CONFIRMING.getCode());
                    query.addConditions(c);
                    return query;
                });
        
        Long nextPageAnchor = null;
        if(communities != null && communities.size() > pageSize) {
            communities.remove(communities.size() - 1);
            nextPageAnchor = communities.get(communities.size() -1).getId();
        }
        ListWaitingForCommunitesCommandResponse response = new ListWaitingForCommunitesCommandResponse();
        response.setNextPageAnchor(nextPageAnchor);
        
        List<CommunityDTO> communityDTOs = communities.stream().map((c) ->{
            return ConvertHelper.convert(c, CommunityDTO.class);
        }).collect(Collectors.toList());
        
        response.setRequests(communityDTOs);
        return response;
    }

   
}
