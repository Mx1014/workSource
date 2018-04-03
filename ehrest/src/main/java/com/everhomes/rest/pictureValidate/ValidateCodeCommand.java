// @formatter:off
package com.everhomes.rest.pictureValidate;

import com.everhomes.util.StringHelper;

public class ValidateCodeCommand {
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
