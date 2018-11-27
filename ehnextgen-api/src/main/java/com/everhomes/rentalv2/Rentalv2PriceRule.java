// @formatter:off
package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PriceRules;
import com.everhomes.util.StringHelper;

import java.util.List;

public class Rentalv2PriceRule extends EhRentalv2PriceRules {
	
	private static final long serialVersionUID = 5296764597917186698L;

	private List<RentalPriceClassification> priceClassification;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<RentalPriceClassification> getPriceClassification() {
		return priceClassification;
	}

	public void setPriceClassification(List<RentalPriceClassification> priceClassification) {
		this.priceClassification = priceClassification;
	}
}