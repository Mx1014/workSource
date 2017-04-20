// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>CREATE: 创建订单</li>
 * <li>CANCEL: 取消订单</li>
 * <li>CONFIRM_MONEY: 确定金额</li>
 * <li>UPDATE_MONEY: 修改金额</li>
 * <li>PRINT: 打印订单</li>
 * <li>PAYING: 支付中</li>
 * <li>PAID: 支付完成</li>
 * </ul>
 */
public enum ExpressActionEnum {
	CREATE("create"), CANCEL("cancel"), CONFIRM_MONEY("confirm_money"), 
		UPDATE_MONEY("update_money"), PRINT("print"), PAYING("paying"), PAID("paid"); 

	private String code;

	private ExpressActionEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ExpressActionEnum fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (ExpressActionEnum expressActionEnum : ExpressActionEnum.values()) {
				if (expressActionEnum.getCode().equals(code)) {
					return expressActionEnum;
				}
			}
		}
		return null;
	}
	
}
