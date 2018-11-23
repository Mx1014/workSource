// @formatter:off
package com.everhomes.rentalv2;

import java.util.List;

import com.everhomes.rest.rentalv2.admin.PriceRuleDTO;

public interface Rentalv2PriceRuleProvider {

	void createRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule);

	void createRentalv2PriceClassification(RentalPriceClassification classification);

	void updateRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule);

	Rentalv2PriceRule findRentalv2PriceRuleById(Long id);

	List<Rentalv2PriceRule> listRentalv2PriceRule();

	List<Rentalv2PriceRule> listPriceRuleByOwner(String resourceType, String ownerType, Long ownerId);

	void deletePriceRuleByOwnerId(String resourceType, String ownerType, Long ownerId);

	Rentalv2PriceRule findRentalv2PriceRuleByOwner(String resourceType, String ownerType, Long ownerId, Byte rentalType);

}