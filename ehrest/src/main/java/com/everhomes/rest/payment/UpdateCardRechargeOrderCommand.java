package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>rechargeStatus:  0:充值失败   1:处理中  2:充值成功 3:处理完成  4:已退款{@link com.everhomes.rest.payment.CardRechargeStatus}</li>
 * </ul>
 */
public class UpdateCardRechargeOrderCommand {
	private java.lang.Byte       rechargeStatus;
	private java.lang.Long       id;
	public java.lang.Byte getRechargeStatus() {
		return rechargeStatus;
	}
	public void setRechargeStatus(java.lang.Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
