package com.everhomes.rest.user;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>TEXT("text"): 文本</li>
 *  <li>IMAGE("image"): 图片</li>
 *  <li>AUDIO("audio"): 音频</li>
 *  <li>VIDEO("video"): 视频</li>
 *  <li>FILE("file"): 文件</li>
 * </ul>
 */
public enum FieldContentType {
	 
	TEXT("text"), IMAGE("image"), AUDIO("audio"), VIDEO("video"), FILE("file");
		
	private String code;
	
	private FieldContentType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static FieldContentType fromCode(String code) {
		for(FieldContentType v : FieldContentType.values()) {
			if(StringUtils.equals(v.getCode(), code))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
