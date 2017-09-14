// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 政府机构id</li>
 *     <li>isSignedup: 是否左邻注册用户</li>
 *     <li>status: 状态:1-待认证 3-已同意 0-已拒绝</li>
 *     <li>pageOffset: pageOffset</li>
 *     <li>pageAnchor: 页码</li>
 *     <li>pageSize: 每页大小</li>
 *     <li>keywords: 关键字</li>
 *     <li>visibleFlag: visibleFlag</li>
 *     <li>targetTypes: 是否注册{@link com.everhomes.rest.organization.OrganizationMemberTargetType}</li>
 *     <li>filterScopeTypes: 过滤范围类型{@link com.everhomes.rest.organization.FilterOrganizationContactScopeType}</li>
 *     <li>checkInTime:入职日期</li>
 *     <li>employmentTime:转正日期</li>
 *     <li>contractEndTime:合同结束日期</li>
 *     <li>employeeStatus:员工状态</li>
 *     <li>contractPartyId:合同主体</li>
 *     <li>workPlaceId:工作地点</li>
 * </ul>
 */
public class ListOrganizationContactCommand {
    @NotNull
    private Long organizationId;
    private Byte isSignedup;
    private Byte status;
    private Integer pageOffset;
    private Long pageAnchor;
    private Integer pageSize;

    private String keywords;

    private Byte visibleFlag;

    @ItemType(String.class)
    private List<String> targetTypes;

    @ItemType(String.class)
    private List<String> filterScopeTypes;

    private Date checkInTimeStart;//入职日期
    private Date checkInTimeEnd;
    private Date employmentTimeStart;//转正日期
    private Date employmentTimeEnd;
    private Date contractEndTimeStart;//合同结束日期
    private Date contractEndTimeEnd;
    private Byte employeeStatus;//员工状态
    private Long contractPartyId;//合同主体
    private Long workPlaceId;//工作地点

    @ItemType(Long.class)
    private List<Long> exceptIds;

    public ListOrganizationContactCommand() {
    }


    public Long getOrganizationId() {
        return organizationId;
    }


    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getIsSignedup() {
        return isSignedup;
    }

    public void setIsSignedup(Byte isSignedup) {
        this.isSignedup = isSignedup;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }


    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }


    public String getKeywords() {
        return keywords;
    }


    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<String> getTargetTypes() {
        return targetTypes;
    }

    public void setTargetTypes(List<String> targetTypes) {
        this.targetTypes = targetTypes;
    }

    public List<String> getFilterScopeTypes() {
        return filterScopeTypes;
    }

    public void setFilterScopeTypes(List<String> filterScopeTypes) {
        this.filterScopeTypes = filterScopeTypes;
    }


    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Long getContractPartyId() {
        return contractPartyId;
    }

    public void setContractPartyId(Long contractPartyId) {
        this.contractPartyId = contractPartyId;
    }

    public Long getWorkPlaceId() {
        return workPlaceId;
    }

    public void setWorkPlaceId(Long workPlaceId) {
        this.workPlaceId = workPlaceId;
    }

    public Date getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(Date checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public Date getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(Date checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public Date getEmploymentTimeStart() {
        return employmentTimeStart;
    }

    public void setEmploymentTimeStart(Date employmentTimeStart) {
        this.employmentTimeStart = employmentTimeStart;
    }

    public Date getEmploymentTimeEnd() {
        return employmentTimeEnd;
    }

    public void setEmploymentTimeEnd(Date employmentTimeEnd) {
        this.employmentTimeEnd = employmentTimeEnd;
    }

    public Date getContractEndTimeStart() {
        return contractEndTimeStart;
    }

    public void setContractEndTimeStart(Date contractEndTimeStart) {
        this.contractEndTimeStart = contractEndTimeStart;
    }

    public Date getContractEndTimeEnd() {
        return contractEndTimeEnd;
    }

    public void setContractEndTimeEnd(Date contractEndTimeEnd) {
        this.contractEndTimeEnd = contractEndTimeEnd;
    }

    public List<Long> getExceptIds() {
        return exceptIds;
    }

    public void setExceptIds(List<Long> exceptIds) {
        this.exceptIds = exceptIds;
    }
}
