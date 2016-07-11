package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkUndoKeyProvider {

    Long createAclinkUndoKey(AclinkUndoKey obj);

    void updateAclinkUndoKey(AclinkUndoKey obj);

    void deleteAclinkUndoKey(AclinkUndoKey obj);

    AclinkUndoKey getAclinkUndoKeyById(Long id);

    List<AclinkUndoKey> queryAclinkUndoKeyByDoorId(ListingLocator locator, Long refId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<AclinkUndoKey> queryAclinkUndoKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

}
