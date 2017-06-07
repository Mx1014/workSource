package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT_JSON: form 的数据以 json 形式进行描述</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormTemplateType {
	DEFAULT_JSON("DEFAULT_JSON");
	
	private String code;
	
	private GeneralFormTemplateType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormTemplateType fromCode(String code) {
		for(GeneralFormTemplateType v : GeneralFormTemplateType.values()) {
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
