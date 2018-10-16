package com.everhomes.rest.parking.jieshun;

import java.util.List;

import com.everhomes.rest.parking.jieshun.FeeItem;
import com.everhomes.util.StringHelper;

public class CardType {
	private String cardType;
	private List<FeeItem> feeItems;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public List<FeeItem> getFeeItems() {
		return feeItems;
	}

	public void setFeeItems(List<FeeItem> feeItems) {
		this.feeItems = feeItems;
	}

}
