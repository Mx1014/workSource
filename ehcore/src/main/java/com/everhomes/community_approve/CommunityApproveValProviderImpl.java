package com.everhomes.community_approve;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Component
public class CommunityApproveValProviderImpl implements CommunityApproveValProvider{

    @Override
    public Long createCommunityApproveVal(CommunityApproveVal obj) {
        return null;
    }

    @Override
    public void updateCommunityApproveVal(CommunityApproveVal obj) {

    }

    @Override
    public void deleteCommunityApproveVal(CommunityApproveVal obj) {

    }

    @Override
    public CommunityApproveVal getCommunityApproveValById(Long id) {
        return null;
    }

    @Override
    public List<CommunityApproveVal> queryCommunityApproves(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        return null;
    }
}
