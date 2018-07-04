package com.everhomes.launchpad;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface CommunityBizProvider {

    Long createCommunityBiz(CommunityBiz obj);

    void updateCommunityBiz(CommunityBiz obj);

    void deleteCommunityBiz(Long id);

    CommunityBiz getCommunityBizById(Long id);

    CommunityBiz findCommunityBiz(Long organizationId, Long communityId, Byte status);

}
