package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalRecordDTO;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页页码</li>
 * <li>records: 审批记录列表, 参考{@link EnterpriseApprovalRecordDTO}</li>
 * </ul>
 */
public class ListGeneralApprovalRecordsResponse {

    private Long nextPageAnchor;

    @ItemType(EnterpriseApprovalRecordDTO.class)
    private List<EnterpriseApprovalRecordDTO> records;

    public ListGeneralApprovalRecordsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnterpriseApprovalRecordDTO> getRecords() {
        return records;
    }

    public void setRecords(List<EnterpriseApprovalRecordDTO> records) {
        this.records = records;
    }
}
