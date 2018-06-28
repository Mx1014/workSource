// @formatter:off
package com.everhomes.welfare;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface WelfareProvider {

	void createWelfare(Welfare welfare);

	void updateWelfare(Welfare welfare);

	Welfare findWelfareById(Long id);

	List<Welfare> listWelfare(Long ownerId, CrossShardListingLocator locator, Integer pageSize);

	void deleteWelfare(Long welfareId);
}