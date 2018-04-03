package com.everhomes.rest.parking;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>cardTypes: 卡类型列表 {@link com.everhomes.rest.parking.ParkingCardType}</li>
 * </ul>
 */
public class ListCardTypeResponse {

	@ItemType(value = ParkingCardType.class)
	private List<ParkingCardType> cardTypes;

	public List<ParkingCardType> getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(List<ParkingCardType> cardTypes) {
		this.cardTypes = cardTypes;
	}

}
