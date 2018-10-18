package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AllianceTagProvider {

	void createAllianceTag(AllianceTag headTag);

	void updateAllianceTag(AllianceTag headTag);

	List<AllianceTag> getAllianceParentTagList(ListingLocator locator, Integer pageSize, Integer namespaceId,
			String ownerType, Long ownerId, Long type);

	List<AllianceTag> getAllianceChildTagList(Integer namespaceId, Long type, Long id);
	
	List<AllianceTag> listAllianceTags(Integer pageSize, ListingLocator locator, ListingQueryBuilderCallback callback);

	void deleteProjectTags(Long projectId, Long type);
}
