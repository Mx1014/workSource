package com.everhomes.rest.general_approval;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>SINGLE_LINE_TEXT: 单行文本</li>
 * <li>MULTI_LINE_TEXT: 多行文本</li>
 * <li>IMAGE: 图片</li>
 * <li>FILE: 文件</li>
 * <li>INTEGER_TEXT:整数文本</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormFieldType {
	SINGLE_LINE_TEXT("SINGLE_LINE_TEXT"), MULTI_LINE_TEXT("MULTI_LINE_TEXT"), IMAGE("IMAGE"), FILE("FILE"), INTEGER_TEXT("INTEGER_TEXT");
	
	private String code;
	
	private GeneralFormFieldType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormFieldType fromCode(String code) {
		for(GeneralFormFieldType v : GeneralFormFieldType.values()) {
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
