// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * </ul>
 *
 */
public class ListTempAuthDefaultRuleResponse {

	@ItemType(AclinkFormValuesDTO.class)
	private AclinkFormValuesDTO maxDuration;
	private AclinkFormValuesDTO maxCount;

	public AclinkFormValuesDTO getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(AclinkFormValuesDTO maxDuration) {
		this.maxDuration = maxDuration;
	}

	public AclinkFormValuesDTO getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(AclinkFormValuesDTO maxCount) {
		this.maxCount = maxCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
