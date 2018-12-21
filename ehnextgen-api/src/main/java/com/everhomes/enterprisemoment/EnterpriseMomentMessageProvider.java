// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.Collection;
import java.util.List;

public interface EnterpriseMomentMessageProvider {

	void createEnterpriseMomentMessage(EnterpriseMomentMessage enterpriseMomentMessage);

	List<Long> findMessageReceiverListBySourceId(Integer namespaceId, Long organizationId, Long momentId, String sourceType, Long operatorId, Collection<Long> receiverIds);

	List<EnterpriseMomentMessage> listEnterpriseMomentMessage(Integer currentNamespaceId, Long organizationId, Long aLong, CrossShardListingLocator locator, int pageSize);

	void markSourceDeleteBySourceId(Integer namespaceId, Long organizationId, Long momentId, String sourceType, Long sourceId);
}