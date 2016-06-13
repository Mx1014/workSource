package com.everhomes.rest.payment;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>cardId: 卡号</li>
 * <li>amount: 充值金额</li>
 * </ul>
 */
public class RechargeCardCommand {
	private String ownerType;
    private Long ownerId;
    private Long cardId;
    private BigDecimal amount;
    
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
