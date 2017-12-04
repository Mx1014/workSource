package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>reportId: 汇报id</li>
 * <li>reportValId: 汇报单id</li>
 * <li>reportType: 汇报类型, 0-日报 1-周报 2-月报 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>reportTime: 汇报时间</li>
 * <li>values: 汇报项中，每项对应的值 参考{@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * <li>receiverIds: <List>接收人ids</li>
 * </ul>
 */
public class PostWorkReportValCommand {

    private Long reportId;

    private Long reportValId;

    private Byte reportType;

    private Timestamp reportTime;

    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> values;

    @ItemType(Long.class)
    private List<Long> receiverIds;

    public PostWorkReportValCommand() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    public List<Long> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<Long> receiverIds) {
        this.receiverIds = receiverIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
