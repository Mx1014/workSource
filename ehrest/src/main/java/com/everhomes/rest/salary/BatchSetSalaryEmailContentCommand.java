package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>参数:
 * <li>salaryGroupCmd: 单个批次设置参数</li>
 * </ul>
 */
public class BatchSetSalaryEmailContentCommand {
    @ItemType(SetSalaryEmailContentCommand.class)
    private List<SetSalaryEmailContentCommand> salaryGroupCmd;

    public List<SetSalaryEmailContentCommand> getSalaryGroupCmd() {
        return salaryGroupCmd;
    }

    public void setSalaryGroupCmd(List<SetSalaryEmailContentCommand> salaryGroupCmd) {
        this.salaryGroupCmd = salaryGroupCmd;
    }
}
