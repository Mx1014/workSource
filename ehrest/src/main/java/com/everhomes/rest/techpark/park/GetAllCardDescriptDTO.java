package com.everhomes.rest.techpark.park;

import java.util.List;

import com.everhomes.discover.ItemType;

public class GetAllCardDescriptDTO {
	@ItemType(String.class)
	private List<String> cardDescript;
	private boolean success;
	public List<String> getCardDescript() {
		return cardDescript;
	}
	public void setCardDescript(List<String> cardDescript) {
		this.cardDescript = cardDescript;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
