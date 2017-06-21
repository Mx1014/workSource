package com.everhomes.rest.salary;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>groupId: 标签分类id</li>
 * <li>userId: 用户id</li>
 * <li>groupEntryId: 项目标签id</li>
 * <li>originEntryId: 项目标签原始id</li>
 * <li>salaryValue: 项目标签值</li>
 * </ul>
 */
public class SalaryEmployeeOriginValDTO {

    private Long id;

    private Long groupId;

    private Long userId;

    private Long groupEntryId;

    private Long originEntryId;

    private String salaryValue;

    public SalaryEmployeeOriginValDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupEntryId() {
        return groupEntryId;
    }

    public void setGroupEntryId(Long groupEntryId) {
        this.groupEntryId = groupEntryId;
    }

    public Long getOriginEntryId() {
        return originEntryId;
    }

    public void setOriginEntryId(Long originEntryId) {
        this.originEntryId = originEntryId;
    }

    public String getSalaryValue() {
        return salaryValue;
    }

    public void setSalaryValue(String salaryValue) {
        this.salaryValue = salaryValue;
    }
}
