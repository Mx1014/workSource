package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.AclinkQueryLogResponse;

public interface AclinkLogProvider {

    Long createAclinkLog(AclinkLog obj);

    void updateAclinkLog(AclinkLog obj);

    void deleteAclinkLog(AclinkLog obj);

    AclinkLog getAclinkLogById(Long id);

    List<AclinkLog> queryAclinkLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<AclinkLog> queryAclinkLogsByTime(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
