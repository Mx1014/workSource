package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CardTransactionOfMonth implements Comparable<CardTransactionOfMonth>{
	private Timestamp date;
	private BigDecimal consumeAmount;
	private BigDecimal rechargeAmount;
	@ItemType(CardTransactionDTO.class)
	private List<CardTransactionDTO> requests;
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
	public List<CardTransactionDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionDTO> requests) {
		this.requests = requests;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	@Override
	public int compareTo(CardTransactionOfMonth o) {
		if(this.date.getTime() < o.date.getTime())
			return 1;
		if(this.date.getTime() > o.date.getTime())
			return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
        if(this == obj)
        	return true;
        if(obj instanceof CardTransactionOfMonth)
        	return this.date.getTime() == ((CardTransactionOfMonth)obj).date.getTime();
        return false;
    }
}
