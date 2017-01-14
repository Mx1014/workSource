package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>entityType: {@link com.everhomes.rest.flow.FlowCaseEntityType}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowCaseEntity {
	private String key;
	private String value;
	private String entityType;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
