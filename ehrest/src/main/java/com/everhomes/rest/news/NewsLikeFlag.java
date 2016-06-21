// @formatter:off
package com.everhomes.rest.news;

/**
 * 
 * <ul>点赞标记
 * <li>NONE: 未点赞</li>
 * <li>LIKE: 已点赞</li>
 * </ul>
 */
public enum NewsLikeFlag {
	NONE((byte) 0), LIKE((byte) 1);
	private byte code;

	private NewsLikeFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public NewsLikeFlag fromCode(Byte code) {
		if (code != null) {
			for (NewsLikeFlag flag : NewsLikeFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
