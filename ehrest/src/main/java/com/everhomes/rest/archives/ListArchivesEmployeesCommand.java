package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

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
 * <li>keywords: 搜索关键词</li>
 * <li>departmentId: 部门 id</li>
 * <li>pageAnchor: 锚点值</li>
 * <li>pageSize: 页大小</li>
 * </ul>
 */
public class ListArchivesEmployeesCommand {

    private Long organizationId;

    private String checkInTimeStart;

    private String checkInTimeEnd;

    private String employmentTimeStart;

    private String employmentTimeEnd;

    private String contractTimeStart;

    private String contractTimeEnd;

    private Byte employeeStatus;

    private Long contractPartyId;

    private String keywords;

    private Long departmentId;

    private Long pageAnchor;

    private Integer pageSize;

    public ListArchivesEmployeesCommand() {
    }

    public String getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(String checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public String getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(String checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public String getEmploymentTimeStart() {
        return employmentTimeStart;
    }

    public void setEmploymentTimeStart(String employmentTimeStart) {
        this.employmentTimeStart = employmentTimeStart;
    }

    public String getEmploymentTimeEnd() {
        return employmentTimeEnd;
    }

    public void setEmploymentTimeEnd(String employmentTimeEnd) {
        this.employmentTimeEnd = employmentTimeEnd;
    }

    public String getContractTimeStart() {
        return contractTimeStart;
    }

    public void setContractTimeStart(String contractTimeStart) {
        this.contractTimeStart = contractTimeStart;
    }

    public String getContractTimeEnd() {
        return contractTimeEnd;
    }

    public void setContractTimeEnd(String contractTimeEnd) {
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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
