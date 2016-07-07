package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CardTransactionOfMonth implements Comparable<CardTransactionOfMonth>{
	private Long date;
	private BigDecimal consumeAmount;
	private BigDecimal rechargeAmount;
	@ItemType(CardTransactionFromVendorDTO.class)
	private List<CardTransactionFromVendorDTO> requests;
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
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
	
	public List<CardTransactionFromVendorDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionFromVendorDTO> requests) {
		this.requests = requests;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	@Override
	public int compareTo(CardTransactionOfMonth o) {
		if(this.date.longValue() < o.date.longValue())
			return 1;
		if(this.date.longValue() > o.date.longValue())
			return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
        if(this == obj)
        	return true;
        if(obj instanceof CardTransactionOfMonth)
        	return this.date.longValue() == ((CardTransactionOfMonth)obj).date.longValue();
        return false;
    }
}
