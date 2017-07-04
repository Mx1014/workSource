package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>参数:
 * <li>salaryGroupCmd: 单个批次设置参数参考 {@link com.everhomes.rest.salary.UpdateSalaryGroupEntitiesVisableCommand}</li>
 * </ul>
 */
public class BatchUpdateSalaryGroupEntitiesVisableCommand {
    @ItemType(SetSalaryEmailContentCommand.class)
    private List<UpdateSalaryGroupEntitiesVisableCommand> salaryGroupCmd;

    public List<UpdateSalaryGroupEntitiesVisableCommand> getSalaryGroupCmd() {
        return salaryGroupCmd;
    }

    public void setSalaryGroupCmd(List<UpdateSalaryGroupEntitiesVisableCommand> salaryGroupCmd) {
        this.salaryGroupCmd = salaryGroupCmd;
    }
}
