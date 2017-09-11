package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>checkInTimeStart: 入职起始日期</li>
 * <li>checkInTimeEnd: 入职结束日期</li>
 * <li>employmentTimeStart: 转正起始日期</li>
 * <li>employmentTimeEnd: 转正结束日期</li>
 * <li>contractTimeStart: 合同开始日期</li>
 * <li>contractTimeEnd: 合同结束日期</li>
 * <li>employeeStatus: 员工状态</li>
 * <li>contractPartyId: 合同主体 id</li>
 * <li>contactName: 姓名</li>
 * <li>departmentId: 部门 id</li>
 * <li>workingPlaceId: 工作地点 id</li>
 * <li>pageAnchor: 锚点值</li>
 * <li>pageSize: 页大小</li>
 * </ul>
 */
public class ListArchivesEmployeesCommand {

    private Long organizationId;

    private Timestamp checkInTimeStart;

    private Timestamp checkInTimeEnd;

    private Timestamp employmentTimeStart;

    private Timestamp employmentTimeEnd;

    private Timestamp contractTimeStart;

    private Timestamp contractTimeEnd;

    private Byte employeeStatus;

    private Long contractPartyId;

    private String contactName;

    private Long departmentId;

    private Long workingPlaceId;

    private Long pageAnchor;

    private Integer pageSize;

    public ListArchivesEmployeesCommand() {
    }

    public Timestamp getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(Timestamp checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public Timestamp getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(Timestamp checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public Timestamp getEmploymentTimeStart() {
        return employmentTimeStart;
    }

    public void setEmploymentTimeStart(Timestamp employmentTimeStart) {
        this.employmentTimeStart = employmentTimeStart;
    }

    public Timestamp getEmploymentTimeEnd() {
        return employmentTimeEnd;
    }

    public void setEmploymentTimeEnd(Timestamp employmentTimeEnd) {
        this.employmentTimeEnd = employmentTimeEnd;
    }

    public Timestamp getContractTimeStart() {
        return contractTimeStart;
    }

    public void setContractTimeStart(Timestamp contractTimeStart) {
        this.contractTimeStart = contractTimeStart;
    }

    public Timestamp getContractTimeEnd() {
        return contractTimeEnd;
    }

    public void setContractTimeEnd(Timestamp contractTimeEnd) {
        this.contractTimeEnd = contractTimeEnd;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getContractPartyId() {
        return contractPartyId;
    }

    public void setContractPartyId(Long contractPartyId) {
        this.contractPartyId = contractPartyId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getWorkingPlaceId() {
        return workingPlaceId;
    }

    public void setWorkingPlaceId(Long workingPlaceId) {
        this.workingPlaceId = workingPlaceId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
