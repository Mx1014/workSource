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
 * <li>reportType: 汇报类型</li>
 * <li>reportTypeText: 汇报类型(显示文本)</li>
 * <li>applierName: 申请人姓名</li>
 * <li>updateTime: 修改时间</li>
 * <li>createTime: 创建时间</li>
 * <li>receiverNames: 接收人姓名(显示文本)</li>
 * <li>iconUrl: 图标url</li>
 * <li>applierUserId: 申请人id</li>
 * <li>applierUserAvatar: 申请人头像</li>
 * <li>receivers: 接收人 参考{@link com.everhomes.rest.ui.user.SceneContactDTO}</li>
 * <li>reportTime: 汇报时间</li>
 * <li>reportTimeText: 汇报时间(显示文本)</li>
 * <li>validText: 截止时间(显示文本)</li>
 * <li>validitySetting: 提交时间设置</li>
 * <li>values: 表单各项值 参考{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>readStatus: 0-未读 1-已读 参考{@link com.everhomes.rest.workReport.WorkReportReadStatus}</li>
 * <li>ownerToken: 实体标识(对接评论模块)</li>
 * </ul>
 */
public class WorkReportValDTO {

    //  common
    private Long reportValId;

    private Long reportId;

    private String title;

    private Byte reportType;

    private String reportTypeText;

    private String applierName;

    private Timestamp updateTime;

    private Timestamp createTime;

    //  list
    private String receiverNames;

    private String iconUrl;

    //  get
    private Long applierUserId;

    private Long applierDetailId;

    private String applierUserAvatar;

    @ItemType(SceneContactDTO.class)
    private List<SceneContactDTO> receivers;

    private Timestamp reportTime;

    private String reportTimeText;

    private String validText;

    private ReportValiditySettingDTO validitySetting;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> values;

    private Byte readStatus;

    private String ownerToken;

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

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
    }

    public String getReportTypeText() {
        return reportTypeText;
    }

    public void setReportTypeText(String reportTypeText) {
        this.reportTypeText = reportTypeText;
    }

    public String getApplierName() {
        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
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

    public String getReceiverNames() {
        return receiverNames;
    }

    public void setReceiverNames(String receiverNames) {
        this.receiverNames = receiverNames;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getApplierUserId() {
        return applierUserId;
    }

    public void setApplierUserId(Long applierUserId) {
        this.applierUserId = applierUserId;
    }

    public Long getApplierDetailId() {
        return applierDetailId;
    }

    public void setApplierDetailId(Long applierDetailId) {
        this.applierDetailId = applierDetailId;
    }

    public String getApplierUserAvatar() {
        return applierUserAvatar;
    }

    public void setApplierUserAvatar(String applierUserAvatar) {
        this.applierUserAvatar = applierUserAvatar;
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

    public String getReportTimeText() {
        return reportTimeText;
    }

    public void setReportTimeText(String reportTimeText) {
        this.reportTimeText = reportTimeText;
    }

    public String getValidText() {
        return validText;
    }

    public void setValidText(String validText) {
        this.validText = validText;
    }

    public ReportValiditySettingDTO getValiditySetting() {
        return validitySetting;
    }

    public void setValiditySetting(ReportValiditySettingDTO validitySetting) {
        this.validitySetting = validitySetting;
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

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
