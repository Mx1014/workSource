package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryGroupCmd: 单个批次设置参数参考 {@link com.everhomes.rest.salary.UpdateSalaryGroupEntitiesVisableCommand}</li>
 * </ul>
 */
public class BatchUpdateSalaryGroupEntitiesVisableCommand {


    private String ownerType;

    private Long ownerId;

    @ItemType(SetSalaryEmailContentCommand.class)
    private List<UpdateSalaryGroupEntitiesVisableCommand> salaryGroupCmd;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public List<UpdateSalaryGroupEntitiesVisableCommand> getSalaryGroupCmd() {
        return salaryGroupCmd;
    }

    public void setSalaryGroupCmd(List<UpdateSalaryGroupEntitiesVisableCommand> salaryGroupCmd) {
        this.salaryGroupCmd = salaryGroupCmd;
    }
}
