package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: 选择人员ids</li>
 * <li>operationType: 操作类型 0-入职 1-转正 2-调整 3-离职 参考{@link com.everhomes.rest.archives.ArchivesOperationType}</li>
 * </ul>
 */
public class CheckOperationCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Byte operationType;

    public CheckOperationCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
