package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT: 默认渲染类型，每个控件都有自己默认的渲染类型</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormRenderType {
	DEFAULT("DEFAULT");
	
	private String code;
	
	private GeneralFormRenderType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormRenderType fromCode(String code) {
		for(GeneralFormRenderType v : GeneralFormRenderType.values()) {
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
