package com.everhomes.community_approve;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface CommunityApproveProvider {

    Long createCommunityApprove(CommunityApprove obj);

    void updateCommunityApprove(CommunityApprove obj);

    void deleteCommunityApprove(CommunityApprove obj);

    CommunityApprove getCommunityApproveById(Long id);

    List<CommunityApprove> queryCommunityApproves(ListingLocator locator,
                                                  int count, ListingQueryBuilderCallback queryBuilderCallback);
}
