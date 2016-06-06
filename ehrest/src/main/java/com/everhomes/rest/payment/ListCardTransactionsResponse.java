package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListCardTransactionsResponse {
	private Long nextPageAnchor;
	private Timestamp date;
	private BigDecimal consumeAmount;
	private BigDecimal rechargeAmount;
	@ItemType(CardTransactionsDTO.class)
	private List<CardTransactionsDTO> requests;
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}
	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	
	public List<CardTransactionsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionsDTO> requests) {
		this.requests = requests;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
