// @formatter:off
package com.everhomes.rest.news;

/**
 * <ul>新闻状态
 * <li>INACTIVE: 0 删除状态</li>
 * <li>DRAFT: 1 草稿状态</li>
 * <li>ACTIVE: 2 发布状态</li>
 * </ul>
 */
public enum NewsStatus {
	INACTIVE((byte)0), DRAFT((byte)1), ACTIVE((byte)2);
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
