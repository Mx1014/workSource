package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 原始formid</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormIdCommand {
	private Long formOriginId;

	//	added by LiMingDang for approval1.6
	private String moduleType;

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
