package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>salaryGroupEntity: 批次字段，参考 {@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class GetSalaryGroupResponse {

    @ItemType(SalaryGroupEntityDTO.class)
    private List<SalaryGroupEntityDTO> salaryGroupEntity;

    public GetSalaryGroupResponse() {
    }

    public List<SalaryGroupEntityDTO> getSalaryGroupEntity() {
        return salaryGroupEntity;
    }

    public void setSalaryGroupEntity(List<SalaryGroupEntityDTO> salaryGroupEntity) {
        this.salaryGroupEntity = salaryGroupEntity;
    }
}
