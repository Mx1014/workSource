// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;
import java.util.Set;

public interface EnterpriseMomentProvider {

	void createEnterpriseMoment(EnterpriseMoment enterpriseMoment);

	void updateEnterpriseMoment(EnterpriseMoment enterpriseMoment);

	EnterpriseMoment findEnterpriseMomentById(Long id);

	List<EnterpriseMoment> listEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Long tagId, CrossShardListingLocator locator, int pageSize);

	List<EnterpriseMoment> listEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Set<Long> orgIds, Long tagId, CrossShardListingLocator locator, int pageSize);

	void deleteEnterpriseMoment(Long momentId);

	void incrCommentCount(Long id, Integer namespaceId, Long organizationId, int incr);

	void incrLikeCount(Long id, Integer namespaceId, Long organizationId, int incr);

	void updateEnterpriseMomentTagName(Integer namespaceId, Long organizationId, Long tagId, String tagName);

	Integer countAllStatusEnterpriseMomentByOrganization(Integer namespaceId, Long organizationId);
}