package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>entityType: {@link ReserveOrderContentEntityType}</li>
 * <li>key: 键</li>
 * <li>value: 值</li>
 * </ul>
 *
 */
public class ReserveOrderContentEntity {
	private String key;
	private String value;
	private String entityType;

	public ReserveOrderContentEntity() {
		super();
	}

	public ReserveOrderContentEntity(String key, String value, String entityType) {
		super();
		this.key = key;
		this.value = value;
		this.entityType = entityType;
	}

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
