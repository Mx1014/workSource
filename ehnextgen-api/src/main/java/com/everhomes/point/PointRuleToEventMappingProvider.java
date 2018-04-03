// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointRuleToEventMappingProvider {

	void createPointRuleToEventMapping(PointRuleToEventMapping pointRuleToEventMapping);

	void updatePointRuleToEventMapping(PointRuleToEventMapping pointRuleToEventMapping);

    List<PointRuleToEventMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointRuleToEventMapping findById(Long id);

    List<PointRuleToEventMapping> listByPointRule(Long pointRuleId);

    void createPointRuleToEventMappings(List<PointRuleToEventMapping> mappings);

    List<PointRuleToEventMapping> listByEventName(String eventName);
}