package com.everhomes.rest.parking;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListCardTypeResponse {
	
	@ItemType(value = String.class)
	private List<String> cardTypes;

	public List<String> getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(List<String> cardTypes) {
		this.cardTypes = cardTypes;
	}
 
}
