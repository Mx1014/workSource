package com.everhomes.community_approve;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface CommunityApproveValProvider {

    Long createCommunityApproveVal(CommunityApproveRequests obj);

    void updateCommunityApproveVal(CommunityApproveRequests obj);

    void deleteCommunityApproveVal(CommunityApproveRequests obj);

    CommunityApproveRequests getCommunityApproveValById(Long id);

    CommunityApproveRequests getCommunityApproveValByFlowCaseId(Long id);

    List<CommunityApproveRequests> queryCommunityApproves(ListingLocator locator,
                                                          int count, ListingQueryBuilderCallback queryBuilderCallback);
}
