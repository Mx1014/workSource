package com.everhomes.community_approve;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface CommunityApproveValProvider {

    Long createCommunityApproveVal(CommunityApproveVal obj);

    void updateCommunityApproveVal(CommunityApproveVal obj);

    void deleteCommunityApproveVal(CommunityApproveVal obj);

    CommunityApproveVal getCommunityApproveValById(Long id);

    List<CommunityApproveVal> queryCommunityApproves(ListingLocator locator,
                                                  int count, ListingQueryBuilderCallback queryBuilderCallback);
}
