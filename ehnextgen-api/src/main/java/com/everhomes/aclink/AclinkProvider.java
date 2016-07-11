package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkProvider {

    Long createAclink(Aclink obj);

    void updateAclink(Aclink obj);

    void deleteAclink(Aclink obj);

    Aclink getAclinkById(Long id);

    List<Aclink> queryAclinks(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<Aclink> queryAclinkByDoorId(ListingLocator locator, Long refId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    Aclink getAclinkByDoorId(Long doorId);

}
