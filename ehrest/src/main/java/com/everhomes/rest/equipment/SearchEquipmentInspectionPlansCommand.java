package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 计划所属组织等的id</li>
 *  <li>ownerType: 计划所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 计划所属管理处id</li>
 *  <li>targetType: 计划所属管理处类型</li>
 *  <li>status: 计划状态 参考{@link com.everhomes.rest.equipment.EquipmentPlanStatus}</li>
 *  <li>planType: 类型 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>repeatType: 执行周期类型 repeatType: 0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 类型</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class SearchEquipmentInspectionPlansCommand {
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long targetId;
    @ItemType(Long.class)
    private List<Long> targetIds;

    private String targetType;

    private Byte status;

    private  Byte planType;

    private String keyword;

    private Long pageAnchor;

    private Integer pageSize;

    private Long inspectionCategoryId;

    private Byte repeatType;

    private Integer namespaceId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getPlanType() {
        return planType;
    }

    public void setPlanType(Byte planType) {
        this.planType = planType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getInspectionCategoryId() {
        return inspectionCategoryId;
    }

    public void setInspectionCategoryId(Long inspectionCategoryId) {
        this.inspectionCategoryId = inspectionCategoryId;
    }

    public Byte getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Byte repeatType) {
        this.repeatType = repeatType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<Long> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<Long> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
