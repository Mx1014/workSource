package com.everhomes.rest.user;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>STRING("string"): 字符串</li>
 *  <li>DATETIME("datetime"): 时间</li>
 *  <li>DECIMAL("decimal"):小数</li>
 *  <li>NUMBER("number"):整数</li>
 *  <li>BLOB("blob"):二进制大对象</li>
 * </ul>
 */
public enum FieldType {

	 STRING("string"), DATETIME("datetime"), DECIMAL("decimal"), NUMBER("number"), BLOB("blob");
	
	private String code;
	
	private FieldType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static FieldType fromCode(String code) {
		for(FieldType v : FieldType.values()) {
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
