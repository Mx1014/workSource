package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>NUM_LIMIT: 数字限制</li>
 * <li>TEXT_LIMIT: 字符限制</li>
 * <li>IMAGE_COUNT_SIZE_LIMIT: 限制图片数，以及图片的大小</li>
 * <li>FILE_COUNT_SIZE_LIMIT: 限制文件数，以及文件的大小</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormValidatorType {
	NUM_LIMIT("NUM_LIMIT"),TEXT_LIMIT("TEXT_LIMIT"), IMAGE_COUNT_SIZE_LIMIT("IMAGE_COUNT_SIZE_LIMIT"), FILE_COUNT_SIZE_LIMIT("FILE_COUNT_SIZE_LIMIT");
	
	private String code;
	
	private GeneralFormValidatorType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormValidatorType fromCode(String code) {
		for(GeneralFormValidatorType v : GeneralFormValidatorType.values()) {
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
