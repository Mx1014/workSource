package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工档案id</li>
 * <li>duplicateFlag: 是否为重复数据</li>
 * </ul>
 */
public class ImportArchivesJudgments {

    private Long detailId;

    private boolean duplicateFlag;

    public ImportArchivesJudgments() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public boolean isDuplicateFlag() {
        return duplicateFlag;
    }

    public void setDuplicateFlag(boolean duplicateFlag) {
        this.duplicateFlag = duplicateFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
