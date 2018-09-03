// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>返回值:
 * <li>id: id</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryName: 类别名称</li>
 * <li>timeUnit: 请假单位,参考{@link com.everhomes.rest.approval.ApprovalCategoryTimeUnit}</li>
 * <li>timeStep: 最小请假时长，单位对应timeUnit</li>
 * <li>remainderFlag: 是否支持关联余额，1表示支持，0不支持，参考{@link com.everhomes.rest.approval.ApprovalCategoryReminderFlag}</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.approval.ApprovalCategoryStatus}</li>
 * <li>defaultOrder: 排序，值越小越前面</li>
 * <li>remainCount: 假期余额，单位是天</li>
 * <li>remainCountShow: 假期余额remainCount的换算显示，如 3.5天3.5小时</li>
 * <li>timeUnitTipInfo: 时间单位文案信息，如：按天请假，最小单位0.5天；</li>
 * </ul>
 */
public class ApprovalCategoryDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Byte approvalType;
    private String categoryName;
    private String timeUnit;
    private Double timeStep;
    private Byte remainderFlag;
    private Byte status;
    private Integer defaultOrder;
    private Long originId;
    private String handlerType;
    private Double remainCount;
    private String remainCountDisplay;
    private String timeUnitTipInfo;

    public ApprovalCategoryDTO() {
    }

    public ApprovalCategoryDTO(Long id, Byte approvalType, String categoryName) {
        this.id = id;
        this.approvalType = approvalType;
        this.categoryName = categoryName;
    }

    public Byte getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(Byte approvalType) {
        this.approvalType = approvalType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    public Byte getRemainderFlag() {
        return remainderFlag;
    }

    public void setRemainderFlag(Byte remainderFlag) {
        this.remainderFlag = remainderFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getTimeUnitTipInfo() {
        return timeUnitTipInfo;
    }

    public void setTimeUnitTipInfo(String timeUnitTipInfo) {
        this.timeUnitTipInfo = timeUnitTipInfo;
    }

    public Double getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Double remainCount) {
        this.remainCount = remainCount;
    }

    public String getRemainCountDisplay() {
        return remainCountDisplay;
    }

    public void setRemainCountDisplay(String remainCountDisplay) {
        this.remainCountDisplay = remainCountDisplay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
