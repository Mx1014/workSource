package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>organizationId: 总公司id</li>
 * <li>entities: 字段项 {@link SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class UpdateGroupEntitiesCommand {
    private Long organizationId;

    @ItemType(SalaryGroupEntityDTO.class)
    List<SalaryGroupEntityDTO> entities;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<SalaryGroupEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<SalaryGroupEntityDTO> entities) {
        this.entities = entities;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
