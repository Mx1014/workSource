// @formatter:off
package com.everhomes.rest.news;

/**
 * <ul>
 * 新闻评论是否可删除标记
 * <li>NONE: 不可删除</li>
 * <li>DELETE: 可删除</li>
 * </ul>
 */
public enum NewsCommentDeleteFlag {
	NONE((byte)0), DELETE((byte)1);
	private byte code;
	private NewsCommentDeleteFlag(byte code) {
		this.code = code;
	}
	public byte getCode(){
		return code;
	}
	public NewsCommentDeleteFlag fromCode(Byte code){
		if (code != null) {
			for(NewsCommentDeleteFlag flag : NewsCommentDeleteFlag.values()){
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
