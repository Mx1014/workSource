package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.ui.user.SceneContactDTO;
import com.everhomes.rest.ui.user.SceneContactV2DTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>reportValId: 工作汇报单id</li>
 * <li>reportId: 工作汇报id</li>
 * <li>title: 标题</li>
 * <li>applierName: 申请人姓名</li>
 * <li>applierUserId: 申请人id</li>
 * <li>applierUserAvatar: 申请人头像</li>
 * <li>receiverNames: 接收人姓名(外部快速显示)</li>
 * <li>receivers: 接收人 参考{@link com.everhomes.rest.ui.user.SceneContactDTO}</li>
 * <li>reportTime: 汇报时间</li>
 * <li>reportType: 汇报类型</li>
 * <li>values: 表单各项值 参考{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>readStatus: 0-未读 1-已读 参考{@link com.everhomes.rest.workReport.WorkReportReadStatus}</li>
 * <li>updateTime: 修改时间</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class WorkReportValDTO {

    private Long reportValId;

    private Long reportId;

    private String title;

    private String applierName;

    private Long applierUserId;

    private String applierUserAvatar;

    private String receiverNames;

    @ItemType(SceneContactDTO.class)
    private List<SceneContactDTO> receivers;

    private Timestamp reportTime;

    private Byte reportType;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> values;

    private Byte readStatus;

    private Timestamp updateTime;

    private Timestamp createTime;

    public WorkReportValDTO() {
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
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

    public String getApplierUserAvatar() {
        return applierUserAvatar;
    }

    public void setApplierUserAvatar(String applierUserAvatar) {
        this.applierUserAvatar = applierUserAvatar;
    }

    public String getReceiverNames() {
        return receiverNames;
    }

    public void setReceiverNames(String receiverNames) {
        this.receiverNames = receiverNames;
    }

    public List<SceneContactDTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<SceneContactDTO> receivers) {
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

    public Byte getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Byte readStatus) {
        this.readStatus = readStatus;
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
