// @formatter:off
package com.everhomes.rentalv2;

import java.util.List;

public interface Rentalv2PriceRuleProvider {

	void createRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule);

	void updateRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule);

	Rentalv2PriceRule findRentalv2PriceRuleById(Long id);

	List<Rentalv2PriceRule> listRentalv2PriceRule();

	List<Rentalv2PriceRule> listPriceRuleByOwner(String ownerType, Long ownerId);

}