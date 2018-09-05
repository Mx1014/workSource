package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>entityType: {@link com.everhomes.rest.flow.FlowCaseEntityType}</li>
 * <li>key</li>
 * <li>value</li>
 * </ul>
 * @author janson
 *
 */
public class FlowCaseEntity {
	private String key;
	private String value;
	private String entityType;

	public FlowCaseEntity() {
		super();
	}

	public FlowCaseEntity(String key, String value, String entityType) {
		super();
		this.key = key;
		this.value = value;
		this.entityType = entityType;
	}
	
	@ItemType(FlowCaseEntity.class)
    private List<FlowCaseEntity> chargingItemEntities;
	
	public List<FlowCaseEntity> getChargingItemEntities() {
		return chargingItemEntities;
	}

	public void setChargingItemEntities(List<FlowCaseEntity> chargingItemEntities) {
		this.chargingItemEntities = chargingItemEntities;
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
