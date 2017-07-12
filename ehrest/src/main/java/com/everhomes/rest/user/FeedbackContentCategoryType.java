package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0-其它</li>
 *  <li>1-产品bug</li>
 *  <li>2-产品改进</li>
 *  <li>3-版本问题</li>
 *  <li>11-敏感信息</li>
 *  <li>12-版权问题</li>
 *  <li>13-暴力色情</li>
 *  <li>14-诈骗和虚假信息</li>
 *  <li>15-骚扰</li>
 *  <li>16-谣言</li>
 *  <li>17-恶意营销</li>
 *  <li>18-诱导分享</li>
 *  <li>19-政治</li>
 * </ul>
 *
 */
public enum FeedbackContentCategoryType {
	OTHERS((byte)0,"其它"), BUGS((byte)1,"产品bug"),IMPROVEMENT((byte)2,"产品改进"), VERSION((byte)3,"版本问题"),SENSITIVE_INFO((byte)11,"敏感信息"),
	COPYRIGHT((byte)12,"版权问题"),VIO_POR((byte)13,"暴力色情"), DEFRAUD((byte)14,"诈骗和虚假信息"),HARASS((byte)15,"骚扰"), RUMOUR((byte)16,"谣言"),
	MALICIOUS_MARKETING((byte)17,"恶意营销"), INDUCE_TO_SHARE((byte)18,"诱导分享"), POLITICS((byte)19,"政治");
	
	private byte code;
	private String text;
	
	private FeedbackContentCategoryType(byte code, String text){
		this.code = code;
		this.text = text;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getText(){
		return this.text;
	}
	
	public static FeedbackContentCategoryType fromStatus(byte code) {
		for(FeedbackContentCategoryType v : FeedbackContentCategoryType.values()) {
			if(v.getCode() == code)
				return v;
		}
		return OTHERS;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
