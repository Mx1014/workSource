package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.ui.user.SceneContactV2DTO;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>reportId: 工作汇报单id</li>
 * <li>title: 标题</li>
 * <li>applierName: 申请人姓名</li>
 * <li>applierUserId: 申请人id</li>
 * <li>receiverNames: 接收人姓名(外部快速显示)</li>
 * <li>receivers: 接收人 {@link com.everhomes.rest.ui.user.SceneContactV2DTO}</li>
 * <li>reportTime: 汇报时间</li>
 * <li>reportType: 汇报类型</li>
 * <li>values: 表单各项值 参考{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>updateTime: 修改时间</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class WorkReportValDTO {

    private String reportId;

    private String title;

    private String applierName;

    private Long applierUserId;

    private String receiverNames;

    @ItemType(SceneContactV2DTO.class)
    private List<SceneContactV2DTO> receivers;

    private Timestamp reportTime;

    private Byte reportType;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> values;

    private Timestamp updateTime;

    private Timestamp createTime;

    public WorkReportValDTO() {
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApplierName() {
        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    public Long getApplierUserId() {
        return applierUserId;
    }

    public void setApplierUserId(Long applierUserId) {
        this.applierUserId = applierUserId;
    }

    public String getReceiverNames() {
        return receiverNames;
    }

    public void setReceiverNames(String receiverNames) {
        this.receiverNames = receiverNames;
    }

    public List<SceneContactV2DTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<SceneContactV2DTO> receivers) {
        this.receivers = receivers;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
    }

    public List<GeneralFormFieldDTO> getValues() {
        return values;
    }

    public void setValues(List<GeneralFormFieldDTO> values) {
        this.values = values;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
