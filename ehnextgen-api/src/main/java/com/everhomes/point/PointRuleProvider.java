// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.point.ListPointRulesCommand;
import com.everhomes.rest.point.PointRuleDTO;

import java.util.List;

public interface PointRuleProvider {

	void createPointRule(PointRule pointRule);

	void updatePointRule(PointRule pointRule);

    List<PointRule> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointRule findById(Long id);

    void createPointRules(List<PointRule> pointRules);

    List<PointRuleDTO> listPointRules(ListPointRulesCommand cmd, int pageSize, ListingLocator locator);

    // List<PointRule> listPointRuleByEventName(Integer namespaceId, Long systemId, String eventName);

    List<PointRule> listPointRuleByIds(List<Long> ruleIds);

    List<PointRule> listPointRuleByCategoryId(Long categoryId);
}