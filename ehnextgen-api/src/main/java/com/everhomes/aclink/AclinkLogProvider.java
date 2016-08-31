package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkLogProvider {

    Long createAclinkLog(AclinkLog obj);

    void updateAclinkLog(AclinkLog obj);

    void deleteAclinkLog(AclinkLog obj);

    AclinkLog getAclinkLogById(Long id);

    List<AclinkLog> queryAclinkLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

}
