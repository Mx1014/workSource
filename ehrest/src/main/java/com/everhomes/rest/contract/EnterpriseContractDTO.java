package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>entities: 实体的详细信息 {@link com.everhomes.rest.flow.FlowCaseEntity}</li>
 * </ul>
 * Created by djm on 2018/8/9.
 */
public class EnterpriseContractDTO {

    private String ownerType;
    private Long ownerId;

    @ItemType(FlowCaseEntity.class)
    private List<FlowCaseEntity> entities = new ArrayList<>();

    public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public List<FlowCaseEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<FlowCaseEntity> entities) {
		this.entities = entities;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
