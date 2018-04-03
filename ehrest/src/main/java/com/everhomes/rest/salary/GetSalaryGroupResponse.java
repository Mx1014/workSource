package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>salaryGroupName: 薪酬组（批次）名称</li>
 * <li>salaryGroupEntity: 批次字段，参考 {@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class GetSalaryGroupResponse {

    private String salaryGroupName;

    @ItemType(SalaryGroupEntityDTO.class)
    private List<SalaryGroupEntityDTO> salaryGroupEntity;

    public GetSalaryGroupResponse() {
    }

    public String getSalaryGroupName() {
        return salaryGroupName;
    }

    public void setSalaryGroupName(String salaryGroupName) {
        this.salaryGroupName = salaryGroupName;
    }

    public List<SalaryGroupEntityDTO> getSalaryGroupEntity() {
        return salaryGroupEntity;
    }

    public void setSalaryGroupEntity(List<SalaryGroupEntityDTO> salaryGroupEntity) {
        this.salaryGroupEntity = salaryGroupEntity;
    }
}
