package com.everhomes.acl;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclPrivilegeProvider {

	Long createAclPrivilege(AclPrivilege obj);

	void updateAclPrivilege(AclPrivilege obj);

	List<AclPrivilege> queryAclPrivileges(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void deleteAclPrivilege(AclPrivilege obj);

	AclPrivilege getAclPrivilegeById(Long id);

}
