// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointRuleConfigProvider {

	void createPointRuleConfig(PointRuleConfig pointRuleConfig);

	void updatePointRuleConfig(PointRuleConfig pointRuleConfig);

    List<PointRuleConfig> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointRuleConfig findById(Long id);

    void deleteBySystemId(Long systemId);

    PointRuleConfig findByRuleIdAndSystemId(Long systemId, Long ruleId);
}