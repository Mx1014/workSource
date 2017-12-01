// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.point.ListPointRulesCommand;

import java.util.List;

public interface PointRuleProvider {

	void createPointRule(PointRule pointRule);

	void updatePointRule(PointRule pointRule);

    List<PointRule> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointRule findById(Long id);

    List<PointRule> listPointRuleBySystemId(Long systemId, Integer pageSize, ListingLocator locator);

    List<PointRule> listPointRuleByCategoryId(Long categoryId, Integer pageSize, ListingLocator locator);

    void createPointRules(List<PointRule> pointRules);

    List<PointRule> listPointRules(ListPointRulesCommand cmd, int pageSize, ListingLocator locator);

    List<PointRule> listPointRuleByEventName(Integer namespaceId, Long systemId, String eventName);
}