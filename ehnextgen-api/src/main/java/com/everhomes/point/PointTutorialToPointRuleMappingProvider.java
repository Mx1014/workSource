// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointTutorialToPointRuleMappingProvider {

	void createPointTutorialToPointRuleMapping(PointTutorialToPointRuleMapping pointTutorialToPointRuleMapping);

	void updatePointTutorialToPointRuleMapping(PointTutorialToPointRuleMapping pointTutorialToPointRuleMapping);

    List<PointTutorialToPointRuleMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointTutorialToPointRuleMapping findById(Long id);

    List<PointTutorialToPointRuleMapping> listMappings(Long tutorialId, int pageSize, ListingLocator locator);
}