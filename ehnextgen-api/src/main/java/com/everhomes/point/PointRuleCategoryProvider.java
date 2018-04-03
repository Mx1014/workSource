// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointRuleCategoryProvider {

	void createPointRuleCategory(PointRuleCategory pointRuleCategory);

	void updatePointRuleCategory(PointRuleCategory pointRuleCategory);

    List<PointRuleCategory> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointRuleCategory findById(Long id);

    List<PointRuleCategory> listPointRuleCategories();

    List<PointRuleCategory> listPointRuleCategoriesByServerId(String serverId);

    void registerDefaultPointRuleCategory(List<Long> ids, String serverId);
}