package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页页码</li>
 * <li>records: 审批记录列表, 参考{@link com.everhomes.rest.general_approval.GeneralApprovalRecordDTO}</li>
 * </ul>
 */
public class ListGeneralApprovalRecordsResponse {

    private Long nextPageAnchor;

    @ItemType(GeneralApprovalRecordDTO.class)
    private List<GeneralApprovalRecordDTO> records;

    public ListGeneralApprovalRecordsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GeneralApprovalRecordDTO> getRecords() {
        return records;
    }

    public void setRecords(List<GeneralApprovalRecordDTO> records) {
        this.records = records;
    }
}
