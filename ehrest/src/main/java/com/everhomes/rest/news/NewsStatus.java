// @formatter:off
package com.everhomes.rest.news;

/**
 * <ul>新闻状态
 * <li>INACTIVE: 失效</li>
 * <li>WAITING_FOR_CONFIRMATION: 等待审批</li>
 * <li>ACTIVE: 有效</li>
 * </ul>
 */
public enum NewsStatus {
	INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
	private byte code;
	private NewsStatus(byte code) {
		this.code = code;
	}
	public byte getCode(){
		return this.code;
	}
	public NewsStatus fromCode(Byte code){
		if (code != null) {
			for (NewsStatus status : NewsStatus.values()) {
				if (code.byteValue() == status.getCode()) {
					return status;
				}
			}
		}
		
		return null;
	}
}
